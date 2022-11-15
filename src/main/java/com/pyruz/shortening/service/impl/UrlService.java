package com.pyruz.shortening.service.impl;

import com.pyruz.shortening.handler.ApplicationProperties;
import com.pyruz.shortening.handler.TypesHelper;
import com.pyruz.shortening.handler.UrlHandler;
import com.pyruz.shortening.model.domain.CustomUrlBean;
import com.pyruz.shortening.model.domain.UrlBean;
import com.pyruz.shortening.model.dto.base.BaseDTO;
import com.pyruz.shortening.model.dto.base.MetaDTO;
import com.pyruz.shortening.model.dto.base.ServiceExceptionDTO;
import com.pyruz.shortening.model.entiry.Review;
import com.pyruz.shortening.model.entiry.Url;
import com.pyruz.shortening.model.mapper.UrlMapper;
import com.pyruz.shortening.repository.ReviewRepository;
import com.pyruz.shortening.repository.UrlRepository;
import com.pyruz.shortening.service.intrface.IUrlService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Optional;

@Service
public class UrlService implements IUrlService {

    final UrlRepository urlRepository;
    final ReviewRepository reviewRepository;
    final ApplicationProperties applicationProperties;
    final UrlMapper urlMapper;

    public UrlService(UrlRepository urlRepository,
                      ReviewRepository reviewRepository,
                      ApplicationProperties applicationProperties,
                      UrlMapper urlMapper) {
        this.urlRepository = urlRepository;
        this.reviewRepository = reviewRepository;
        this.applicationProperties = applicationProperties;
        this.urlMapper = urlMapper;
    }


    @Override
    public BaseDTO generateShortURL(UrlBean urlBean) {
        String hashCode = TypesHelper.getHashCodeString(urlBean.hashCode());
        Optional<Url> existURL = getURLByShortURL(hashCode);
        if (existURL.isEmpty()) {
            Url url = new Url();
            url.setShortURL(hashCode);
            url.setOriginalURL(urlBean.getUrl());
            url.setCreationDate(Calendar.getInstance().getTime());
            url = urlRepository.save(url);
            return BaseDTO.builder()
                    .meta(MetaDTO.getInstance(applicationProperties))
                    .data(urlMapper.URL_DTO(url))
                    .build();
        }
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .data(urlMapper.URL_DTO(existURL.get()))
                .build();
    }


    @Override
    public BaseDTO generateCustomShortURL(CustomUrlBean customUrlBean) {
        if (getURLByShortURL(customUrlBean.getCustomShortPortion()).isPresent()) {
            throw ServiceExceptionDTO.builder()
                    .code(HttpStatus.NOT_ACCEPTABLE.value())
                    .message(
                            String.format(
                                    applicationProperties.getProperty("application.message.is.already.exist.text"),
                                    customUrlBean.getCustomShortPortion()
                            )
                    ).build();
        }
        Url url = new Url();
        url.setShortURL(customUrlBean.getCustomShortPortion());
        url.setOriginalURL(customUrlBean.getUrl());
        url.setCreationDate(Calendar.getInstance().getTime());
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .data(urlMapper.URL_DTO(urlRepository.save(url)))
                .build();
    }

    private Optional<Url> getURLByShortURL(String shortURL) {
        return urlRepository.findByShortURL(shortURL);
    }

    @Override
    public Url getCurrentURL(String url) {
        Url existUrl = urlRepository.findByShortURL(UrlHandler.getShortPortion(url)).orElseThrow(
                () -> ServiceExceptionDTO.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(applicationProperties.getProperty("application.message.not.found.text"))
                        .build()
        );
        Review review = new Review();
        review.setCreationDate(Calendar.getInstance().getTime());
        review.setIp(TypesHelper.getClientIpAddress());
        review.setUrl(existUrl);
        reviewRepository.save(review);
        return existUrl;
    }

    @Override
    public BaseDTO getURLReview(String url) {
        Url existUrl = urlRepository.findByShortURL(UrlHandler.getShortPortion(url)).orElseThrow(
                () -> ServiceExceptionDTO.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(applicationProperties.getProperty("application.message.not.found.text"))
                        .build()
        );
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .data(
                        reviewRepository.findReviewsByUrlAndCreationDateBetween(
                                existUrl,
                                TypesHelper.yesterdayStartDatetime(),
                                TypesHelper.yesterdayEndDatetime()
                        )
                ).build();
    }

    @Override
    public BaseDTO deleteURL(String url) {
        Url existUrl = getURLByShortURL(UrlHandler.getShortPortion(url)).orElseThrow(
                () -> ServiceExceptionDTO.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(applicationProperties.getProperty("application.message.not.found.text"))
                        .build()
        );
        urlRepository.delete(existUrl);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .build();
    }
}
