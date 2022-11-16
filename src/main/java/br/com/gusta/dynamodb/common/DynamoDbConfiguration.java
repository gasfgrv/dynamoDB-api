package br.com.gusta.dynamodb.common;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfiguration {

    @Value("dynamo.serviceEndpoint")
    private String serviceEndpoint;

    @Value("dynamo.signingRegion")
    private String signingRegion;

    @Value("dynamo.accessKey")
    private String accessKey;

    @Value("dynamo.secretKey")
    private String secretKey;

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        var endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion);
        var credentials = new BasicAWSCredentials(accessKey, secretKey);
        var credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        var amazonDynamoDB = AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(credentialsProvider)
                .build();
        return new DynamoDBMapper(amazonDynamoDB);
    }

}
