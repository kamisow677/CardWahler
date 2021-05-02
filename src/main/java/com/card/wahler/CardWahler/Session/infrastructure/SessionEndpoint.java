package com.card.wahler.CardWahler.Session.infrastructure;

import com.card.wahler.CardWahler.Session.domain.EmailFacade;
import com.card.wahler.CardWahler.Session.domain.Session;
import com.card.wahler.CardWahler.Session.domain.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/session")
public class SessionEndpoint {

    private final EmailFacade emailFacade;
    private final SessionService sessionService;


    @PostMapping("/email")
    public ResponseEntity<String> generateMail() {
        var elo = emailFacade.generateMail();
        return ResponseEntity.ok(elo);

    }

    @PostMapping(value = "/start", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Session> startSession(@RequestBody Session session, Authentication authentication) {
        session.setLink("generated");
        var elo = sessionService.save(session);
        return ResponseEntity.ok(session);

    }



}
