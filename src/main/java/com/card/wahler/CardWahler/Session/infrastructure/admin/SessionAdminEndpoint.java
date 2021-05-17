package com.card.wahler.CardWahler.Session.infrastructure.admin;

import com.card.wahler.CardWahler.Session.domain.EmailFacade;
import com.card.wahler.CardWahler.Session.domain.Session;
import com.card.wahler.CardWahler.Session.domain.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/session")
public class SessionAdminEndpoint {

    private final EmailFacade emailFacade;
    private final SessionService sessionService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Session> startSession(@Valid @RequestBody Session session, Authentication authentication) {
        sessionService.save(session);
        return ResponseEntity.ok(session);
    }

}
