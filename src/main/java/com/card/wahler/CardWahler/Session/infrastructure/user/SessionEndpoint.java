package com.card.wahler.CardWahler.Session.infrastructure.user;

import com.card.wahler.CardWahler.Session.domain.EmailFacade;
import com.card.wahler.CardWahler.Session.domain.SessionDto;
import com.card.wahler.CardWahler.Session.domain.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
    public ResponseEntity<Set<SessionDto>> getAll() {
        return ResponseEntity.ok(sessionService.getAll());
    }

    @GetMapping
    public ResponseEntity<SessionDto> get(@RequestParam Long sessionId, Authentication authentication) {
        return ResponseEntity.ok(sessionService.fingKeycloakUserIdById(authentication.getPrincipal().toString(), sessionId));
    }

    @PostMapping(value = "/join", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SessionDto> joinSession(@RequestParam String password, Authentication authentication) {
        return ResponseEntity.ok(sessionService.joinSession(password, authentication.getPrincipal().toString()));
    }

    @PutMapping(value = "/leave", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> leaveSession(@RequestParam Integer sessionId, Authentication authentication) {
        sessionService.leaveSession(sessionId, authentication.getPrincipal().toString());
        return ResponseEntity.noContent().build();
    }


}
