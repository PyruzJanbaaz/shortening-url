package com.pyruz.shortening.service.intrface;

import com.pyruz.shortening.model.domain.CustomUrlBean;
import com.pyruz.shortening.model.domain.UrlBean;
import com.pyruz.shortening.model.dto.base.BaseDTO;
import com.pyruz.shortening.model.entiry.Url;
import org.springframework.stereotype.Service;

@Service
public interface IUrlService {
    BaseDTO generateShortURL(UrlBean urlBean);
    BaseDTO generateCustomShortURL(CustomUrlBean customUrlBean);
    Url getCurrentURL(String url);
    BaseDTO getURLReview(String url);
    BaseDTO deleteURL(String url);
}
