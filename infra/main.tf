resource "aws_vpc" "dynamo_api_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    "Name" = "dynamo_api_vpc"
  }
}

resource "aws_subnet" "dynamo_api_subnet" {
  vpc_id                  = aws_vpc.dynamo_api_vpc.id
  cidr_block              = "10.0.0.0/16"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true

  tags = {
    "Name" = "dynamo_api_subnet"
  }
}

resource "aws_internet_gateway" "dynamo_api_gateway" {
  vpc_id = aws_vpc.dynamo_api_vpc.id

  tags = {
    "Name" = "dynamo_api_gateway"
  }
}

resource "aws_route_table" "dynamo_api_route_table" {
  vpc_id = aws_vpc.dynamo_api_vpc.id

  tags = {
    "Name" = "dynamo_api_route_table"
  }
}

resource "aws_route" "dynamo_api_route" {
  route_table_id         = aws_route_table.dynamo_api_route_table.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.dynamo_api_gateway.id
}

resource "aws_route_table_association" "dynamo_api_route_table_association" {
  route_table_id = aws_route_table.dynamo_api_route_table.id
  subnet_id      = aws_subnet.dynamo_api_subnet.id
}