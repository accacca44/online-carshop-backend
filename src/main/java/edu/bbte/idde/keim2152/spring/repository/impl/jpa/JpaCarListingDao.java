package edu.bbte.idde.keim2152.spring.repository.impl.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

import edu.bbte.idde.keim2152.spring.model.domain.CarListing;

@Repository
public interface JpaCarListingDao extends JpaRepository<CarListing, Long> {
    Collection<CarListing> findAllByMake(String make);

    Page<CarListing> findByUserId(Long userId, Pageable pageable);
}
