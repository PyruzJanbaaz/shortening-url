package com.pyruz.shortening.controller;

import com.pyruz.shortening.model.domain.CustomUrlBean;
import com.pyruz.shortening.model.domain.UrlBean;
import com.pyruz.shortening.model.dto.base.BaseDTO;
import com.pyruz.shortening.service.impl.UrlService;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Validated
@RestController
@RequestMapping("/api")
public class UrlController {

    final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/url")
    public ResponseEntity<?> generateShortURL(@Valid @RequestBody UrlBean urlBean) {
        return new ResponseEntity<>(urlService.generateShortURL(urlBean), HttpStatus.CREATED);
    }

    @PostMapping("/url/custom")
    public ResponseEntity<?> generateCustomShortURL(@Valid @RequestBody CustomUrlBean customUrlBean) {
        return new ResponseEntity<>(urlService.generateCustomShortURL(customUrlBean), HttpStatus.OK);
    }

    @GetMapping("/url")
    public void redirectToOriginalUrl(@URL @RequestParam String shortURL, HttpServletResponse response) throws IOException {
        response.sendRedirect(urlService.getCurrentURL(shortURL).getOriginalURL());
    }

    @GetMapping("/url/review")
    public ResponseEntity<?> getUrlReviewCount(@URL @RequestParam String shortURL) {
        return new ResponseEntity<>(urlService.getURLReview(shortURL), HttpStatus.OK);
    }

    @DeleteMapping("/url")
    public ResponseEntity<?> redirectToOriginalUrl(@URL @RequestParam String shortURL) {
        return new ResponseEntity<>(urlService.deleteURL(shortURL), HttpStatus.OK);
    }
}
