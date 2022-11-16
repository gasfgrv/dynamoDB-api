package br.com.gusta.dynamodb.domain.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class Departament {

    @DynamoDBAttribute
    private String departamentName;

    @DynamoDBAttribute
    private String departamentCode;

}
