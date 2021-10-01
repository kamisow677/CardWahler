package com.card.wahler.CardWahler.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/api/answer")
public class AnswerEndpoint {

    @Autowired
    AnswerSerive service;

    @GetMapping("/all")
    public ResponseEntity<Iterable<AnswerDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping
    public ResponseEntity<AnswerDto> get(@RequestParam int roundId, Authentication authentication) {
        return ResponseEntity.ok(service.find(roundId, authentication.getName()));
    }

    @GetMapping("/current/round")
    public ResponseEntity<AnswerDto> getAnswerInCurrentRound(@RequestParam int sessionId, Authentication authentication) {
        return ResponseEntity.ok(service.findAnswerCurrentRoundInSession(sessionId, authentication.getName()));
    }

    @PutMapping("/{points}")
    public ResponseEntity<Object> editPoints(@PathVariable int points, @RequestParam String task, Authentication authentication) {
        service.editPoints(points, authentication.getName(), task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{points}")
    public ResponseEntity<Object> addAnswer(@PathVariable int points, @RequestParam String task, Authentication authentication) {
        service.addAnswer(authentication.getName(), points, task);
        return ResponseEntity.ok().build();
    }

}
