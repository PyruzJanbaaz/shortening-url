package com.pyruz.shortening.model.entiry;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "url")
public class Url {

    @Id
    @GeneratedValue
    private long id;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
