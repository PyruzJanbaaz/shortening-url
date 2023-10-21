package com.pyruz.shortening.controller;

import com.pyruz.shortening.ShorteningURLApplication;
import com.pyruz.shortening.model.domain.UrlBean;
import com.pyruz.shortening.model.dto.base.BaseDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = ShorteningURLApplication.class)
class UrlControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @SneakyThrows
    @Test
    @DisplayName("It should generate and save a shortening URL and return 201 as Http status code")
    void generateShortURL_Created() {
        UrlBean urlBean = UrlBean.builder()
                .url("https://www.mercedes-arena-stuttgart.de/en/inovation")
                .build();
        ResponseEntity<BaseDTO> responseEntity = restTemplate.postForEntity("/api/v1/url", urlBean, BaseDTO.class);
        assertEquals(CREATED, responseEntity.getStatusCode());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should return 400 as Http status code")
    void generateShortURL_BadRequest() {
        UrlBean urlBean = UrlBean.builder()
                .url("BAD URL")
                .build();
        ResponseEntity<BaseDTO> responseEntity = restTemplate.postForEntity("/api/v1/url", urlBean, BaseDTO.class);
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }



}
