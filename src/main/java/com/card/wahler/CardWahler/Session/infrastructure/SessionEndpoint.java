package com.card.wahler.CardWahler.Session.infrastructure;

import com.card.wahler.CardWahler.Session.domain.EmailFacade;
import com.card.wahler.CardWahler.Session.domain.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/session")
public class SessionEndpoint {

    private final EmailFacade emailFacade;
    private final SessionService sessionService;


    @PostMapping("/email")
    public ResponseEntity<String> endSession() {
        var elo = emailFacade.generateMail();
        return ResponseEntity.ok(elo);

    }

    @PostMapping("/start")
    public ResponseEntity<String> startSession() {
        var elo = emailFacade.generateMail();
        return ResponseEntity.ok(elo);

    }




}
