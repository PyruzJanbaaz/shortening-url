package com.pyruz.shortening.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlReviewDTO {
    private String originalURL;
    private String shortURL;
    private Integer review;
}
