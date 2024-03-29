package com.pyruz.shortening.service.impl;

import com.pyruz.shortening.handler.CustomServiceException;
import com.pyruz.shortening.handler.TypesHelper;
import com.pyruz.shortening.handler.UrlHandler;
import com.pyruz.shortening.model.domain.CustomUrlBean;
import com.pyruz.shortening.model.domain.UrlBean;
import com.pyruz.shortening.model.dto.UrlDTO;
import com.pyruz.shortening.model.entiry.Review;
import com.pyruz.shortening.model.entiry.Url;
import com.pyruz.shortening.model.mapper.UrlMapper;
import com.pyruz.shortening.repository.ReviewRepository;
import com.pyruz.shortening.repository.UrlRepository;
import com.pyruz.shortening.service.intrface.IUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlService implements IUrlService {

    final UrlRepository urlRepository;
    final ReviewRepository reviewRepository;

    @Override
    public Url generateShortURL(UrlBean urlBean) {
        String hashCode = TypesHelper.getHashCodeString(urlBean.hashCode());
        Optional<Url> existURL = getURLByShortURL(hashCode);
        if (existURL.isEmpty()) {
            Url url = Url.builder()
                    .shortURL(hashCode)
                    .originalURL(urlBean.getUrl())
                    .build();
            return urlRepository.save(url);
        }
        return existURL.get();
    }

    @Override
    public Url generateCustomShortURL(CustomUrlBean customUrlBean) {
        if (getURLByShortURL(customUrlBean.getCustomShortPortion()).isPresent()) {
            throw new CustomServiceException(
                    "application.message.is.already.exist.text",
                    new Object[]{customUrlBean.getCustomShortPortion()},
                    HttpStatus.BAD_REQUEST);
        }
        Url url = Url.builder()
                .shortURL(customUrlBean.getCustomShortPortion())
                .originalURL(customUrlBean.getUrl())
                .build();
        return urlRepository.save(url);
    }

    private Optional<Url> getURLByShortURL(String shortURL) {
        return urlRepository.findByShortURL(shortURL);
    }

    @Override
    public Url getCurrentURL(String url) {
        Url existUrl = urlRepository.findByShortURL(UrlHandler.getShortPortion(url)).orElseThrow(
                () -> new CustomServiceException(
                        "application.message.not.found.text",
                        null,
                        HttpStatus.NOT_FOUND)
        );
        reviewRepository.save(
                Review.builder()
                        .ip(TypesHelper.getClientIpAddress())
                        .url(existUrl)
                        .build()
        );
        return existUrl;
    }

    @Override
    public List<Review> getURLReview(String url) {
        Url existUrl = urlRepository.findByShortURL(UrlHandler.getShortPortion(url)).orElseThrow(
                () -> new CustomServiceException(
                        "application.message.not.found.text",
                        null,
                        HttpStatus.NOT_FOUND)
        );
        return reviewRepository.findReviewsByUrlAndCreatedAtBetween(
                existUrl,
                TypesHelper.yesterdayStartDatetime(),
                TypesHelper.yesterdayEndDatetime());
    }

    @Override
    public void deleteURL(String url) {
        Url existUrl = getURLByShortURL(UrlHandler.getShortPortion(url)).orElseThrow(
                () -> new CustomServiceException(
                        "application.message.not.found.text",
                        null,
                        HttpStatus.NOT_FOUND)
        );
        urlRepository.delete(existUrl);
    }
}
