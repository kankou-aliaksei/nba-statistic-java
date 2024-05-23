package pro.sunspace.nba.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

@TestConfiguration
public class TestConfig {

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate() {
        return Mockito.mock(R2dbcEntityTemplate.class);
    }
}

