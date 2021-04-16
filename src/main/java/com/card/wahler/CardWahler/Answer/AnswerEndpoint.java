package com.card.wahler.CardWahler.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> editPoints(@PathVariable int points, @RequestParam String nick, @RequestParam String task) {
        service.editPoints(points, nick, task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{points}")
    public ResponseEntity<Object> addAnswer(@PathVariable int points, @RequestParam String nick, @RequestParam String task) {
        service.addAnswer(nick,  points, task);
        return ResponseEntity.ok().build();
    }

}
