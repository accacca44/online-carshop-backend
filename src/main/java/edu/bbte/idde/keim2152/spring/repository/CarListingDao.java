package edu.bbte.idde.keim2152.spring.repository;

import edu.bbte.idde.keim2152.spring.model.domain.CarListing;

import java.util.Collection;

public interface CarListingDao extends AbstractDao<CarListing> {
    Collection<CarListing> findByMake(String make);
}
