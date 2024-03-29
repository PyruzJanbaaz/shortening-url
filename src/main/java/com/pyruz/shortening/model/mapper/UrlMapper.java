package com.pyruz.shortening.model.mapper;

import com.pyruz.shortening.handler.UrlHandler;
import com.pyruz.shortening.model.dto.UrlDTO;
import com.pyruz.shortening.model.entiry.Url;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UrlMapper {
    default UrlDTO toUrlDTO(Url url) {
        UrlDTO urlDTO = new UrlDTO();
        urlDTO.setOriginalURL(url.getOriginalURL());
        urlDTO.setShortURL(UrlHandler.getBaseDomain(url.getOriginalURL()) + "/" + url.getShortURL());
        return urlDTO;
    }
}
