package edu.bbte.idde.keim2152.spring.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.keim2152.spring.model.domain.CarListing;
import edu.bbte.idde.keim2152.spring.model.domain.User;
import edu.bbte.idde.keim2152.spring.repository.impl.jpa.JpaUserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.OptionalDouble;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class StatisticsTask {
    private final JpaUserDao userDao;

    @Value("${statistics}")
    Boolean doStatistics;

    @Scheduled(fixedDelay = 2000)
    public void createStatistics() {

        if (!doStatistics) {
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<User> users = userDao.findAll();

            // For each user get how many car listings they have
            users.forEach(user -> {
                if (! user.getCarListings().isEmpty()) {
                    String name = user.getUsername();
                    Double totalValue = user.getCarListings().stream().mapToDouble(CarListing::getPrice).sum();
                    OptionalDouble average = user.getCarListings().stream()
                            .mapToDouble(CarListing::getPrice)
                            .average();

                    Double avgResult = average.isPresent() ? average.getAsDouble() : 0.0;

                    // Save the statistics with the current time;
                    Statistics statistics = new Statistics(name, totalValue, avgResult);
                    writeStatisticsToFile(statistics, objectMapper);
                }
            });
            log.info("Statistics Created!");
        } catch (DataAccessException e) {
            log.error("Error creating statistics" + e.getMessage());
        }
    }

    private void writeStatisticsToFile(Statistics statistics, ObjectMapper objectMapper) {
        try {
            String statisticsFilePath = "statistics.json";
            Path statisticsPath = Path.of(statisticsFilePath);

            // If the file does not exist, create a new one
            if (!Files.exists(statisticsPath)) {
                try {
                    Files.createFile(statisticsPath);
                    log.info("Statistics file created at: " + statisticsFilePath);
                } catch (IOException ex) {
                    log.error("Unable to create statistics file at: " + statisticsFilePath);
                    return;
                }
            }

            try (BufferedWriter writer = Files.newBufferedWriter(statisticsPath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND)) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, statistics);
            }
        } catch (IOException e) {
            log.error("Error writing statistics to file: " + e.getMessage());
        }
    }
}
