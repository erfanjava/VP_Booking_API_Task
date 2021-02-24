package com.vp.bookingApp.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(

        features = "src/test/resources/features",
        glue = "com/vp/bookingApp/step_definitions",
        dryRun = false,
        plugin ={
                "rerun:target/rerun.txt",
                "json:target/cucumber.json",
                "html:target/default-cucumber-reports"

        }

)

public class CukeRunner {
}
