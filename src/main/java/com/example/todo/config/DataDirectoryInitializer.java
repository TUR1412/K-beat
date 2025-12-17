package com.example.todo.config;

import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataDirectoryInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataDirectoryInitializer.class);

    private final String datasourceUrl;

    public DataDirectoryInitializer(@Value("${spring.datasource.url:}") String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (datasourceUrl == null || !datasourceUrl.startsWith("jdbc:h2:file:")) {
            return;
        }

        Path dataDir = Path.of("data");
        Files.createDirectories(dataDir);
        log.info("Data directory ready: {}", dataDir.toAbsolutePath());
    }
}
