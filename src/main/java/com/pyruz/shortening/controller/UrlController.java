package com.pyruz.shortening.controller;

import com.pyruz.shortening.model.domain.CustomUrlBean;
import com.pyruz.shortening.model.domain.UrlBean;
import com.pyruz.shortening.model.dto.base.BaseDTO;
import com.pyruz.shortening.service.impl.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Validated
@RestController
@RequestMapping("/api")
@Tag(name = "UrlController", description = "Url management APIs")
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
     * @param urlBean the url
     * @return Url in baseDto data field
     */
    @Operation(
            summary = "Add a new URL",
            description = "Generate a new short URL based on the entered original URL.")
    @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @PostMapping("/v1/url")
    public ResponseEntity<BaseDTO> generateShortURL(@Valid @RequestBody UrlBean urlBean) {
        return new ResponseEntity<>(urlService.generateShortURL(urlBean), HttpStatus.CREATED);
    }

    /**
     * Generate custom ShortURL.
     * <p>
     * This method creates and returns the stored url.
     *
     * @param customUrlBean the url and custom portion
     * @return Url in baseDto data field
     */
    @Operation(
            summary = "Add a new custom URL",
            description = "Generate a new custom short URL based on the entered original URL.")
    @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @PostMapping("/v1/url/custom")
    public ResponseEntity<BaseDTO> generateCustomShortURL(@Valid @RequestBody CustomUrlBean customUrlBean) {
        return new ResponseEntity<>(urlService.generateCustomShortURL(customUrlBean), HttpStatus.OK);
    }

    /**
     * Redirect to the originalURL.
     * <p>
     * This method redirects to the originalURL.
     *
     * @param shortURL the shortURL to convert to the originalURL
     */
    @Operation(
            summary = "Redirect to original URL",
            description = "Redirecting to original URL by calling the API.")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @GetMapping("/v1/url")
    public void redirectToOriginalUrl(@URL @RequestParam String shortURL, HttpServletResponse response) throws IOException {
        response.sendRedirect(urlService.getCurrentURL(shortURL).getOriginalURL());
    }

    /**
     * Yesterday reviews by ShortURL.
     * <p>
     * This method returns the reviews of an url.
     *
     * @param shortURL the shortURL to get the yesterday reviews
     * @return List<Review> in baseDto data field
     */
    @Operation(
            summary = "Get a URL review count",
            description = "Get a URL review count by calling the API.")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @GetMapping("/v1/url/review")
    public ResponseEntity<BaseDTO> getUrlReviewCount(@URL @RequestParam String shortURL) {
        return new ResponseEntity<>(urlService.getURLReview(shortURL), HttpStatus.OK);
    }

    /**
     * Delete a ShortURL.
     * <p>
     * This method removes the url.
     *
     * @param shortURL the shortURL to remove from database
     * @return baseDto
     */
    @Operation(
            summary = "Delete a URL ",
            description = "Delete a URL based on the shortURL by calling the API.")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = BaseDTO.class), mediaType = "application/json")})
    @DeleteMapping("/v1/url")
    public ResponseEntity<BaseDTO> deleteURL(@URL @RequestParam String shortURL) {
        return new ResponseEntity<>(urlService.deleteURL(shortURL), HttpStatus.OK);
    }
}
