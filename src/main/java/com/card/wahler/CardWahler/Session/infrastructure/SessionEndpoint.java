package com.card.wahler.CardWahler.Session.infrastructure;

import com.card.wahler.CardWahler.Session.domain.EmailFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/session")
public class SessionEndpoint {

    @Autowired
    EmailFacade emailFacade;


    @PostMapping("/email")
    public ResponseEntity<String> startNewSession() {
        var elo = emailFacade.generateMail();
        return ResponseEntity.ok(elo);

    }



}
