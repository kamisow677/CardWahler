package com.card.wahler.CardWahler.Pokerman.api;

import com.card.wahler.CardWahler.Pokerman.Pokerman;
import com.card.wahler.CardWahler.Pokerman.PokermanDto;
import com.card.wahler.CardWahler.Pokerman.PokermanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/api/pokerman")
public class PokermanEndpoint {

    @Autowired
    PokermanService service;

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAll(Authentication authentication) {
        return ResponseEntity.ok(service.findAll());
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

    @GetMapping()
    public ResponseEntity<Object> getPokerman(@RequestParam Optional<String> nick, @RequestParam Optional<String> id,
            Authentication authentication) {
        PokermanDto pokerman;
        if (id.isPresent())
            pokerman = service.getPokermanDtoById(id.get());
        else if (nick.isPresent())
            pokerman = service.getPokermanDtoByNick(nick.get());
        else
            pokerman = service.getPokermanDtoByNick(authentication.getName());
        return ResponseEntity.ok(pokerman);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Pokerman> createPokerman(@RequestBody Pokerman pokerman, Authentication authentication) {
        return  ResponseEntity.ok(service.save(pokerman));
    }

}
