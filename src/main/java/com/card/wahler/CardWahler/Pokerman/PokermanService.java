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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PokermanService {

    private final PokermanRepository repository;
    private final PokermanMapper pokermanMapper;

    public Pokerman save(Pokerman pokerman) {
        return repository.save(pokerman);
    }

    public Set<PokermanDto> findAll() {
        Stream<Pokerman> stream = StreamSupport.stream(repository.findAll().spliterator(), false);
        return stream.map(pokermanMapper::pokermanToPokermanDto).collect(Collectors.toSet());
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
        Optional<ImageOnly> image = repository.findByNick(nick, ImageOnly.class);
        if (image.isPresent()) {
            return image.get().getImage();
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no image for " + nick);
    }

    public PokermanDto getPokermanDtoByNick(String nick) {
        return pokermanMapper.pokermanToPokermanDto(repository.findByNick(nick, Pokerman.class)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no user for " + nick)));
    }

    public PokermanDto getPokermanDtoById(String id) {
        return  pokermanMapper.pokermanToPokermanDto( repository.findById(id, Pokerman.class)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no user for " + id)));
    }
}
