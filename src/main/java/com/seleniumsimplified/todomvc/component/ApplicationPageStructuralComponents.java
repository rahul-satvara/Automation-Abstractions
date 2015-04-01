package com.seleniumsimplified.todomvc.component;

import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.seleniumsimplified.todomvc.page.functionalvsstructural.StructuralEnums.ItemsInState;
import com.seleniumsimplified.todomvc.page.functionalvsstructural.StructuralEnums.Filter;

import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a 'structural' page object which uses components
 * This is partially complete as an 'example' and the only component is a VisibleToDoEntry
 *
 * Where possible methods delegate to the VisibleToDoEntry component
 */
public class ApplicationPageStructuralComponents {

    private static final By CLEAR_COMPLETED = By.id("clear-completed");


    private final WebDriver driver;
    private final TodoMVCSite todoMVCSite;
    private final WebDriverWait wait;

    public ApplicationPageStructuralComponents(WebDriver driver, TodoMVCSite todoMVCSite) {
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

    public int getCountOfTodo(ItemsInState state) {
        return getTodoItems(state).size();
    }

    public VisibleToDoEntry getToDoEntryAt(int itemIndex){
        List<WebElement> items = getTodoItems(ItemsInState.VISIBLE);
        return new VisibleToDoEntry(driver, items.get(itemIndex));
    }

    public String getToDoText(int itemIndex) {
        return getToDoEntryAt(itemIndex).getText();
    }

    public List<WebElement> getTodoItems(ItemsInState state) {
        return driver.findElements(By.cssSelector(state.cssSelector()));
    }


    public void typeIntoNewToDo(CharSequence... keysToSend) {
        WebElement createTodo = driver.findElement(By.id("new-todo"));
        createTodo.click();
        createTodo.sendKeys(keysToSend);
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

    public void deleteTodoItem(int todoIndex) {
        getToDoEntryAt(todoIndex).delete();
    }

    public void editItem(int itemIndex, String editTheTitleTo) {
        getToDoEntryAt(itemIndex).edit(editTheTitleTo);
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

    public void clickOnFilter(Filter filter) {
        List<WebElement> filters = driver.findElements(By.cssSelector("#filters li"));
        filters.get(filter.index()).click();
    }

    public void toggleCompletionOfItem(int itemIndex) {
        getToDoEntryAt(itemIndex).toggle();
    }

    public boolean isClearCompletedVisible() {
        // it may or may not be in the dom
        List<WebElement> clearCompleted = driver.findElements(CLEAR_COMPLETED);
        if(clearCompleted.size()==0){return false;}

        return clearCompleted.get(0).isDisplayed();
    }

    public Integer getClearCompletedCount() {
        Integer clearCompletedCount=0;

        if(isClearCompletedVisible()){
            WebElement clearCompletedButton = driver.findElement(CLEAR_COMPLETED);
            String clearCompletedText = clearCompletedButton.getText();
            Pattern completedText = Pattern.compile("Clear completed \\((.+)\\)");
            Matcher matcher = completedText.matcher(clearCompletedText);

            if(matcher.matches()){
                return Integer.valueOf(matcher.group(1));
            }
        }

        return clearCompletedCount;
    }

    public void clickClearCompleted() {
        WebElement clearCompletedButton = driver.findElement(CLEAR_COMPLETED);
        clearCompletedButton.click();
    }

}
