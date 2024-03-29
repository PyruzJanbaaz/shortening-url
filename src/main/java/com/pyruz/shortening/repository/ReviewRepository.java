package com.pyruz.shortening.repository;

import com.pyruz.shortening.model.entiry.Review;
import com.pyruz.shortening.model.entiry.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findReviewsByUrlAndCreatedAtBetween(Url url, Date from, Date to);
}
