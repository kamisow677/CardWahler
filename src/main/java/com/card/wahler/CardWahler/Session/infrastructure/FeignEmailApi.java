package com.card.wahler.CardWahler.Session.infrastructure;

import com.card.wahler.CardWahler.Session.domain.EmailApi;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@FeignClient(name = "email", url = "${feign.client.url}",
        fallback = FeignEmailApi.FeignEmailApiFallback.class,
        configuration = FeignEmailApi.FeignConfig.class)
public interface FeignEmailApi extends EmailApi {

    @GetMapping("/api/email")
    ResponseEntity<String> sendEmail();

    class FeignEmailApiFallback implements FeignEmailApi {
        @Override
        public ResponseEntity<String> sendEmail() {
            return ResponseEntity.ok("fallback triggered");
        }
    }

    class FeignClientInterceptor implements RequestInterceptor {

        private static final String AUTHORIZATION_HEADER="Authorization";
        private static final String TOKEN_TYPE = "Bearer";

        public static String getBearerTokenHeader() {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        }

        @Override
        public void apply(RequestTemplate requestTemplate) {
            requestTemplate.header(AUTHORIZATION_HEADER, getBearerTokenHeader());
        }

    }

    class FeignConfig {

        @Bean
        public RequestInterceptor requestInterceptor() {
            return new FeignClientInterceptor();
        }

        @Bean
        public ErrorDecoder errorDecoder() {
            return new CustomErrorDecoder();
        }

    }

    class CustomErrorDecoder implements ErrorDecoder {

        Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public Exception decode(String methodKey, Response response) {

            switch (response.status()){
                case 400:
                case 403:
                case 404: {
                    logger.error("Error took place when using Feign client to send HTTP Request. Status code " + response.status() + ", methodKey = " + methodKey);
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Email Service is not responding");
                }
                default:
                    return new Exception("Generic error");
            }
        }
    }

}
