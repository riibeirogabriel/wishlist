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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RemoveAProductInWishlistTest extends CucumberSpringConfiguration {
    @Autowired
    private MockMvc mvc;

    private MvcResult result;

    @Autowired
    MongoTemplate mongoTemplate;

    @Given("the client has a wishlist with one product")
    @SneakyThrows
    public void createNewWishlistForAClient() {
        mvc.perform(post("/customers/1/products").content("""
                {
                	"id": "1"
                }
                """
        ).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
    }

    @When("the client removes this specific product in wishlist")
    @SneakyThrows
    public void removeAnExistentProductInWishlist() {
        result = mvc.perform(delete("/customers/1/products/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
    }

    @Then("the client removes this product in wishlist successfully")
    @SneakyThrows
    public void verifySuccessWhenRemovingExistentProductInWishlist() {
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @After
    public void cleanUp() {
        mongoTemplate.dropCollection("wishlist");
    }
}
