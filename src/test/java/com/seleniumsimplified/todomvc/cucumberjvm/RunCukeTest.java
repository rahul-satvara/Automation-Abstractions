package com.seleniumsimplified.todomvc.cucumberjvm;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format={"pretty", "html:target/cucumber"})
public class RunCukeTest {
}
