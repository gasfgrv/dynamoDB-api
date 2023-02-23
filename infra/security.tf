resource "aws_security_group" "api_security_group" {
  name        = "api_sg"
  description = "api security group"
  vpc_id      = aws_vpc.api_vpc.id

  tags = {
    "Name" = "api_sg"
  }
}

resource "aws_security_group_rule" "api_security_group_rule_egress" {
  type        = "egress"
  from_port   = 0
  to_port     = 0
  protocol    = "-1"
  cidr_blocks = ["0.0.0.0/0"]

  security_group_id = aws_security_group.api_security_group.id
}

resource "aws_security_group_rule" "api_security_group_rule_ssh" {
  type              = "ingress"
  from_port         = 22
  to_port           = 22
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.api_security_group.id
}

resource "aws_key_pair" "api_key_pair" {
  key_name   = "api-dynamo-key"
  public_key = file("~/.ssh/api-dynamo-key.pub")
}

resource "aws_security_group_rule" "api_security_group_rule_ingress" {
  type              = "ingress"
  from_port         = 80
  to_port           = 80
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.api_security_group.id
}
