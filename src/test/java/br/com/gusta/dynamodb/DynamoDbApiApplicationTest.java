package br.com.gusta.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import springfox.documentation.spring.web.plugins.Docket;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DynamoDbApiApplicationTest {

    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context.containsBean("modelMapper")).isTrue();

        assertThat(context.getBean(ModelMapper.class)).isNotNull();

        assertThat(context.containsBean("messageSource")).isTrue();

        assertThat(context.getBean(MessageSource.class)).isNotNull();

        assertThat(context.containsBean("dynamoDbApi")).isTrue();

        assertThat(context.getBean(Docket.class)).isNotNull();

        assertThat(context.containsBean("dynamoDBMapper")).isTrue();

        assertThat(context.getBean(DynamoDBMapper.class)).isNotNull();

        Arrays.stream(context.getBeanDefinitionNames()).forEach(LOGGER::info);
    }

}