package br.com.wishlist.bdd;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
public class SpringIntegrationTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(
            DockerImageName.parse("mongo:latest"))
            .withReuse(true);

}
