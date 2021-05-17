package com.card.wahler.CardWahler.Pokerman;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PokermanService {

    private final PokermanRepository repository;
    private final KeycloakClient keycloakClient;
    private final PokermanMapper pokermanMapper;

    public Pokerman save(Pokerman pokerman) {
        return repository.save(pokerman);
    }

    public Set<PokermanDto> findAll() {
        Stream<Pokerman> stream = StreamSupport.stream(repository.findAll().spliterator(), false);
        return stream.map(pokerman -> pokermanMapper.pokermanToPokermanDto(pokerman)).collect(Collectors.toSet());
    }

    @Transactional
    public void saveImage(MultipartFile multipartFile, String nick) {
        byte[] buffer = new byte[1024];
        var bytesOut = new ByteArrayOutputStream();
        int bytesRead;
        try (var file = new BufferedInputStream(multipartFile.getInputStream())) {
            while ((bytesRead = file.read(buffer)) != -1) {
                bytesOut.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imagesBytes = bytesOut.toByteArray();
        repository.updatePokermanImage(imagesBytes, nick);
    }

    public byte[] getImage(String nick) {
        Iterator<ImageOnly> iterator = repository.findByNick(nick, ImageOnly.class).iterator();
        if (iterator.hasNext()) {
            return iterator.next().getImage();
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no image for " + nick);
    }

    private Pokerman getPokerman(Optional<String> nick, Optional<String> keycloakUserId) {
        if (nick.isPresent() && keycloakUserId.isPresent())
            return repository.findByNickAndKeycloakUserId(nick.get(), keycloakUserId.get(), Pokerman.class)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no user for " + nick));
        else if (nick.isPresent()) {
            Iterator<Pokerman> iterator = repository.findByNick(nick.get(), Pokerman.class).iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no image for " + nick);
        } else
            return repository.findByKeycloakUserId(keycloakUserId.get(), Pokerman.class)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no image for " + nick));
    }

    public PokermanDto getPokermanDto(Optional<String> nick, Optional<String> keycloakUserId) {
        return pokermanMapper.pokermanToPokermanDto(
                getPokerman(nick, keycloakUserId)
        );
    }
}
