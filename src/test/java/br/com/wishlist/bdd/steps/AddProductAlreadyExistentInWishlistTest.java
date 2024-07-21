package br.com.wishlist.bdd.steps;

import br.com.wishlist.bdd.CucumberSpringConfiguration;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AddProductAlreadyExistentInWishlistTest extends CucumberSpringConfiguration {
    @Autowired
    private MockMvc mvc;

    private MvcResult result;

    @Autowired
    MongoTemplate mongoTemplate;

    @Given("the client has a wishlist with a specific product")
    @SneakyThrows
    public void insertProductInWishlist() {
        MvcResult result = mvc.perform(post("/customers/1/products").content("""
                {
                	"id": "1"
                }
                """
        ).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
    }

    @When("the client inserts the same product in wishlist")
    @SneakyThrows
    public void insertSameProductInWishlist() {
        result = mvc.perform(post("/customers/1/products").content("""
                {
                	"id": "1"
                }
                """
        ).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
    }

    @Then("the client receives an error message about wishlist already containing the product")
    @SneakyThrows
    public void verifyErrorWhenInsertingSameProductInWishlist() {
        assertEquals(HttpStatus.CONFLICT.value(), result.getResponse().getStatus());
    }

    @After
    public void cleanUp() {
        mongoTemplate.dropCollection("wishlist");
    }
}
