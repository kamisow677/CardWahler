package com.card.wahler.CardWahler.Pokerman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PokermanService {

    @Autowired
    PokermanRepository repository;

    public Pokerman save(Pokerman pokerman) {
        return repository.save(pokerman);
    }

    public Iterable<Pokerman> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void saveImage(MultipartFile multipartFile, String nick) {
        byte[] buffer = new byte[1024];
        var bytesOut = new ByteArrayOutputStream();
        int bytesRead;
        try (var file = new BufferedInputStream(multipartFile.getInputStream())) {
            while ((bytesRead = file.read(buffer)) != -1)
            {
                bytesOut.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imagesBytes = bytesOut.toByteArray();
        repository.updatePokermanImage(imagesBytes, nick);
    }

    public byte[] getImage(String nick) {
        return repository.findByNick(nick, ImageOnly.class).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no image for "+ nick)
        ).getImage();
    }

    public Pokerman getPokerman(String nick) {
        return repository.findByNick(nick, Pokerman.class).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "nick is bad")
        );
    }

}
