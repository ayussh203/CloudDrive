variable "project_name"    { type = string }
variable "db_name"         { type = string }
variable "username"        { type = string }
variable "password"        { type = string } # for demo; move to Secrets Manager later
variable "vpc_id"          { type = string }
variable "private_subnets" { type = list(string) }
variable "allowed_cidr" { type = string }