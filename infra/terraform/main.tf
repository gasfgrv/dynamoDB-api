resource "aws_dynamodb_table" "employee_table" {
  name         = "employee"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "employee_id"
  range_key    = "employee_email"

  attribute {
    name = "employee_id"
    type = "S"
  }

  attribute {
    name = "employee_email"
    type = "S"
  }
}
