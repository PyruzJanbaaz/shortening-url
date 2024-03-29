package com.pyruz.shortening.controller;

import com.pyruz.shortening.handler.CustomServiceException;
import com.pyruz.shortening.model.domain.CustomUrlBean;
import com.pyruz.shortening.model.domain.UrlBean;
import com.pyruz.shortening.model.dto.UrlDTO;
import com.pyruz.shortening.model.entiry.Review;
import com.pyruz.shortening.model.mapper.UrlMapper;
import com.pyruz.shortening.service.impl.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "UrlController", description = "Url management APIs")
public class UrlController {

    final UrlService urlService;
    final UrlMapper urlMapper;

    /**
     * Generate ShortURL.
     * <p>
     * This method creates and returns the stored url.
     *
     * @param urlBean the url
     * @return UrlDTO
     */
    @Operation(
            summary = "Add a new URL",
            description = "Generate a new short URL based on the entered original URL.")
    @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = UrlDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = CustomServiceException.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = CustomServiceException.class), mediaType = "application/json")})
    @PostMapping("/v1/url")
    public ResponseEntity<UrlDTO> generateShortURL(@Valid @RequestBody UrlBean urlBean) {
        UrlDTO urlDTO = urlMapper.toUrlDTO(urlService.generateShortURL(urlBean));
        return new ResponseEntity<>(urlDTO, HttpStatus.CREATED);
    }

    /**
     * Generate custom ShortURL.
     * <p>
     * This method creates and returns the stored url.
     *
     * @param customUrlBean the url and custom portion
     * @return UrlDTO
     */
    @Operation(
            summary = "Add a new custom URL",
            description = "Generate a new custom short URL based on the entered original URL.")
    @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = UrlDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = CustomServiceException.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = CustomServiceException.class), mediaType = "application/json")})
    @PostMapping("/v1/url/custom")
    public ResponseEntity<UrlDTO> generateCustomShortURL(@Valid @RequestBody CustomUrlBean customUrlBean) {
        UrlDTO urlDTO = urlMapper.toUrlDTO(urlService.generateCustomShortURL(customUrlBean));
        return new ResponseEntity<>(urlDTO, HttpStatus.OK);
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
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = CustomServiceException.class), mediaType = "application/json")})
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
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = CustomServiceException.class), mediaType = "application/json")})
    @GetMapping("/v1/url/review")
    public ResponseEntity<List<Review>> getUrlReviewCount(@URL @RequestParam String shortURL) {
        return new ResponseEntity<>(urlService.getURLReview(shortURL), HttpStatus.OK);
    }

    /**
     * Delete a ShortURL.
     * <p>
     * This method removes the url.
     *
     * @param shortURL the shortURL to remove from database
     */
    @Operation(
            summary = "Delete a URL ",
            description = "Delete a URL based on the shortURL by calling the API.")
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = CustomServiceException.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = CustomServiceException.class), mediaType = "application/json")})
    @DeleteMapping("/v1/url")
    public void deleteURL(@URL @RequestParam String shortURL) {
        urlService.deleteURL(shortURL);
    }
}
