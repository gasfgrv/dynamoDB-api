package br.com.gusta.dynamodb.common;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Slf4j
@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource() {
        LOGGER.info("Creating bean MessageSource");

        var defaultEncoding = System.getProperty("file.encoding");

        var messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setDefaultEncoding(defaultEncoding);

        messageSource.setDefaultLocale(Locale.getDefault());

        return messageSource;
    }

}
