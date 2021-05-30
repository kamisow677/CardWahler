package com.card.wahler.CardWahler.Answer;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/api/answer")
public class AnswerEndpoint {

    @Autowired
    AnswerSerive service;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{points}")
    public ResponseEntity<Object> editPoints(@PathVariable int points, @RequestParam String task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        service.editPoints(points, authentication.getPrincipal().toString(), task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{points}")
    public ResponseEntity<Object> addAnswer(@PathVariable int points, @RequestParam String task, Authentication authentication, Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        service.addAnswer(authentication.getPrincipal().toString(),  points, task);
        return ResponseEntity.ok().build();
    }

}
