variable "region" { default = "ap-south-1" }
variable "project_name" { default = "clouddrive" }
variable "owner" { default = "ayush" }
variable "my_ip_cidr" {
  description = "Your public IP with /32, e.g. 203.0.113.10/32"
  type        = string
  default     = "157.50.165.122/32"
}