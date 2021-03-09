package com.voting.poker.VotingPoker.Pokerman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/pokerman")
public class PokermanEndpoint {

    @Autowired
    PokermanService service;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public Pokerman test(@RequestBody Pokerman pokerman) {
        return service.save(pokerman);
    }



}
