package br.com.wishlist.bdd;


import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@CucumberContextConfiguration
@AutoConfigureMockMvc
@AutoConfigureWebMvc
public class CucumberSpringConfiguration {
}
