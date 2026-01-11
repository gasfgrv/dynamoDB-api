package com.gasfgrv.dynamodbapi.domain.exceptions

import com.gasfgrv.dynamodbapi.domain.models.enums.EmployeeErrorStatus

class EmployeeException(status: EmployeeErrorStatus) : RuntimeException(status.getMessage())