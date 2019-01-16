package com.on.jobs.publica;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty",
        "html:target/cucumber",
        "json:target/cucumber.json"},
        features = "src/test/resources")
public class CucumberIntegrationTest {
}
