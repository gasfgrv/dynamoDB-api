# Localstack
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "6.28.0"
    }
  }
}

provider "aws" {
  region                      = "us-east-1"
  access_key                  = "test"
  secret_key                  = "test"
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true

  endpoints {
    dynamodb = "http://localhost:4566"
  }

  default_tags {
    tags = {
      Project   = "DynamoDB-API"
      ManagedBy = "Terraform"
    }
  }
}

# AWS Console
# terraform {
#   required_providers {
#     aws = {
#       source  = "hashicorp/aws"
#       version = "6.28.0"
#     }
#   }
#
#   backend "s3" {
#     bucket = "gasfgrv-terraform-remote-state"
#     key    = "employee-app/terraform.tfstate"
#     region = "us-east-1"
#   }
# }
#
# provider "aws" {
#   shared_config_files      = ["~/.aws/config"]
#   shared_credentials_files = ["~/.aws/credentials"]
#   profile                  = "default"
#   region                   = "us-east-1"
#
#   default_tags {
#     tags = {
#       Project   = "DynamoDB-API"
#       ManagedBy = "Terraform"
#     }
#   }
# }
