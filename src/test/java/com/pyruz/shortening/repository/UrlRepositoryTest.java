package com.pyruz.shortening.repository;

import com.pyruz.shortening.model.entiry.Url;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Test
    @DisplayName("It should generate and save a shortening URL")
    void generateShortURL_Success() {
        Url url =  new Url();
        url.setOriginalURL("https://www.mercedes-arena-stuttgart.de/en/inovation");
        url.setShortURL("https://mercedes.de/ERt450");
        url = urlRepository.save(url);
        assertNotNull(url);
        assertThat(url.getId()).isNotZero();
    }

}
