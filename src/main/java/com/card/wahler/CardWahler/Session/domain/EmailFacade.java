package com.card.wahler.CardWahler.Session.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailFacade {

    private final EmailApi emailApi;

    public String generateMail() {
        return emailApi.sendEmail().getBody();
    }
}
