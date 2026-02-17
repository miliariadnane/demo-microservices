# =============================================================================
# Kubernetes Workloads — Managed by Terraform
# =============================================================================
#
# This module deploys ALL Kubernetes resources so that `terraform destroy`
# cleans up everything in a single command. No more manual kubectl commands.
# =============================================================================

# ─── Namespace ──────────────────────────────────────────────────────────────

resource "kubernetes_namespace" "app" {
  metadata {
    name = var.project_name
    labels = {
      project     = var.project_name
      environment = var.environment
      managed-by  = "terraform"
    }
  }
}

# ─── Database Secrets ───────────────────────────────────────────────────────

resource "kubernetes_secret" "db_credentials" {
  metadata {
    name      = "db-credentials"
    namespace = kubernetes_namespace.app.metadata[0].name
  }

  data = {
    db_username = var.db_username
    db_password = var.db_password
  }
}

# =============================================================================
# PostgreSQL
# =============================================================================

resource "kubernetes_config_map" "postgres_config" {
  metadata {
    name      = "postgres-config"
    namespace = kubernetes_namespace.app.metadata[0].name
  }

  data = {
    POSTGRES_DB       = "postgres"
    POSTGRES_USER     = var.db_username
    POSTGRES_PASSWORD = var.db_password
  }
}

resource "kubernetes_config_map" "postgres_init" {
  metadata {
    name      = "postgres-init"
    namespace = kubernetes_namespace.app.metadata[0].name
  }

  data = {
    "init.sql" = <<-EOT
      CREATE DATABASE customer;
      CREATE DATABASE product;
      CREATE DATABASE "order";
      CREATE DATABASE notification;
      CREATE DATABASE payment;
      CREATE DATABASE "apikey-manager";
      CREATE DATABASE keycloak;
    EOT
  }
}

resource "kubernetes_stateful_set" "postgres" {
  metadata {
    name      = "postgres"
    namespace = kubernetes_namespace.app.metadata[0].name
    labels    = { app = "postgres" }
  }

  spec {
    service_name = "postgres"
    replicas     = 1

    selector {
      match_labels = { app = "postgres" }
    }

    template {
      metadata {
        labels = { app = "postgres" }
      }

      spec {
        container {
          name              = "postgres"
          image             = "postgres:16-alpine"
          image_pull_policy = "IfNotPresent"

          port {
            container_port = 5432
          }

          env_from {
            config_map_ref {
              name = kubernetes_config_map.postgres_config.metadata[0].name
            }
          }

          volume_mount {
            name       = "init-script"
            mount_path = "/docker-entrypoint-initdb.d/init.sql"
            sub_path   = "init.sql"
          }

          resources {
            requests = {
              cpu    = "100m"
              memory = "256Mi"
            }
            limits = {
              cpu    = "500m"
              memory = "512Mi"
            }
          }
        }

        volume {
          name = "init-script"
          config_map {
            name = kubernetes_config_map.postgres_init.metadata[0].name
            items {
              key  = "init.sql"
              path = "init.sql"
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "postgres" {
  metadata {
    name      = "postgres"
    namespace = kubernetes_namespace.app.metadata[0].name
  }

  spec {
    selector = { app = "postgres" }

    port {
      port        = 5432
      target_port = 5432
    }

    type = "ClusterIP"
  }
}

# =============================================================================
# RabbitMQ
# =============================================================================

resource "kubernetes_config_map" "rabbitmq_config" {
  metadata {
    name      = "rabbitmq-config"
    namespace = kubernetes_namespace.app.metadata[0].name
  }

  data = {
    "enabled_plugins" = "[rabbitmq_management,rabbitmq_peer_discovery_k8s].\n"
    "rabbitmq.conf"   = <<-EOT
      cluster_formation.peer_discovery_backend  = rabbit_peer_discovery_k8s
      cluster_formation.k8s.host = kubernetes.default.svc.cluster.local
      cluster_formation.k8s.address_type = hostname
      cluster_formation.node_cleanup.interval = 30
      cluster_formation.node_cleanup.only_log_warning = true
      cluster_partition_handling = autoheal
      queue_master_locator=min-masters
      loopback_users.guest = false
    EOT
  }
}

resource "kubernetes_service_account" "rabbitmq" {
  metadata {
    name      = "rabbitmq"
    namespace = kubernetes_namespace.app.metadata[0].name
  }
}

resource "kubernetes_role" "rabbitmq" {
  metadata {
    name      = "rabbitmq-peer-discovery-rbac"
    namespace = kubernetes_namespace.app.metadata[0].name
  }

  rule {
    api_groups = [""]
    resources  = ["endpoints"]
    verbs      = ["get"]
  }

  rule {
    api_groups = [""]
    resources  = ["events"]
    verbs      = ["create"]
  }
}

resource "kubernetes_role_binding" "rabbitmq" {
  metadata {
    name      = "rabbitmq-peer-discovery-rbac"
    namespace = kubernetes_namespace.app.metadata[0].name
  }

  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind      = "Role"
    name      = kubernetes_role.rabbitmq.metadata[0].name
  }

  subject {
    kind      = "ServiceAccount"
    name      = kubernetes_service_account.rabbitmq.metadata[0].name
    namespace = kubernetes_namespace.app.metadata[0].name
  }
}

resource "kubernetes_stateful_set" "rabbitmq" {
  metadata {
    name      = "rabbitmq"
    namespace = kubernetes_namespace.app.metadata[0].name
    labels    = { app = "rabbitmq" }
  }

  spec {
    service_name = "rabbitmq"
    replicas     = 1

    selector {
      match_labels = { app = "rabbitmq" }
    }

    template {
      metadata {
        labels = { app = "rabbitmq" }
      }

      spec {
        service_account_name             = kubernetes_service_account.rabbitmq.metadata[0].name
        termination_grace_period_seconds = 10

        node_selector = {
          "kubernetes.io/os" = "linux"
        }

        container {
          name              = "rabbitmq"
          image             = "rabbitmq:3.9.20-management-alpine"
          image_pull_policy = "Always"

          port {
            name           = "http"
            container_port = 15672
            protocol       = "TCP"
          }

          port {
            name           = "amqp"
            container_port = 5672
            protocol       = "TCP"
          }

          volume_mount {
            name       = "config-volume"
            mount_path = "/etc/rabbitmq"
          }

          env {
            name  = "RABBITMQ_ERLANG_COOKIE"
            value = "mycookie"
          }
        }

        volume {
          name = "config-volume"
          config_map {
            name = kubernetes_config_map.rabbitmq_config.metadata[0].name
            items {
              key  = "rabbitmq.conf"
              path = "rabbitmq.conf"
            }
            items {
              key  = "enabled_plugins"
              path = "enabled_plugins"
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "rabbitmq" {
  metadata {
    name      = "rabbitmq"
    namespace = kubernetes_namespace.app.metadata[0].name
  }

  spec {
    selector = { app = "rabbitmq" }

    port {
      name        = "http"
      port        = 15672
      target_port = 15672
      protocol    = "TCP"
    }

    port {
      name        = "amqp"
      port        = 5672
      target_port = 5672
      protocol    = "TCP"
    }

    type = "ClusterIP"
  }
}

# =============================================================================
# Zipkin
# =============================================================================

resource "kubernetes_stateful_set" "zipkin" {
  metadata {
    name      = "zipkin"
    namespace = kubernetes_namespace.app.metadata[0].name
    labels    = { app = "zipkin" }
  }

  spec {
    service_name = "zipkin"
    replicas     = 1

    selector {
      match_labels = { app = "zipkin" }
    }

    template {
      metadata {
        labels = { app = "zipkin" }
      }

      spec {
        container {
          name              = "zipkin"
          image             = "openzipkin/zipkin"
          image_pull_policy = "Always"

          port {
            container_port = 9411
            protocol       = "TCP"
          }

          resources {
            requests = {
              cpu    = "100m"
              memory = "256Mi"
            }
            limits = {
              cpu    = "200m"
              memory = "256Mi"
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "zipkin" {
  metadata {
    name      = "zipkin"
    namespace = kubernetes_namespace.app.metadata[0].name
  }

  spec {
    selector = { app = "zipkin" }

    port {
      port        = 9411
      target_port = 9411
      protocol    = "TCP"
    }

    type = "ClusterIP"
  }
}

# =============================================================================
# Microservices — Dynamic deployment using for_each
# =============================================================================

resource "kubernetes_deployment" "microservice" {
  for_each = var.microservices

  metadata {
    name      = each.key
    namespace = kubernetes_namespace.app.metadata[0].name
    labels    = { app = each.key }
  }

  spec {
    replicas = 1

    selector {
      match_labels = { app = each.key }
    }

    template {
      metadata {
        labels = { app = each.key }
      }

      spec {
        container {
          name              = each.key
          image             = each.value.image
          image_pull_policy = "Always"

          port {
            container_port = each.value.container_port
          }

          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = "eks"
          }

          env {
            name  = "SPRING_DATASOURCE_URL"
            value = "jdbc:postgresql://postgres:5432/${each.value.db_name}"
          }

          env {
            name = "SPRING_DATASOURCE_USERNAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_credentials.metadata[0].name
                key  = "db_username"
              }
            }
          }

          env {
            name = "SPRING_DATASOURCE_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_credentials.metadata[0].name
                key  = "db_password"
              }
            }
          }

          resources {
            requests = {
              cpu    = "200m"
              memory = "256Mi"
            }
            limits = {
              cpu    = "500m"
              memory = "512Mi"
            }
          }
        }
      }
    }
  }

  depends_on = [
    kubernetes_stateful_set.postgres,
    kubernetes_stateful_set.rabbitmq,
  ]
}

resource "kubernetes_service" "microservice" {
  for_each = var.microservices

  metadata {
    name      = each.key
    namespace = kubernetes_namespace.app.metadata[0].name
  }

  spec {
    selector = { app = each.key }

    port {
      port        = 80
      target_port = each.value.container_port
    }

    type = each.value.service_type
  }
}
