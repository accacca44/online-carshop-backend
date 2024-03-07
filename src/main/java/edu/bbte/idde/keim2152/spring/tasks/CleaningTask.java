package edu.bbte.idde.keim2152.spring.tasks;

import edu.bbte.idde.keim2152.spring.model.domain.User;
import edu.bbte.idde.keim2152.spring.repository.impl.jpa.JpaUserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class CleaningTask {
    private final JpaUserDao userDao;

    @Value("${clean}")
    Boolean doClean;

    @Scheduled(fixedDelay = 8000)
    public void clean() {
        if (!doClean) {
            return;
        }

        try {
            List<User> users = userDao.findAll();

            // For each user delete the ones, that has no car listing
            users.forEach(user -> {
                if (user.getCarListings().isEmpty()) {
                    userDao.delete(user);
                }
            });
            log.info("Users cleaned!");
        } catch (DataAccessException e) {
            log.error("Error deleting user" + e.getMessage());
        }
    }
}
