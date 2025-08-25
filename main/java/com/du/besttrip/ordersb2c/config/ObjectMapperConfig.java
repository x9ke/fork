package com.du.besttrip.ordersb2c.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ObjectMapperConfig {
    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new JavaTimeModule())  // Поддержка Java 8 Date/Time API
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)  // Даты в ISO-8601 формате
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)  // Игнорировать неизвестные свойства
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)  // Не падать при сериализации пустых объектов
                .enable(SerializationFeature.INDENT_OUTPUT);  // Красивый вывод JSON с отступами
    }
}
