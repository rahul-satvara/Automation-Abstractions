package com.seleniumsimplified.todomvc.page.pojo;

import com.seleniumsimplified.selenium.support.webelement.EnsureWebElementIs;
import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationPage {
    private final WebDriver driver;
    private final TodoMVCSite todoMVCSite;
    private final WebDriverWait wait;

    public ApplicationPage(WebDriver driver, TodoMVCSite todoMVCSite) {
        this.driver = driver;
        this.todoMVCSite = todoMVCSite;
        wait = new WebDriverWait(driver,10);

        // move the mouse out of the way so it
        // doesn't interfere with the test
        try {
            new Robot().mouseMove(0,0);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public int getCountOfTodoDoItems() {
        return getTodoItems().size();
    }

    public String getLastToDoIext() {
        List<WebElement> items = getTodoItems();
        return items.get(items.size()-1).getText();
    }

    private List<WebElement> getTodoItems() {
        // visible ones
        return driver.findElements(By.cssSelector("ul#todo-list li:not(.hidden)"));
    }

    public void enterNewToDo(String todoTitle) {
        WebElement createTodo = driver.findElement(By.id("new-todo"));
        createTodo.click();
        createTodo.sendKeys(todoTitle);
        createTodo.sendKeys(Keys.RETURN);
    }

    public void get() {
        driver.get(todoMVCSite.getURL());
    }

    public boolean isFooterVisible() {
        return driver.findElement(By.id("footer")).isDisplayed();
    }

    public boolean isMainVisible() {
        return driver.findElement(By.id("main")).isDisplayed();
    }

    public void deleteLastToDo() {
        List<WebElement> items = getTodoItems();
        WebElement todoListItem = items.get(items.size()-1);

        wait.until(ExpectedConditions.elementToBeClickable(todoListItem));

        // on my mac, sometimes this fails because
        // the default size of the window is small so
        // the element is off screen,
        // I used to do this with an extra todoListItem.click()
        // where the first click brings it on to screen
        // but by scrolling, the button is lost
        // I decided to use JavaScript to scroll it into view instead
        EnsureWebElementIs.inViewOnThePage(driver, todoListItem);

        todoListItem.click(); // enable the destroy button

        WebElement destroyButton = todoListItem.findElement(By.cssSelector("button.destroy"));
        wait.until(ExpectedConditions.elementToBeClickable(destroyButton));

        destroyButton.click();
    }


    public void editLastItem(String editTheTitleTo) {
        List<WebElement> items = getTodoItems();
        WebElement todoListItem = items.get(items.size()-1);
        wait.until(ExpectedConditions.elementToBeClickable(todoListItem));

        // have no choice but to use actions here
        new Actions(driver).doubleClick(todoListItem.findElement(By.cssSelector("div > label"))).perform();

        WebElement editfield = todoListItem.findElement(By.cssSelector("input.edit"));
        wait.until(ExpectedConditions.elementToBeClickable(editfield));
        editfield.click();
        editfield.clear();
        editfield.sendKeys(editTheTitleTo);
        editfield.sendKeys(Keys.ENTER);
    }


    public Integer getCountInFooter() {
        WebElement countElement = driver.findElement(By.cssSelector("#todo-count strong"));
        return Integer.valueOf(countElement.getText());
    }

    public String getCountTextInFooter() {
        WebElement countElement = driver.findElement(By.cssSelector("#todo-count"));
        String countText = countElement.getText();

        // remove the number from the string
        return countText.replace(getCountInFooter() + " ", "");
    }

    public void filterOnAll() {
        clickOnFilter(0);
    }

    public void filterOnActive() {
        clickOnFilter(1);
    }

    public void filterOnCompleted() {
        clickOnFilter(2);
    }

    private void clickOnFilter(int filterIndex) {
        List<WebElement> filters = driver.findElements(By.cssSelector("#filters li a"));
        filters.get(filterIndex).click();
    }

    public void toggleCompletionOfLastItem() {
        List<WebElement> items = getTodoItems();
        WebElement todoListItem = items.get(items.size()-1);
        wait.until(ExpectedConditions.elementToBeClickable(todoListItem));

        WebElement checkbox = todoListItem.findElement(By.cssSelector("input.toggle"));
        checkbox.click();
    }

    public int getCountOfCompletedTodoDoItems() {
        return driver.findElements(By.cssSelector("ul#todo-list li.completed:not(.hidden)")).size();
    }

    public int getCountOfActiveTodoDoItems() {
        return driver.findElements(By.cssSelector("ul#todo-list li:not(.completed):not(.hidden)")).size();
    }

    public boolean isClearCompletedVisible() {
        // it may or may not be in the dom
        List<WebElement> clearCompleted = driver.findElements(By.id("clear-completed"));
        if(clearCompleted.size()==0){return false;}

        return clearCompleted.get(0).isDisplayed();
    }

    public Integer getClearCompletedCount() {
        Integer clearCompletedCount=0;

        if(isClearCompletedVisible()){
            WebElement clearCompletedButton = driver.findElement(By.id("clear-completed"));
            String clearCompletedText = clearCompletedButton.getText();
            Pattern completedText = Pattern.compile("Clear completed \\((.+)\\)");
            Matcher matcher = completedText.matcher(clearCompletedText);

            if(matcher.matches()){
                return Integer.valueOf(matcher.group(1));
            }
        }

        return clearCompletedCount;
    }

    public void clearCompleted() {
        WebElement clearCompletedButton = driver.findElement(By.id("clear-completed"));
        clearCompletedButton.click();
    }
}
