package pt.com.bayonnesensei.salesInfo.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
public class AbstractContainerProvider {
    static MySQLContainer mySQLContainer = new MySQLContainer(DockerImageName.parse("mysql:8.0.24")).withDatabaseName("sales_info");

    @DynamicPropertySource
    public static void setup(DynamicPropertyRegistry dynamicPropertyRegistry){
        Startables.deepStart(mySQLContainer).join();

        dynamicPropertyRegistry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username",mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password",mySQLContainer::getPassword);
    }
}
