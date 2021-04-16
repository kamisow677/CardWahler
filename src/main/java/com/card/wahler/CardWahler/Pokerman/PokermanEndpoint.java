package com.card.wahler.CardWahler.Pokerman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/image/{nick}")
    public ResponseEntity<Object> addImage(@RequestParam MultipartFile image, @PathVariable String nick) {
        service.saveImage(image, nick);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/image/{nick}")
    public ResponseEntity getImage(@PathVariable String nick) {
        byte[] image = service.getImage(nick);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/{nick}")
    public ResponseEntity<Object> getPokerman(@RequestParam String nick) {
        Pokerman pokerman = service.getPokerman(nick);
        return ResponseEntity.ok(pokerman);
    }

}
