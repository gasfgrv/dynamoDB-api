resource "aws_vpc" "api_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    "Name" = "API VPC"
  }
}

resource "aws_subnet" "api_subnet" {
  vpc_id                  = aws_vpc.api_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true

  tags = {
    "Name" = "api_vpc"
  }

}

resource "aws_internet_gateway" "api_internet_gateway" {
  vpc_id = aws_vpc.api_vpc.id

  tags = {
    "Name" = "api_gateway"
  }
}

resource "aws_route_table" "api_route_table" {
  vpc_id = aws_vpc.api_vpc.id

  tags = {
    "Name" = "api_route_table"
  }
}

resource "aws_route" "api_route" {
  route_table_id         = aws_route_table.api_route_table.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.api_internet_gateway.id
}

resource "aws_route_table_association" "api_route_table_association" {
  route_table_id = aws_route_table.api_route_table.id
  subnet_id      = aws_subnet.api_subnet.id
}


resource "aws_instance" "api_instance" {
  instance_type          = "t2.micro"
  ami                    = data.aws_ami.contactbook_server_ami.id
  key_name               = aws_key_pair.api_key_pair.id
  vpc_security_group_ids = [aws_security_group.api_security_group.id]
  subnet_id              = aws_subnet.api_subnet.id
  user_data              = file("userdata.tpl")

  root_block_device {
    volume_size = 8
  }

  tags = {
    "Name" = "contactbook_ec2_inst"
  }
}