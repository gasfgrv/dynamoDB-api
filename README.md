## Visão Geral da Aplicação

A aplicação é um serviço de gerenciamento de funcionários (Employees) que utiliza o Amazon DynamoDB como camada de
persistência. O design foca na separação de responsabilidades, garantindo que a lógica de negócio (domínio) seja
independente de tecnologias externas (infraestrutura).

**Tecnologias Principais:**

* **Linguagem:** Kotlin
* **Framework:** Spring Boot
* **Banco de Dados:** AWS DynamoDB (via AWS SDK v2 Enhanced Client)
* **Arquitetura:** Hexagonal (Clean Architecture)

## Diagrama de Estados

Este diagrama ilustra a representação do estado do registro de um funcionário no sistema.

```mermaid
stateDiagram-v2
  [*] --> Inexistente

  Inexistente --> Ativo: save(Employee)

  state Ativo {
    [*] --> Persistido
    Persistido --> Persistido: update()
    Persistido --> Recuperado: getEmployeeById()
    Recuperado --> Persistido
  }

  Ativo --> Inexistente: delete()

  Ativo --> Falha: Erro de Validação
  Inexistente --> Falha: getEmployeeById() falha

  state Falha {
    NOT_FOUND: EmployeeErrorStatus.NOT_FOUND
    CANT_BE_DELETED: EmployeeErrorStatus.CANT_BE_DELETED
    CANT_BE_UPDATED: EmployeeErrorStatus.CANT_BE_UPDATED
  }

  Falha --> Ativo: Nova tentativa de operação
  Falha --> Inexistente: Registro não encontrado
```

## Diagrama de Classes

O diagrama abaixo detalha as relações entre as principais classes e interfaces do domínio e infraestrutura.

```mermaid
classDiagram
    class EmployeeServicePort {
        <<interface>>
        +save(employee: Employee) Employee
        +getEmployeeById(id: String) Employee
        +delete(id: String)
        +update(id: String, employee: Employee) Employee
    }

    class EmployeeRepositoryPort {
        <<interface>>
        +save(employee: Employee) Employee
        +getEmployeeById(id: String) Optional~Employee~
        +deleteEmployee(employee: Employee)
        +updateEmployee(employee: Employee) Employee
    }

    class EmployeeUsecase {
        -repository: EmployeeRepositoryPort
        +save(employee: Employee) Employee
        +getEmployeeById(id: String) Employee
        +delete(id: String)
        +update(id: String, employee: Employee) Employee
        -checkIfPresent(opt, id, status)
    }

    class EmployeeRepositoryAdapter {
        -table: DynamoDbTable~EmployeeEntity~
        -mapper: EmployeeMapper
        +save(employee: Employee) Employee
        +getEmployeeById(id: String) Optional~Employee~
        +deleteEmployee(employee: Employee)
        +updateEmployee(employee: Employee) Employee
    }

    class Employee {
        -id: String
        -firstName: String
        -lastName: String
        -email: String
        -department: Department
        +employeeId() String
    }

    class Department {
        -name: String
        -code: String
    }

    EmployeeServicePort <|.. EmployeeUsecase
    EmployeeUsecase --> EmployeeRepositoryPort
    EmployeeRepositoryPort <|.. EmployeeRepositoryAdapter
    EmployeeUsecase ..> Employee
    Employee *-- Department
```

## Diagrama de Sequência (Fluxo de Atualização)

Este diagrama representa o fluxo lógico quando o método update é chamado no EmployeeUsecase, demonstrando a interação
entre o domínio e o adaptador de infraestrutura.

```mermaid
sequenceDiagram
    participant App as Client/Controller
    participant UC as EmployeeUsecase
    participant Port as EmployeeRepositoryPort
    participant Adap as EmployeeRepositoryAdapter
    participant DB as DynamoDB
    App ->> UC: update(employeeId, employeeData)
    UC ->> Port: getEmployeeById(employeeId)
    Port ->> Adap: getEmployeeById(id)
    Adap ->> DB: query (KeyEqualTo)
    DB -->> Adap: EmployeeEntity
    Adap -->> Port: Optional<Employee>
    Port -->> UC: Optional<Employee>

    alt Employee não existe
        UC ->> UC: throw EmployeeException(CANT_BE_UPDATED)
    else Employee existe
        UC ->> UC: Criar objeto updatedEmployee (mantendo ID original)
        UC ->> Port: updateEmployee(updatedEmployee)
        Port ->> Adap: updateEmployee(employee)
        Adap ->> DB: updateItem (UpdateItemEnhancedRequest)
        DB -->> Adap: EmployeeEntity (updated)
        Adap -->> Port: Employee
        Port -->> UC: Employee
        UC -->> App: Employee (updated)
    end
```

## Detalhamento das Camadas

### Domínio (`domain`)

* **Models:** `Employee` e `Department` são POJOs (Plain Old Java Objects) que representam a regra de negócio. O uso de
  métodos como `employeeId()` em vez de getters padrão sugere uma abordagem de encapsulamento mais rigorosa.
* **Ports:**
    * `EmployeeServicePort`: Define o que a aplicação pode fazer (Input).
    * `EmployeeRepositoryPort`: Define o que a aplicação precisa do mundo externo para persistência (Output).
* **Exceptions:** `EmployeeException` centraliza o tratamento de erros de negócio usando o enum `EmployeeErrorStatus`.

### Aplicação (`application`)

* Usecases: `EmployeeUsecase` contém a orquestração da lógica. Por exemplo, antes de deletar ou atualizar, ele verifica
  a existência do registro e lança exceções customizadas se necessário.

### Infraestrutura (`infrastructure`)

* Adapters: `EmployeeRepositoryAdapter` implementa a porta de saída. Ele converte objetos de domínio em entidades (
  `EmployeeEntity`) e utiliza o `DynamoDbEnhancedClient` para operações no banco.
* Config: `DynamoDbConfiguration` centraliza a criação dos Beans do SDK da AWS, configurando o endpoint e a região.