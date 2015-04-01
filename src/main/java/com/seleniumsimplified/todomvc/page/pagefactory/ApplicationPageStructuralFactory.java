package com.seleniumsimplified.todomvc.page.pagefactory;

import com.seleniumsimplified.selenium.support.webelement.EnsureWebElementIs;
import com.seleniumsimplified.todomvc.page.functionalvsstructural.StructuralEnums.ItemsInState;
import com.seleniumsimplified.todomvc.page.functionalvsstructural.StructuralEnums.Filter;
import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationPageStructuralFactory {



    @FindBy(how= How.CSS, using="ul#todo-list li:not(.hidden)")
    public List<WebElement> visibleTodoItems;

    @FindBy(how= How.CSS, using="ul#todo-list li.completed:not(.hidden)")
    public List<WebElement> visibleCompletedTodoItems;

    @FindBy(how= How.CSS, using="ul#todo-list li:not(.completed):not(.hidden)")
    public List<WebElement> visibleActiveTodoItems;

    @FindBy(how = How.ID, using="new-todo")
    private WebElement createTodo;

    @FindBy(how = How.ID, using="footer")
    private WebElement footer;

    @FindBy(how = How.ID, using="main")
    private WebElement main;

    @FindBy(how = How.CSS, using="#todo-count strong")
    private WebElement countElementStrong;

    @FindBy(how = How.CSS, using="#todo-count")
    private WebElement countElement;

    @FindBy(how = How.CSS, using="#filters li a")
    List<WebElement> filters;

    @FindBy(how = How.ID, using="clear-completed")
    List<WebElement> clearCompletedAsList;

    @FindBy(how = How.ID, using="clear-completed")
    WebElement clearCompletedButton;

    private final By DESTROY_BUTTON = By.cssSelector("button.destroy");
    private final By EDIT_FIELD = By.cssSelector("input.edit");
    private final By INPUT_TOGGLE = By.cssSelector("input.toggle");




    private final WebDriver driver;
    private final TodoMVCSite todoMVCSite;
    private final WebDriverWait wait;

    public ApplicationPageStructuralFactory(WebDriver driver, TodoMVCSite todoMVCSite) {

        PageFactory.initElements(driver, this);

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

    public String getToDoText(int itemIndex) {
        java.util.List<WebElement> items = getTodoItems(ItemsInState.VISIBLE);
        return items.get(itemIndex).getText();
    }

    public java.util.List<WebElement> getTodoItems(ItemsInState state) {
        List<WebElement> returnList = null;

        switch (state){
            case VISIBLE:
                returnList = visibleTodoItems;
                break;

            case VISIBLE_COMPLETED:
                returnList = visibleCompletedTodoItems;
                break;

            case VISIBLE_ACTIVE:
                returnList = visibleActiveTodoItems;
                break;
        }

        return returnList;
    }

    public void typeIntoNewToDo(CharSequence... keysToSend) {
        createTodo.click();
        createTodo.sendKeys(keysToSend);
    }

    public void get() {
        driver.get(todoMVCSite.getURL());
    }

    public boolean isFooterVisible() {
        return footer.isDisplayed();
    }

    public boolean isMainVisible() {
        return main.isDisplayed();
    }

    public void deleteTodoItem(int todoIndex) {

        WebElement todoListItem = visibleTodoItems.get(todoIndex);

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

        // because this is relative to a dynamically populate item it can't be declared
        // as an @FindBy
        WebElement destroyButton = todoListItem.findElement(DESTROY_BUTTON);
        wait.until(ExpectedConditions.elementToBeClickable(destroyButton));

        destroyButton.click();
    }

    public void editItem(int itemIndex, String editTheTitleTo) {
        WebElement todoListItem = visibleTodoItems.get(itemIndex);
        wait.until(ExpectedConditions.elementToBeClickable(todoListItem));

        // have no choice but to use actions here
        new Actions(driver).doubleClick(todoListItem.findElement(By.cssSelector("div > label"))).perform();

        WebElement editfield = todoListItem.findElement(EDIT_FIELD);
        wait.until(ExpectedConditions.elementToBeClickable(editfield));
        editfield.click();
        editfield.clear();
        editfield.sendKeys(editTheTitleTo);
        editfield.sendKeys(Keys.ENTER);
    }


    public Integer getCountInFooter() {
        return Integer.valueOf(countElementStrong.getText());
    }

    public String getCountTextInFooter() {
        String countText = countElement.getText();

        // remove the number from the string
        return countText.replace(getCountInFooter() + " ", "");
    }

    public void clickOnFilter(Filter filter) {
        filters.get(filter.index()).click();
    }

    public void toggleCompletionOfItem(int itemIndex) {

        WebElement todoListItem = visibleTodoItems.get(itemIndex);
        wait.until(ExpectedConditions.elementToBeClickable(todoListItem));

        WebElement checkbox = todoListItem.findElement(INPUT_TOGGLE);
        checkbox.click();
    }

    public boolean isClearCompletedVisible() {
        // it may or may not be in the dom
        if(clearCompletedAsList.size()==0){return false;}

        return clearCompletedAsList.get(0).isDisplayed();
    }

    public Integer getClearCompletedCount() {
        Integer clearCompletedCount=0;

        if(isClearCompletedVisible()){
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
        clearCompletedButton.click();
    }
}
