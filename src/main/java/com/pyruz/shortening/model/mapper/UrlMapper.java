package com.pyruz.shortening.model.mapper;

import com.pyruz.shortening.handler.UrlHandler;
import com.pyruz.shortening.model.dto.UrlDTO;
import com.pyruz.shortening.model.entiry.Url;
import org.mapstruct.*;

@Mapper
public interface UrlMapper {

    default UrlDTO URL_DTO(Url url) {
        UrlDTO urlDTO = new UrlDTO();
        urlDTO.setOriginalURL(url.getOriginalURL());
        urlDTO.setShortURL(UrlHandler.getBaseDomain(url.getOriginalURL()) + "/" + url.getShortURL());
        return urlDTO;
    }

}
