package com.pyruz.shortening.controller;

import com.pyruz.shortening.model.domain.CustomUrlBean;
import com.pyruz.shortening.model.domain.UrlBean;
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

    /**
     * Generate ShortURL.
     * <p>
     * This method creates and returns the stored url.
     *
     * @param  urlBean  the url
     * @return Url in baseDto data field
     */
    @PostMapping("/v1/url")
    public ResponseEntity<?> generateShortURL(@Valid @RequestBody UrlBean urlBean) {
        return new ResponseEntity<>(urlService.generateShortURL(urlBean), HttpStatus.CREATED);
    }

    /**
     * Generate custom ShortURL.
     * <p>
     * This method creates and returns the stored url.
     *
     * @param  customUrlBean  the url and custom portion
     * @return Url in baseDto data field
     */
    @PostMapping("/v1/url/custom")
    public ResponseEntity<?> generateCustomShortURL(@Valid @RequestBody CustomUrlBean customUrlBean) {
        return new ResponseEntity<>(urlService.generateCustomShortURL(customUrlBean), HttpStatus.OK);
    }

    /**
     * Redirect to the originalURL.
     * <p>
     * This method redirects to the originalURL.
     *
     * @param  shortURL  the shortURL to convert to the originalURL
     */
    @GetMapping("/v1/url")
    public void redirectToOriginalUrl(@URL @RequestParam String shortURL, HttpServletResponse response) throws IOException {
        response.sendRedirect(urlService.getCurrentURL(shortURL).getOriginalURL());
    }

    /**
     * Yesterday reviews by ShortURL.
     * <p>
     * This method returns the reviews of an url.
     *
     * @param  shortURL  the shortURL to get the yesterday reviews
     * @return List<Review> in baseDto data field
     */
    @GetMapping("/v1/url/review")
    public ResponseEntity<?> getUrlReviewCount(@URL @RequestParam String shortURL) {
        return new ResponseEntity<>(urlService.getURLReview(shortURL), HttpStatus.OK);
    }

    /**
     * Delete a ShortURL.
     * <p>
     * This method removes the url.
     *
     * @param  shortURL  the shortURL to remove from database
     * @return baseDto
     */
    @DeleteMapping("/v1/url")
    public ResponseEntity<?> redirectToOriginalUrl(@URL @RequestParam String shortURL) {
        return new ResponseEntity<>(urlService.deleteURL(shortURL), HttpStatus.OK);
    }
}
