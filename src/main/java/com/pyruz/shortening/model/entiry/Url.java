package com.pyruz.shortening.model.entiry;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "url")
public class Url {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "original_url", nullable = false)
    private String originalURL;

    private String shortURL;

    private Date creationDate;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "url")
    private List<Review> reviews;

    public Url() {
    }


    public Url(long id, String originalURL, String shortURL, Date creationDate, List<Review> reviews) {
        this.id = id;
        this.originalURL = originalURL;
        this.shortURL = shortURL;
        this.creationDate = creationDate;
        this.reviews = reviews;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
