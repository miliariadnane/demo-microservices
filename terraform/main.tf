# =============================================================================
# Demo Microservices — Complete AWS Infrastructure + Workloads
# =============================================================================
#
# What gets created:
#   1. Networking  → VPC, subnets, gateways, route tables
#   2. EKS         → Cluster, node group, IAM roles, security groups
#   3. Workloads   → PostgreSQL, RabbitMQ, Zipkin, and all microservices
# =============================================================================

locals {
  common_tags = {
    Project     = var.project_name
    Environment = var.environment
    ManagedBy   = "terraform"
  }
}

# ─── Networking ─────────────────────────────────────────────────────────────
# Creates: VPC, public/private subnets, Internet Gateway, NAT Gateway,
#          route tables, and subnet associations.

module "networking" {
  source = "./modules/networking"

  project_name         = var.project_name
  environment          = var.environment
  vpc_cidr             = var.vpc_cidr
  availability_zones   = var.availability_zones
  public_subnet_cidrs  = var.public_subnet_cidrs
  private_subnet_cidrs = var.private_subnet_cidrs
  tags                 = local.common_tags
}

# ─── EKS Cluster ────────────────────────────────────────────────────────────
# Creates: EKS cluster, managed node group, IAM roles for cluster and nodes,
#          security groups, and OIDC provider for IRSA.

module "eks" {
  source = "./modules/eks"

  project_name        = var.project_name
  environment         = var.environment
  cluster_version     = var.cluster_version
  vpc_id              = module.networking.vpc_id
  public_subnet_ids   = module.networking.public_subnet_ids
  private_subnet_ids  = module.networking.private_subnet_ids
  node_instance_types = var.node_instance_types
  node_desired_size   = var.node_desired_size
  node_min_size       = var.node_min_size
  node_max_size       = var.node_max_size
  node_disk_size      = var.node_disk_size
  tags                = local.common_tags
}

# ─── Kubernetes Workloads ───────────────────────────────────────────────────
# Creates: PostgreSQL, RabbitMQ, Zipkin, and all microservices.
# This is why `terraform destroy` cleans up EVERYTHING — no manual kubectl needed.

module "workloads" {
  source = "./modules/workloads"

  project_name = var.project_name
  environment  = var.environment
  db_username  = var.db_username
  db_password  = var.db_password

  microservices = {
    customer = {
      image          = "miliariadnane/customer:12.09.2022.17.36.48"
      container_port = 8001
      service_type   = "ClusterIP"
      db_name        = "customer"
    }
    product = {
      image          = "miliariadnane/product:12.09.2022.17.36.48"
      container_port = 8002
      service_type   = "ClusterIP"
      db_name        = "product"
    }
    order = {
      image          = "miliariadnane/order:12.09.2022.17.36.48"
      container_port = 8003
      service_type   = "ClusterIP"
      db_name        = "order"
    }
    notification = {
      image          = "miliariadnane/notification:12.09.2022.17.36.48"
      container_port = 8004
      service_type   = "ClusterIP"
      db_name        = "notification"
    }
    payment = {
      image          = "miliariadnane/payment:12.09.2022.17.36.48"
      container_port = 8005
      service_type   = "ClusterIP"
      db_name        = "payment"
    }
  }

  depends_on = [module.eks]
}
