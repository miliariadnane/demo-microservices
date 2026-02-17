output "namespace" {
  description = "Kubernetes namespace where workloads are deployed"
  value       = kubernetes_namespace.app.metadata[0].name
}

output "microservice_endpoints" {
  description = "Service names and ports for each microservice"
  value = {
    for name, svc in kubernetes_service.microservice :
    name => {
      type       = svc.spec[0].type
      cluster_ip = svc.spec[0].cluster_ip
    }
  }
}
