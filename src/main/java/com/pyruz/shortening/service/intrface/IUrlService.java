package com.pyruz.shortening.service.intrface;

import com.pyruz.shortening.model.domain.CustomUrlBean;
import com.pyruz.shortening.model.domain.UrlBean;
import com.pyruz.shortening.model.entiry.Review;
import com.pyruz.shortening.model.entiry.Url;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUrlService {
    Url generateShortURL(UrlBean urlBean);
    Url generateCustomShortURL(CustomUrlBean customUrlBean);
    Url getCurrentURL(String url);
    List<Review> getURLReview(String url);
    void deleteURL(String url);
}
