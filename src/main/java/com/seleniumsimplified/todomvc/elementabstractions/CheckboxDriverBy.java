package com.seleniumsimplified.todomvc.elementabstractions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckboxDriverBy implements Checkbox{

    private final WebDriver driver;
    private final By by;
    private CheckboxElement element;

    public CheckboxDriverBy(WebDriver driver, By by){
        this.driver = driver;
        this.by = by;
    }

    @Override
    public boolean isChecked() {
        return getElement().isChecked();
    }

    @Override
    public Checkbox check() {
        getElement().check();
        return this;
    }

    @Override
    public Checkbox uncheck() {
        getElement().uncheck();
        return this;
    }

    @Override
    public Checkbox toggle() {
        getElement().toggle();
        return this;
    }

    public CheckboxElement getElement() {
        return new CheckboxElement(driver.findElement(by));
    }

    public WebElement getWebElement(){
        return getElement().getWebElement();
    }
}
