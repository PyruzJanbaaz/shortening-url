package com.pyruz.shortening.model.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUrlBean {
    @URL
    private String url;
    @NotBlank
    @Size(max = 6)
    private String customShortPortion;
}
