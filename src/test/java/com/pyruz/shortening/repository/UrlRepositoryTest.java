package com.pyruz.shortening.repository;

import com.pyruz.shortening.model.entiry.Url;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Test
    @Transactional
    @DisplayName("It should generate and save a shortening URL!")
    void generateShortURL_Success() {
        Url url = new Url();
        url.setOriginalURL("https://www.mercedes-arena-stuttgart.de/en/inovation");
        url.setShortURL("https://mercedes.de/ERt450");
        url = urlRepository.save(url);
        assertNotNull(url);
        assertThat(url.getId()).isNotZero();
    }

    @Test
    @Transactional
    @DisplayName("It should throw an exception when originalURL in Null!")
    void generateShortURL_Failed() {
        Url url = new Url();
        url.setOriginalURL("https://www.mercedes-arena-stuttgart.de/en/inovation");
        url.setShortURL("https://mercedes.de/ERt450");
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
