package com.card.wahler.CardWahler.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<AnswerDto> get(@RequestParam Integer roundId, Authentication authentication) {
        return ResponseEntity.ok(service.find(roundId, authentication.getPrincipal().toString()));
    }

    @PutMapping("/{points}")
    public ResponseEntity<Object> editPoints(@PathVariable int points, @RequestParam String task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        service.editPoints(points, authentication.getPrincipal().toString(), task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{points}")
    public ResponseEntity<Object> addAnswer(@PathVariable int points, @RequestParam String task, Authentication authentication) {
        service.addAnswer(authentication.getPrincipal().toString(),  points, task);
        return ResponseEntity.ok().build();
    }

}
