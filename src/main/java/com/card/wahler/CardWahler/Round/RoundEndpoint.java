package com.card.wahler.CardWahler.Round;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/api/round")
public class RoundEndpoint {

    @Autowired
    RoundService service;

    @GetMapping("/{sessionId}")
    public ResponseEntity<RoundDto> get(@PathVariable Integer sessionId) {
        return ResponseEntity.ok(service.getCurrentRound(sessionId));
    }

}
