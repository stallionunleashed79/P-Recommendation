package com.cat.digital.reco;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty","json:target/report.json",
        "de.monochromata.cucumber.report.PrettyReports:target/pretty-cucumber"},
        features = {"src/it/resources/features"})
public class ITCucumberRunner {

}
