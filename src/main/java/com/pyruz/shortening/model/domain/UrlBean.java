package com.pyruz.shortening.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlBean {

    @URL
    private String url;

    @Override
    public int hashCode() {
        final int prime = 100000;
        int result = 1;
        result = result + ((url == null) ? 0 : url.hashCode()) / prime;
        if(result < 0) result *= -1;
        return result;
    }
}
