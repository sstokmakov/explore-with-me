package ru.tokmakov.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.tokmakov.stat.StatClient;

@Configuration
public class BeanConfiguration {
    @Bean
    public StatClient statClient() {
        return new StatClient(new RestTemplate());
    }
}
