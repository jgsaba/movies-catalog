package test.raven.moviescatalog.integration;

import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

public abstract class BaseIntegrationTests {

    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            "postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword")
            .withCopyFileToContainer(MountableFile.forClasspathResource("init.sql"), "/docker-entrypoint-initdb.d/init.sql");

    static MinIOContainer minIOContainer = new MinIOContainer("minio/minio:RELEASE.2023-09-04T19-57-37Z")
            .withUserName("minioadmin")
            .withPassword("minioadmin");

    @DynamicPropertySource
    static void registerPgProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.profiles.active", () -> "test");
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);

        registry.add("minio.uri", minIOContainer::getS3URL);
    }

    static {
        postgresContainer.start();
        minIOContainer.start();
    }
}
