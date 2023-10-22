package com.pyruz.shortening.repository;

import com.pyruz.shortening.model.entiry.Url;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Test
    @DisplayName("It should generate and save a shortening URL!")
    void generateShortURL_Success() {
        Url url = Url.builder()
                .originalURL("https://www.mercedes-arena-stuttgart.de/en/inovation")
                .shortURL("https://mercedes.de/ERt450")
                .build();
        url = urlRepository.save(url);
        assertNotNull(url);
        assertThat(url.getId()).isNotZero();
    }

    @Test
    @DisplayName("It should throw an exception when originalURL in Null!")
    void generateShortURL_Failed() {
        Url url = Url.builder()
                .originalURL("https://www.mercedes-arena-stuttgart.de/en/inovation")
                .shortURL("https://mercedes.de/ERt450")
                .build();
        url = urlRepository.save(url);
        assertNotNull(url);
        assertThat(url.getId()).isNotZero();

        Url finalUrl = url;
        finalUrl.setOriginalURL(null);
        urlRepository.save(finalUrl);
        assertThrows(DataIntegrityViolationException.class,
                () -> urlRepository.flush());
    }


}
