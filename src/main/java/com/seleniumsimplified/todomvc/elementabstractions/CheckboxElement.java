package com.seleniumsimplified.todomvc.elementabstractions;

import org.openqa.selenium.WebElement;

public class CheckboxElement implements Checkbox{

    private final WebElement webelement;

    public CheckboxElement(WebElement element){
        webelement = element;
    }

    @Override
    public boolean isChecked() {
        return webelement.isSelected();
    }

    @Override
    public Checkbox check() {
        if(!isChecked()){
            toggle();
        }
        return this;
    }

    @Override
    public Checkbox uncheck() {
        if(isChecked()){
            toggle();
        }
        return this;
    }

    @Override
    public Checkbox toggle() {
        webelement.click();
        return this;
    }

    public WebElement getWebElement() {
        return webelement;
    }
}
