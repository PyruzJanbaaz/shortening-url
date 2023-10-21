package com.pyruz.shortening.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlBean urlBean = (UrlBean) o;
        return Objects.equals(url, urlBean.url);
    }
}
