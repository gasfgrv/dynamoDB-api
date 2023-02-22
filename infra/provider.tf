terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }
}

provider "aws" {
  region              = "us-east-1"
  shared_config_files = ["~/.aws/credentials"]
  profile             = "default"
}