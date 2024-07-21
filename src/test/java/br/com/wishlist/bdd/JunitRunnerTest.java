package br.com.wishlist.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@CucumberOptions(features = "src/test/resources/casetests")
@RunWith(Cucumber.class)
public class JunitRunnerTest {
}
