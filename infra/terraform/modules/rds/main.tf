resource "aws_security_group" "rds" {
  name        = "${var.project_name}-rds-sg"
  description = "Allow Postgres from my IP"
  vpc_id      = var.vpc_id

  ingress {
    description = "Postgres"
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = [var.allowed_cidr]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = { Name = "${var.project_name}-rds-sg" }
}

resource "aws_db_subnet_group" "this" {
  name       = "${var.project_name}-subnet-group"
  subnet_ids = var.private_subnets
  tags       = { Name = "${var.project_name}-db-subnet-group" }
}

resource "aws_db_instance" "this" {
  identifier           = "${var.project_name}-db"
  engine               = "postgres"
  instance_class       = "db.t3.micro"
  allocated_storage    = 20
  username             = var.username
  password             = var.password
  db_name              = var.db_name
  skip_final_snapshot  = true
  publicly_accessible  = true                         # <--- temporary to let laptop connect
  db_subnet_group_name = aws_db_subnet_group.this.name
  vpc_security_group_ids = [aws_security_group.rds.id]# <--- use our SG
  tags = { Name = "${var.project_name}-db" }
}

output "endpoint" { value = aws_db_instance.this.address }
