variable "project_name" {
  description = "Project name"
  type        = string
}

variable "environment" {
  description = "Deployment environment"
  type        = string
}

variable "db_username" {
  description = "PostgreSQL database username"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "PostgreSQL database password"
  type        = string
  sensitive   = true
}

variable "microservices" {
  description = "Map of microservices to deploy with their container port and image"
  type = map(object({
    image          = string
    container_port = number
    service_type   = string
    db_name        = string
  }))
}
