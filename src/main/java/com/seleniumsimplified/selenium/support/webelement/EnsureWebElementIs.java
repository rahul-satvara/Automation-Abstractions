package com.seleniumsimplified.selenium.support.webelement;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Some utility methods working with WebElements
 */
public class EnsureWebElementIs {

    /**
     * because click didn't scroll up on Mac Firefox -
     * added a scroll to y location method called inViewOnThePage
     * which scrolls to the element's y position
      */

    public static void inViewOnThePage(WebDriver driver, WebElement todoListItem) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + todoListItem.getLocation().getY() + ")");
    }
}
