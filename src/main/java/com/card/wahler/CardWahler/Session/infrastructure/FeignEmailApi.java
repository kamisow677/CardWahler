package com.card.wahler.CardWahler.Session.infrastructure;

import com.card.wahler.CardWahler.Session.domain.EmailApi;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.server.ResponseStatusException;

@FeignClient(value = "jplaceholder", url = "http://localhost:8081/api/email",
        fallback = FeignEmailApi.FeignEmailApiFallback.class,
        configuration = FeignEmailApi.FeignConfig.class)
public interface FeignEmailApi extends EmailApi {

    @GetMapping()
    ResponseEntity<String> sendEmail();

    class FeignEmailApiFallback implements FeignEmailApi {
        @Override
        public ResponseEntity<String> sendEmail() {
            return null;
        }
    }

    class FeignClientInterceptor implements RequestInterceptor {

        private static final String AUTHORIZATION_HEADER="Authorization";
        private static final String TOKEN_TYPE = "Bearer";

        @Autowired
        OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;


        @Override
        public void apply(RequestTemplate requestTemplate) {
//            AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken
//                ("key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getDetails() instanceof SimpleKeycloakAccount) {
                SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
                requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, details.getKeycloakSecurityContext().getTokenString()));
            } else {
                var request = OAuth2AuthorizeRequest
                        .withClientRegistrationId("internal-api") // <- Here you load your registered client
                        .principal(authentication)
                        .build();

                var ael = oAuth2AuthorizedClientManager.authorize(request);

                if (authentication != null) {
                    requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE,  ael.getAccessToken().getTokenValue()));
                }
            }


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

@Configuration
class AuthManager {
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientRepository);

        return authorizedClientManager;
    }
}