package com.card.wahler.CardWahler.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.TestPropertySourceUtils
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@Testcontainers
//@ContextConfiguration(initializers = BaseIntegration.DockerPostgreDataSourceInitializer.class)
class BaseIntegration extends Specification {

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    protected MockMvc mvc

    // will be started before and stopped after each test method
//    @Container
//    protected PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
//            .withDatabaseName("foo")
//            .withUsername("foo")
//            .withPassword("secret");


//    public static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>("postgres:13.0");
//
//    static {
//        postgreDBContainer.start();
//    }
//
//     static class DockerPostgreDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//
//        @Override
//        void initialize(ConfigurableApplicationContext applicationContext) {
//
//            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
//                    applicationContext,
//                    "spring.datasource.url=" + postgreDBContainer.getJdbcUrl(),
//                    "spring.datasource.username=" + postgreDBContainer.getUsername(),
//                    "spring.datasource.password=" + postgreDBContainer.getPassword()
//            );
//        }
//    }

}
