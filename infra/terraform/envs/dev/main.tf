provider "aws" {
  region = var.region
}

module "network" {
  source = "../../modules/network"
  name   = var.project_name
  region = var.region 
}

module "s3" {
  source      = "../../modules/s3"
  bucket_name = "${var.project_name}-dev-${var.owner}"
}

module "rds" {
  source          = "../../modules/rds"
  project_name    = var.project_name
  db_name         = "clouddrive"
  username        = "cloudadmin"               # change from "cloud"
  password = "CloudDriveP#ss123!"
  vpc_id          = module.network.vpc_id
  private_subnets = module.network.private_subnets
   allowed_cidr    = var.my_ip_cidr 
}

output "vpc_id" {
  value = module.network.vpc_id
}

output "rds_endpoint" {
  value = module.rds.endpoint
}

output "bucket_name" {
  value = module.s3.bucket_name
}
