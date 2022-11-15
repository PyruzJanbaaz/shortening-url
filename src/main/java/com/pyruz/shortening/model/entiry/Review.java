package com.pyruz.shortening.model.entiry;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue
    private long id;

    private String ip;

    private Date creationDate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id")
    private Url url;

    public Review() {
    }

    public Review(long id, String ip, Date creationDate, Url url) {
        this.id = id;
        this.ip = ip;
        this.creationDate = creationDate;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }
}
