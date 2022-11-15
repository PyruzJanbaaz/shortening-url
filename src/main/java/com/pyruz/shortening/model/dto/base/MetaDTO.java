package com.pyruz.shortening.model.dto.base;

import com.pyruz.shortening.handler.ApplicationProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaDTO {
    private Integer code;
    private String message;

    public static MetaDTO getInstance(ApplicationProperties applicationProperties) {
        return new MetaDTO(
                HttpStatus.OK.value(),
                applicationProperties.getProperty("application.message.success.text")
        );
    }

}
