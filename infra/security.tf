resource "aws_security_group" "dynamo_api_security_group" {
  name        = "dynamo_api_security_group"
  description = "dynamo api security group"
  vpc_id      = aws_vpc.dynamo_api_vpc.id
}

resource "aws_security_group_rule" "dynamo_api_security_group_public" {
  from_port         = 0
  protocol          = "-1"
  security_group_id = aws_security_group.dynamo_api_security_group.id
  to_port           = 0
  type              = "egress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "dynamo_api_security_group_ssh" {
  from_port         = 22
  protocol          = "tcp"
  security_group_id = aws_security_group.dynamo_api_security_group.id
  to_port           = 22
  type              = "ingress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_key_pair" "dynamo_api_key" {
  key_name   = "dynamo_api_key"
  public_key = file("~/.ssh/api-dynamo-key.pub")
}

resource "aws_iam_policy" "api_dynamo_policy" {
  name        = "api_dynamo_policy"
  description = "Provides permission to access Dynamo"
  policy      = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Sid" : "ListAndDescribe",
        "Effect" : "Allow",
        "Action" : [
          "dynamodb:List*",
          "dynamodb:DescribeReservedCapacity*",
          "dynamodb:DescribeLimits",
          "dynamodb:DescribeTimeToLive"
        ],
        "Resource" : "*"
      },
      {
        "Sid" : "SpecificTable",
        "Effect" : "Allow",
        "Action" : [
          "dynamodb:BatchGet*",
          "dynamodb:DescribeStream",
          "dynamodb:DescribeTable",
          "dynamodb:Get*",
          "dynamodb:Query",
          "dynamodb:Scan",
          "dynamodb:BatchWrite*",
          "dynamodb:CreateTable",
          "dynamodb:Delete*",
          "dynamodb:Update*",
          "dynamodb:PutItem"
        ],
        "Resource" : "arn:aws:dynamodb:*:*:table/employee"
      }
    ]
  })
}

resource "aws_iam_role" "api_dynamo_role" {
  name               = "api_dynamo_role"
  assume_role_policy = jsonencode({
    Version   = "2012-10-17"
    Statement = [
      {
        Action    = "sts:AssumeRole"
        Effect    = "Allow"
        Sid       = "RoleForEC2"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_policy_attachment" "api_dynamo_attach" {
  name       = "api_dynamo_attach"
  roles      = [aws_iam_role.api_dynamo_role.name]
  policy_arn = aws_iam_policy.api_dynamo_policy.arn
}

resource "aws_iam_instance_profile" "api_dynamo_instance_profile" {
  name = "api_dynamo_instance_profile"
  role = aws_iam_role.api_dynamo_role.name
}

resource "aws_instance" "api_dynamo_ec2" {
  instance_type          = "t2.micro"
  key_name               = aws_key_pair.dynamo_api_key.id
  vpc_security_group_ids = [aws_security_group.dynamo_api_security_group.id]
  subnet_id              = aws_subnet.dynamo_api_subnet.id
  iam_instance_profile   = aws_iam_instance_profile.api_dynamo_instance_profile.name
  ami                    = data.aws_ami.api_ami.id
  user_data = file("userdata.tpl")

  root_block_device {
    volume_size = 8
  }

  tags = {
    "Name" = "api_dynamo_ec2"
  }
}