package edu.bbte.idde.keim2152.spring.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.keim2152.spring.model.domain.User;
import edu.bbte.idde.keim2152.spring.repository.impl.jpa.JpaUserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class BackupTask {
    private final JpaUserDao userDao;

    @Value("${backup}")
    Boolean doBackup;

    @Scheduled(fixedDelay = 5000)
    public void createBackup() {

        if (!doBackup) {
            return;
        }

        try {
            // Fetch users from the database
            List<User> users = userDao.findAll();

            // Create ObjectMapper instance for JSON serialization
            ObjectMapper objectMapper = new ObjectMapper();

            // Write users directly to a file named backup.json
            File backupFile = new File("backup.json");
            objectMapper.writeValue(backupFile, users);

            log.info("Backup created successfully.");
        } catch (IOException e) {
            log.info("Error creating backup: " + e.getMessage());
        }
    }
}
