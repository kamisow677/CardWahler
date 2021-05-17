package com.card.wahler.CardWahler.Session.infrastructure.user;

import com.card.wahler.CardWahler.Session.domain.EmailFacade;
import com.card.wahler.CardWahler.Session.domain.SessionDto;
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

    @GetMapping("/all")
    public ResponseEntity<Iterable<SessionDto>> getAll() {
        return ResponseEntity.ok(sessionService.getAll());
    }

    @GetMapping()
    public ResponseEntity<Object> getMySession(Authentication authentication) {
        return ResponseEntity.ok(sessionService.get(authentication.getPrincipal().toString()));
    }

    @PostMapping(value = "/join", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> joinSession(@RequestParam String password, Authentication authentication) {
        sessionService.joinSession(password, authentication.getPrincipal().toString());
        return ResponseEntity.noContent().build();
    }

}
