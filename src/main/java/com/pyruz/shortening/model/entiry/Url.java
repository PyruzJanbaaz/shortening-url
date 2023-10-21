package com.pyruz.shortening.model.entiry;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "url")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Url {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "original_url", nullable = false)
    private String originalURL;

    @Column(name = "short_url", nullable = false)
    private String shortURL;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date creationDate;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "url")
    private List<Review> reviews;
}