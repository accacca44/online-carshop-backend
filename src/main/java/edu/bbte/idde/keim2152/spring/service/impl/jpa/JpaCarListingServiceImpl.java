package edu.bbte.idde.keim2152.spring.service.impl.jpa;

import edu.bbte.idde.keim2152.spring.mapper.CarListingMapper;
import edu.bbte.idde.keim2152.spring.model.domain.CarListing;
import edu.bbte.idde.keim2152.spring.model.dto.incoming.CarListingIncomingDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.CarListingDetailedDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.CarListingReducedDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.ReducedImageDto;
import edu.bbte.idde.keim2152.spring.repository.impl.jpa.JpaCarListingDao;
import edu.bbte.idde.keim2152.spring.service.CarListingService;
import edu.bbte.idde.keim2152.spring.service.exception.ServiceEntityNotFoundException;
import edu.bbte.idde.keim2152.spring.service.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JpaCarListingServiceImpl implements CarListingService {
    private final JpaCarListingDao jpaCarListingDao;
    private final CarListingMapper carListingMapper;

    @Override
    public CarListingDetailedDto create(CarListingIncomingDto carListingIncomingDto) {
        CarListing carListing = carListingMapper.carListingIncomingToDomain(carListingIncomingDto);

        try {
            carListing = jpaCarListingDao.save(carListing);
            return carListingMapper.domainToDetailedOutgoing(carListing);
        } catch (DataAccessException e) {
            log.trace("Error creating carlisting", e);
            throw new ServiceException("Error creating carlisting", e);
        }
    }

    @Override
    public CarListingDetailedDto update(Long id, CarListingIncomingDto carListingIncomingDto) {
        CarListing carListing = carListingMapper.carListingIncomingToDomain(carListingIncomingDto);
        carListing.setId(id);

        Optional<CarListing> carListingOptional;
        if (jpaCarListingDao.existsById(id)) {
            try {
                carListingOptional = jpaCarListingDao.findById(id);
            } catch (DataAccessException e) {
                log.trace("Error updating carlisting", e);
                throw new ServiceException("Error updating carlisting", e);
            }
        } else {
            log.trace("Error updating carlisting");
            throw new ServiceEntityNotFoundException("Error updating carlisting");
        }

        if (carListingOptional.isEmpty()) {
            log.trace("Error updating carlisting");
            throw new ServiceEntityNotFoundException("Error updating carlisting");
        }

        try {
            carListing = jpaCarListingDao.save(carListing);
            return carListingMapper.domainToDetailedOutgoing(carListing);
        } catch (DataAccessException e) {
            log.trace("Error updating carlisting", e);
            throw new ServiceException("Error updating carlisting", e);
        }
    }


    @Override
    public void delete(Long id) {
        Optional<CarListing> carListing;

        try {
            carListing = jpaCarListingDao.findById(id);
        } catch (DataAccessException e) {
            log.trace("Error deleting carlisting", e);
            throw new ServiceException("Error deleting carlisting", e);
        }

        if (carListing.isEmpty()) {
            log.trace("Error deleting carlisting");
            throw new ServiceEntityNotFoundException("Error deleting carlisting");
        }

        try {
            jpaCarListingDao.delete(carListing.get());
        } catch (DataAccessException e) {
            log.trace("Error deleting carlisting", e);
            throw new ServiceException("Error deleting carlisting", e);
        }
    }

    @Override
    public CarListingDetailedDto findById(Long id) {
        Optional<CarListing> carListing;

        try {
            carListing = jpaCarListingDao.findById(id);
        } catch (DataAccessException e) {
            log.trace("Error finding carlisting", e);
            throw new ServiceException("Error finding carlisting", e);
        }

        if (carListing.isEmpty()) {
            log.trace("Error finding carlisting");
            throw new ServiceEntityNotFoundException("Error finding carlisting");
        }

        // For all the images associated, load the images as bytes and set to response dto
        var images = carListing.get().getImages();
        var outDto = carListingMapper.domainToDetailedOutgoing(carListing.get());
        var listOfImages = new ArrayList<ReducedImageDto>();
        images.forEach(image -> {
            String imageDir = "src/main/resources/assets/images";
            try {
                var imageBytes = readFile(imageDir, image.getPath());
                listOfImages.add(new ReducedImageDto(image.getPath(), imageBytes));
            } catch (IOException e) {
                throw new ServiceException("Error reading image from local storage",e );
            }

        });
        outDto.setImages(listOfImages);
        return outDto;
    }

    @Override
    public Collection<CarListingReducedDto> findAll() {
        try {
            Collection<CarListing> carListings = jpaCarListingDao.findAll();
            return carListingMapper.domainsToReducedOutgoings(carListings);
        } catch (DataAccessException e) {
            log.trace("Error finding carlisting", e);
            throw new ServiceException("Error finding carlisting", e);
        }
    }
    
    @Override
    public Collection<CarListingReducedDto> findByMake(String make) {
        try {
            Collection<CarListing> carListings = jpaCarListingDao.findAllByMake(make);
            return carListingMapper.domainsToReducedOutgoings(carListings);
        } catch (DataAccessException e) {
            log.trace("Error finding carlisting", e);
            throw new ServiceException("Error finding carlisting", e);
        }
    }

    @Override
    public Collection<CarListingReducedDto> findAllPaginatedByUserId(Long userId,
                                                                     Integer page,
                                                                     Integer number,
                                                                     String attribute,
                                                                     Boolean isAsc) {
        try {
            Pageable pageable = requestPageable(page, number, attribute, isAsc);
            Page<CarListing> cars = jpaCarListingDao.findByUserId(userId, pageable);
            return carListingMapper.domainsToReducedOutgoings(cars.getContent());
        } catch (DataAccessException e) {
            log.trace("Error finding car listing", e);
            throw new ServiceException("Error finding car listing", e);
        }
    }

    @Override
    public Collection<CarListingReducedDto> findAllPaginated(Integer page,
                                                             Integer number,
                                                             String attribute,
                                                             Boolean isAsc) {
        try {
            Pageable pageable = requestPageable(page, number, attribute, isAsc);
            Page<CarListing> cars = jpaCarListingDao.findAll(pageable);
            return carListingMapper.domainsToReducedOutgoings(cars.getContent());
        } catch (DataAccessException e) {
            log.error("Error finding car listings");
            throw new ServiceException("Error finding car listings", e);
        } catch (PropertyReferenceException e) {
            log.error("Attribute " + attribute + " is not part of car listing entity");
            throw new ServiceException("Attribute " + attribute + " is not part of car listing entity", e);
        }
    }

    private Pageable requestPageable(Integer page, Integer number, String attribute, Boolean isAsc) {
        if (isAsc) {
            return PageRequest.of(page, number, Sort.by(attribute).descending());
        }
        return PageRequest.of(page, number, Sort.by(attribute));
    }

    private byte[] readFile(String directory, String fileName) throws IOException {
        Path directoryPath = Paths.get(directory);
        Path filePath = Paths.get(directory, fileName);
        return Files.readAllBytes(filePath);
    }
}
