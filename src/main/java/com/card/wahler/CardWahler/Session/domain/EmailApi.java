package com.card.wahler.CardWahler.Session.domain;

import org.springframework.http.ResponseEntity;

public interface EmailApi {
    ResponseEntity<String> sendEmail();
}
