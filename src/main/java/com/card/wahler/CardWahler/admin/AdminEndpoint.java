package com.card.wahler.CardWahler.admin;

import com.card.wahler.CardWahler.Pokerman.PokermanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/test")
public class AdminEndpoint {

    @Autowired
    PokermanService service;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok("Testing admin");
    }


}
