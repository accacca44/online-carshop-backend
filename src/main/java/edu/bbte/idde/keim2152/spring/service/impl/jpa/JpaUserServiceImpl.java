package edu.bbte.idde.keim2152.spring.service.impl.jpa;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.bbte.idde.keim2152.spring.model.domain.CarImage;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.CarListingReducedDto;
import edu.bbte.idde.keim2152.spring.repository.exception.EntityNotFoundException;
import edu.bbte.idde.keim2152.spring.repository.impl.jpa.JpaCarImageDao;
import edu.bbte.idde.keim2152.spring.service.CarListingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import edu.bbte.idde.keim2152.spring.mapper.CarListingMapper;
import edu.bbte.idde.keim2152.spring.mapper.UserMapper;
import edu.bbte.idde.keim2152.spring.model.domain.CarListing;
import edu.bbte.idde.keim2152.spring.model.domain.User;
import edu.bbte.idde.keim2152.spring.model.dto.incoming.UserCarlistingIncomingDto;
import edu.bbte.idde.keim2152.spring.model.dto.incoming.UserIncomingDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.UserDetailedDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.UserReducedDto;
import edu.bbte.idde.keim2152.spring.repository.impl.jpa.JpaCarListingDao;
import edu.bbte.idde.keim2152.spring.repository.impl.jpa.JpaUserDao;
import edu.bbte.idde.keim2152.spring.service.UserService;
import edu.bbte.idde.keim2152.spring.service.exception.ServiceEntityNotFoundException;
import edu.bbte.idde.keim2152.spring.service.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Slf4j
public class JpaUserServiceImpl implements UserService {
    private final JpaUserDao jpaUserDao;
    private final UserMapper userMapper;
    private final JpaCarListingDao jpaCarListingDao;
    private final CarListingMapper carListingMapper;
    private final CarListingService carListingService;
    private final JpaCarImageDao carImageDao;

    @Override
    public UserDetailedDto create(UserIncomingDto userIncomingDto) {
        User user = new User();
        userMapper.userIncomingToDomain(userIncomingDto, user);
        log.info("ASDASDADASDASDASDADADDASDASDADAS");
        try {
            user.setLang("lang");
            user = jpaUserDao.save(user);
            log.info(user.getLang());
            return userMapper.domainToDetailedOutgoing(user);
        } catch (DataAccessException e) {
            log.trace("Error creating user", e);
            throw new ServiceException("Error creating user", e);
        }
    }

    @Override
    public UserDetailedDto update(Long id, UserIncomingDto userIncomingDto) {
        Optional<User> userOptional;
        if (jpaUserDao.existsById(id)) {
            try {
                userOptional = jpaUserDao.findById(id);
            } catch (DataAccessException e) {
                log.trace("Error updating user", e);
                throw new ServiceException("Error updating user", e);
            }
        } else {
            log.trace("Error updating user");
            throw new ServiceEntityNotFoundException("Error updating user");
        }

        if (userOptional.isEmpty()) {
            log.trace("Error updating carlisting");
            throw new ServiceEntityNotFoundException("Error updating user");
        }

        try {
            var user = userOptional.get();
            userMapper.userIncomingToDomain(userIncomingDto, user);
            user = jpaUserDao.save(user);
            return userMapper.domainToDetailedOutgoing(user);
        } catch (DataAccessException e) {
            log.trace("Error updating user", e);
            throw new ServiceException("Error updating user", e);
        }
    }

    @Override
    public void delete(Long id) {
        Optional<User> userOptional;

        try {
            userOptional = jpaUserDao.findById(id);
        } catch (DataAccessException e) {
            log.trace("Error deleting user", e);
            throw new ServiceException("Error deleting user", e);
        }

        if (userOptional.isEmpty()) {
            log.trace("Error deleting user");
            throw new ServiceEntityNotFoundException("Error deleting user");
        }

        try {
            jpaUserDao.delete(userOptional.get());
        } catch (DataAccessException e) {
            log.trace("Error deleting user", e);
            throw new ServiceException("Error deleting user", e);
        }
    }

    @Override
    public UserDetailedDto findById(Long id) {
        Optional<User> userOptional;

        try {
            userOptional = jpaUserDao.findById(id);
        } catch (DataAccessException e) {
            log.error("Error finding user", e);
            throw new ServiceException("Error finding user", e);
        }

        if (userOptional.isEmpty()) {
            throw new ServiceEntityNotFoundException("Error finding user");
        }
        
        return userMapper.domainToDetailedOutgoing(userOptional.get());
    }

    @Override
    public Collection<UserReducedDto> findAll() {
        try {
            Collection<User> users = jpaUserDao.findAll();
            return userMapper.domainsToReducedOutgoings(users);
        } catch (DataAccessException e) {
            log.error("Error finding users", e);
            throw new ServiceException("Error finding users", e);
        }
    }

    @Override
    public Collection<UserReducedDto> findAllPaginated(Integer pageNumber,
                                                       Integer nrOfEntities,
                                                       String attribute,
                                                       Boolean isAsc) {
        try {
            Pageable pageable = buildRequest(pageNumber, nrOfEntities, attribute, isAsc);
            Page<User> users = jpaUserDao.findAll(pageable);

            return userMapper.domainsToReducedOutgoings(users.getContent());
        } catch (DataAccessException e) {
            log.error("Error finding users");
            throw new ServiceException("Error finding users", e);
        }
    }

    @Override
    public Collection<CarListingReducedDto> findCarsPaginatedByUser(Long userId,
                                                                    Integer page,
                                                                    Integer number,
                                                                    String attribute,
                                                                    Boolean isAsc) {
        try {
            Optional<User> userOptional;
            try {
                userOptional = jpaUserDao.findById(userId);
            } catch (DataAccessException e) {
                log.error("Error finding user", e);
                throw new ServiceException("Error finding user", e);
            }

            if (userOptional.isEmpty()) {
                log.error("Error finding user");
                throw new ServiceEntityNotFoundException("Error finding user");
            }

            return carListingService.findAllPaginatedByUserId(userId, page, number, attribute, isAsc);


        } catch (DataAccessException e) {
            log.error("Error finding listings");
            throw new ServiceException("Error finding listings", e);
        }
    }

    private Pageable buildRequest(Integer pageNumber, Integer nrOfEntities, String attribute, Boolean isAsc) {
        if (isAsc) {
            return PageRequest.of(pageNumber, nrOfEntities, Sort.by(attribute).descending());
        }
        return PageRequest.of(pageNumber, nrOfEntities, Sort.by(attribute));

    }

    @Override
    public Collection<UserReducedDto> findByEmail(String email) {
        try {
            Collection<User> users = jpaUserDao.findAllByEmail(email);
            return userMapper.domainsToReducedOutgoings(users);
        } catch (DataAccessException e) {
            log.error("Error finding users", e);
            throw new ServiceException("Error finding users", e);
        }
    }

    @Override
    @Transactional
    public UserDetailedDto createCarListing(Long userId, UserCarlistingIncomingDto userCarlistingIncomingDto, List<MultipartFile> images) {
        try {
            Optional<User> optionalUser = jpaUserDao.findById(userId);
            if (optionalUser.isEmpty()) {
                throw new EntityNotFoundException("User Not Found!");
            }

            var user = optionalUser.get();

            // set the user of the new carlisting
            CarListing carListing = carListingMapper.userCarlistingToDomain(userCarlistingIncomingDto);
            carListing.setUser(user);

            // Save the new car listing first
            carListing = jpaCarListingDao.save(carListing);

            // Now associate the images with the saved car listing
            CarListing finalCarListing = carListing;
            images.forEach(image -> {
                try {
                    String path = finalCarListing.getId() + "-" + image.getOriginalFilename();
                    var imageDomain = new CarImage();
                    imageDomain.setCarListing(finalCarListing);
                    imageDomain.setPath(path);
                    carImageDao.save(imageDomain);

                    // Save the image to local storage
                    String imageDir = "src/main/resources/assets/images";
                    saveFile(image.getBytes(), imageDir, path);

                } catch (DataAccessException e) {
                    // Handle exception appropriately, e.g., log it or throw a ServiceException
                    throw new ServiceException("Error saving car image", e);
                } catch (IOException e) {
                    throw new ServiceException("Error saving into local storage", e);
                }
            });

            // Ensure the saved car listing is updated in the user's listings
            var carListings = user.getCarListings();
            carListings.add(carListing);
            user.setCarListings(carListings);
            user = jpaUserDao.save(user);

            return userMapper.domainToDetailedOutgoing(user);
        } catch (DataAccessException e) {
            throw new ServiceException("Error creating carlisting", e);
        } catch (EntityNotFoundException e) {
            throw new ServiceEntityNotFoundException("User not found!");
        }
    }


    @Override
    @Transactional
    public UserDetailedDto updateCarListing(Long userId, Long carListingId, UserCarlistingIncomingDto dto) {
        try {
            Optional<User> userOptional;
            try {
                userOptional = jpaUserDao.findById(userId);
            } catch (DataAccessException e) {
                log.error("Error finding user", e);
                throw new ServiceException("Error finding user", e);
            }

            if (userOptional.isEmpty()) {
                log.error("Error finding user");
                throw new ServiceEntityNotFoundException("Error finding user");
            }

            // Retrieve the existing carListing
            Optional<CarListing> carListingOptional = jpaCarListingDao.findById(carListingId);

            if (carListingOptional.isEmpty()) {
                log.error("Error finding car listing");
                throw new ServiceEntityNotFoundException("Error finding car listing");
            }

            // Update the car listing
            var carListing = carListingOptional.get();
            carListingMapper.userCarsToDomain(dto, carListing);

            jpaCarListingDao.save(carListing);

            var user = jpaUserDao.findById(userId).get();
            return userMapper.domainToDetailedOutgoing(user);

        } catch (DataAccessException e) {
            log.error("Error finding listings");
            throw new ServiceException("Error finding listings", e);
        }
    }

    @Override
    public UserDetailedDto updateLang(Long id, String lang) {
       try{

           var user = jpaUserDao.findById(id).get();
           user.setLang(lang);
           jpaUserDao.save(user);

           return userMapper.domainToDetailedOutgoing(user);
       }catch (DataAccessException e) {
           log.error(e.getLocalizedMessage());
           throw new ServiceException("Error updating lang!");
       }
    }

    @Override
    public UserDetailedDto updateTheme(Long id, String theme) {
        try{

            var user = jpaUserDao.findById(id).get();
            user.setTheme(theme);
            log.info(user.getTheme());
            jpaUserDao.save(user);

            return userMapper.domainToDetailedOutgoing(user);
        }catch (DataAccessException e) {
            log.error(e.getLocalizedMessage());
            throw new ServiceException("Error updating theme!");
        }
    }

    @Override
    @Transactional
    public UserDetailedDto deleteCarListing(Long userId, Long carListingId) {
        Optional<User> userOptional;
        Optional<CarListing> carListingOptional;

        try {
            userOptional = jpaUserDao.findById(userId);
            
            if (userOptional.isEmpty()) {
                log.error("Error deleting car listing");
                throw new ServiceEntityNotFoundException("Error deleting car listing");
            }
            carListingOptional = jpaCarListingDao.findById(carListingId);
            if (carListingOptional.isEmpty()) {
                log.error("Error deleting car listing");
                throw new ServiceEntityNotFoundException("Error deleting car listing");
            }

            var user = userOptional.get();
            var carListing = carListingOptional.get();
            var carListings = user.getCarListings();
            if (!carListings.contains(carListing)) {
                log.error("Car listing does not belong to the user");
                throw new ServiceEntityNotFoundException("Car listing does not belong to the user");
            }

            carListings.remove(carListing);
            user.setCarListings(carListings);
            log.info("User after car listing deletion " + user.toString());
            jpaCarListingDao.delete(carListing);

            user = jpaUserDao.findById(userId).get();

            var images = carListing.getImages();
            images.forEach(this::deleteImage);

            return userMapper.domainToDetailedOutgoing(user);

        } catch (DataAccessException e) {
            log.error("Error deleting car listing", e);
            throw new ServiceException("Error deleting car listing", e);
        }



    }

    private void deleteImage(CarImage carImage) {
        String dir = "src/main/resources/assets/images";
        String fileName = carImage.getPath();

        Path filePath = Paths.get(dir, fileName);
        try {
            Files.delete(filePath);
            log.debug("Image deleted successfully.");
        } catch (Exception e) {
            log.error("Error deleting image: " + e.getMessage());
        }
    }

    public void saveFile(byte[] data, String directory, String fileName) throws IOException {
        Path directoryPath = Paths.get(directory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Path filePath = Paths.get(directory, fileName);
        Files.write(filePath, data);
    }

}
