# dynamoDB-API <!-- omit from toc -->

API feita para testar a integração do Spring boot com o AWS DynamoDB.

## Tabela de Conteúdos <!-- omit from toc -->
- [Features](#features)
- [Contrato da API](#contrato-da-api)
- [Endpoints da Aplicação](#endpoints-da-aplicação)
  - [/employee](#employee)
    - [POST](#post)
      - [Parâmetros](#parâmetros)
      - [Respostas](#respostas)
  - [/employee/{id}](#employeeid)
    - [GET](#get)
      - [Parâmetros](#parâmetros-1)
      - [Respostas](#respostas-1)
    - [PUT](#put)
      - [Parâmetros](#parâmetros-2)
      - [Respostas](#respostas-2)
    - [DELETE](#delete)
      - [Parâmetros](#parâmetros-3)
      - [Respostas](#respostas-3)
  - [Modelos](#modelos)
    - [Departament](#departament)
    - [Employee](#employee-1)
    - [EmployeeDto](#employeedto)
    - [EmployeeInput](#employeeinput)
- [Pré-requisitos e como rodar a aplicação/testes](#pré-requisitos-e-como-rodar-a-aplicaçãotestes)
  - [Tecnologias utilizadas](#tecnologias-utilizadas)
  - [Dependencias](#dependencias)
  - [Licença](#licença)
  - [Autor](#autor)
 
## Features

- [x] Cadastro de dados de funcionários e seus respectivos departamentos

- [x] Consulta de dados de funcionários e seus respectivos departamentos

- [x] Atualização de dados de funcionários e seus respectivos departamentos

- [x] Exclusão de dados de funcionários e seus respectivos departamentos

## Contrato da API

```yaml
swagger: '2.0'
info:
  description: API using Spring Boot for dynamoDB testing
  version: 1.0.0
  title: DynamoDB API
  license:
    name: Apache License Version 2.0
    url: https://www.apache.org/licenses/LICENSE-2.0"
host: localhost:8080
basePath: /
tags:
  - name: employee-controller
    description: Employee Controller
paths:
  /employee:
    post:
      tags:
        - employee-controller
      summary: Saves a new employee
      description: Saves a new employee in dynamoDB
      operationId: saveEmployeeUsingPOST
      consumes:
        - application/json
      produces:
        - '*/*'
      parameters:
        - in: body
          name: employee
          description: Form for creation of user
          required: true
          schema:
            $ref: '#/definitions/EmployeeInput'
      responses:
        '200':
          description: OK
          schema:
            $ref: '#/definitions/Employee'
      deprecated: false
  /employee/{id}:
    get:
      tags:
        - employee-controller
      summary: Get data of specific employee
      description: Get data of specific employee in dynamoDB
      operationId: getEmployeeUsingGET
      produces:
        - '*/*'
      parameters:
        - name: id
          in: path
          description: Employee id
          required: true
          type: string
      responses:
        '200':
          description: OK
          schema:
            $ref: '#/definitions/EmployeeDto'
      deprecated: false
    put:
      tags:
        - employee-controller
      summary: Update data of specific employee
      description: Update data of specific employee in dynamoDB
      operationId: updateEmployeeUsingPUT
      consumes:
        - application/json
      produces:
        - '*/*'
      parameters:
        - in: body
          name: employee
          description: Form to update user
          required: true
          schema:
            $ref: '#/definitions/EmployeeInput'
        - name: id
          in: path
          description: Employee id
          required: true
          type: string
      responses:
        '200':
          description: OK
          schema:
            $ref: '#/definitions/Employee'
      deprecated: false
    delete:
      tags:
        - employee-controller
      summary: Deletes specific employee
      description: Deletes employee in dynamoDB
      operationId: deleteEmployeeUsingDELETE
      produces:
        - '*/*'
      parameters:
        - name: id
          in: path
          description: Employee id
          required: true
          type: string
      responses:
        '200':
          description: OK
      deprecated: false
definitions:
  Departament:
    type: object
    properties:
      departamentCode:
        type: string
      departamentName:
        type: string
    title: Departament
  Employee:
    type: object
    properties:
      departament:
        $ref: '#/definitions/Departament'
      email:
        type: string
      employeeId:
        type: string
      firstName:
        type: string
      lastName:
        type: string
    title: Employee
  EmployeeDto:
    type: object
    properties:
      departamentCode:
        type: string
      departamentName:
        type: string
      email:
        type: string
      firstName:
        type: string
      lastName:
        type: string
    title: EmployeeDto
  EmployeeInput:
    type: object
    properties:
      departament:
        $ref: '#/definitions/Departament'
      email:
        type: string
      firstName:
        type: string
      lastName:
        type: string
    title: EmployeeInput
```

## Endpoints da Aplicação

### /employee

#### POST

##### Parâmetros

| Nome     | Localizado em | Descrição                            | Obrigatório | Schema                          |
| -------- | ------------- | ------------------------------------ | ----------- | ------------------------------- |
| employee | body          | Formulário para criação do empregado | Sim         | [EmployeeInput](#employeeinput) |

##### Respostas

| Code | Descrição | Schema                |
| ---- | --------- | --------------------- |
| 200  | OK        | [Employee](#employee) |


### /employee/{id}

#### GET

##### Parâmetros

| Nome | Localizado em | Descrição       | Obrigatório | Schema |
| ---- | ------------- | --------------- | ----------- | ------ |
| id   | path          | Id do empregado | Sim         | string |

##### Respostas

| Code | Descrição | Schema                      |
| ---- | --------- | --------------------------- |
| 200  | OK        | [EmployeeDto](#employeedto) |


#### PUT

##### Parâmetros

| Nome     | Localizado em | Descrição                               | Obrigatório | Schema                          |
| -------- | ------------- | --------------------------------------- | ----------- | ------------------------------- |
| employee | body          | Formulário para aualização do empregado | Sim         | [EmployeeInput](#employeeinput) |
| id       | path          | Id do empregado                         | Sim         | string                          |

##### Respostas

| Code | Descrição | Schema                |
| ---- | --------- | --------------------- |
| 200  | OK        | [Employee](#employee) |


#### DELETE

##### Parâmetros

| Nome | Localizado em | Descrição       | Obrigatório | Schema |
| ---- | ------------- | --------------- | ----------- | ------ |
| id   | path          | Id do empregado | Sim         | string |

##### Respostas

| Code | Descrição  |
| ---- | ---------- |
| 204  | No Content |

### Modelos


#### Departament

| Nome            | Type   | Descrição | Obrigatório |
| --------------- | ------ | --------- | ----------- |
| departamentCode | string |           | No          |
| departamentNome | string |           | No          |

#### Employee

| Nome        | Type                        | Descrição | Obrigatório |
| ----------- | --------------------------- | --------- | ----------- |
| departament | [Departament](#departament) |           | No          |
| email       | string                      |           | No          |
| employeeId  | string                      |           | No          |
| firstNome   | string                      |           | No          |
| lastNome    | string                      |           | No          |

#### EmployeeDto

| Nome            | Type   | Descrição | Obrigatório |
| --------------- | ------ | --------- | ----------- |
| departamentCode | string |           | No          |
| departamentNome | string |           | No          |
| email           | string |           | No          |
| firstNome       | string |           | No          |
| lastNome        | string |           | No          |

#### EmployeeInput

| Nome        | Type                        | Descrição | Obrigatório |
| ----------- | --------------------------- | --------- | ----------- |
| departament | [Departament](#departament) |           | No          |
| email       | string                      |           | No          |
| firstNome   | string                      |           | No          |
| lastNome    | string                      |           | No          |


Coleção para testar os endpoints da aplicação

[![Run in Insomnia}](https://insomnia.rest/images/run.svg)](https://insomnia.rest/run/?label=dynamoDB-API&uri=https%3A%2F%2Fraw.githubusercontent.com%2Fgasfgrv%2FdynamoDB-api%2Fmaster%2FdynamoDB-API-collection.json)

## Pré-requisitos e como rodar a aplicação/testes

Pré-requisitos para rodar localmente:

* JDK 11 ou superior
* Maven 3
* Uma conta na AWS com uma tabela chamada empoyee no dynamoDB
  * Um usuário com as permissões (de preferencia a de full access ao bd)

Para rodar via docker, rode os comandos abaixo:

```bash
# Baixar a imagem
docker pull gustosilva/dynamodb-api:latest

# Gerar o containter
docker run \
  --env ACCESS_KEY=<ACCESS_KEY> \
  --env SECRET_KEY=<SECRET_KEY> \
  --env SERVICE_ENDPOINT=<SERVICE_ENDPOINT> \
  --env SIGNING_REGION=<SIGNING_REGION> \
  -p 8080:8080 gustosilva/dynamodb-api:latest
```

### Tecnologias utilizadas

Projeto feito usando **Java 11** e **Maven 3.8** como ferramenta de build.

### Dependencias

- spring validation
- spring web
- lombok
- spring test
- spring cache
- modelmapper
- aws-java-sdk-dynamodb
- springfox-swagger2
- springfox-swagger-ui

### Licença

[Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)

### Autor

<div>
    <img style="border-radius: 10%; float: left; margin-right: 20px" src="https://avatars.githubusercontent.com/u/34608751?v=4" width="100px;" alt=""/>
    <p>Feito por Gustavo Silva:</p>
    <a href="https://www.linkedin.com/in/gustavo-silva-69b84a15b/"><img src="https://img.shields.io/badge/linkedin-%230077B5.svg?&style=for-the-badge&logo=linkedin&logoColor=white" height=25></a>
    <a href="https://discordapp.com/users/616994765065420801"><img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white" height=25></a>
    <br>
    <a href="mailto:gustavoalmeidasilva41@gmail.com"><img src="https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white" height=25></a>
    <a href="mailto:gustavo_almeida11@hotmail.com"><img src="https://img.shields.io/badge/Microsoft_Outlook-0078D4?style=for-the-badge&logo=microsoft-outlook&logoColor=white" height=25></a>
</div>
