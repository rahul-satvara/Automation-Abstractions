package com.seleniumsimplified.todomvc;

import com.seleniumsimplified.todomvc.page.pojo.ApplicationPage;
import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

// Tests were written against the live demo version of
// https://github.com/tastejs/todomvc/tree/f57e0b773db14f094ef09274af90042f83328412/architecture-examples/backbone
public class SequentialCreationOfTest {

    private WebDriver driver;
    private TodoMVCSite todoMVCSite;

    // comment, uncomment to use the different implementations
    // could go further and create factory and interface but leave like this for now
    // This was the first set of tests created, to create the first abstraction layer ApplicationPage
    // This was subsequently split into functional and structural pages
    private ApplicationPage todoMVC;
    //private ApplicationPageFunctional todoMVC;

    @Before
    public void setup(){
        driver = new FirefoxDriver();
        todoMVCSite = new TodoMVCSite();

        todoMVC = new ApplicationPage(driver, todoMVCSite);
        //todoMVC = new ApplicationPageFunctional(driver, todoMVCSite);
        todoMVC.get();
    }

    @Test
    public void canCreateAToDo(){

        int originalNumberOfTodos = todoMVC.getCountOfTodoDoItems();

        todoMVC.enterNewToDo("new task");

        int newToDos = todoMVC.getCountOfTodoDoItems();

        assertThat(newToDos, greaterThan(originalNumberOfTodos));
        assertThat(newToDos, is(originalNumberOfTodos + 1));

        assertThat("new task", is(todoMVC.getLastToDoIext()));
    }

    @Test
    public void newTodoItemsAreTrimmed(){

        int originalNumberOfTodos = todoMVC.getCountOfTodoDoItems();

        todoMVC.enterNewToDo("    trimmed task    ");

        int newToDos = todoMVC.getCountOfTodoDoItems();

        assertThat(newToDos, greaterThan(originalNumberOfTodos));
        assertThat(newToDos, is(originalNumberOfTodos + 1));

        assertThat("trimmed task", is(todoMVC.getLastToDoIext()));
    }


    @Test
    public void cannotCreateEmptyTask(){

        int originalNumberOfTodos = todoMVC.getCountOfTodoDoItems();

        todoMVC.enterNewToDo("        ");

        int newToDos = todoMVC.getCountOfTodoDoItems();

        assertThat(newToDos, is(originalNumberOfTodos));
    }

    @Test
    public void whenNoToDosOnLoadThenMainAndFooterAreHidden(){

        // since we are closing and starting the browser on
        // each test we don't have to worry about local storage yet

        assertThat(todoMVC.isFooterVisible(), is(false));
        assertThat(todoMVC.isMainVisible(), is(false));
    }


    @Test
    public void whenMainAndFooterAreVisibleAfterCreatingAToDo(){
        todoMVC.enterNewToDo("Display Main and Footer");

        assertThat(todoMVC.isFooterVisible(), is(true));
        assertThat(todoMVC.isMainVisible(), is(true));
    }

    // TODO: check for main and footer after deleting all the items

    @Test
    public void canDeleteATodo(){

        int originalNumberOfTodos = todoMVC.getCountOfTodoDoItems();

        todoMVC.enterNewToDo("Delete Me");

        int addedATodoCount = todoMVC.getCountOfTodoDoItems();

        assertThat(addedATodoCount, is(originalNumberOfTodos + 1));

        assertThat(todoMVC.isFooterVisible(), is(true));
        assertThat(todoMVC.isMainVisible(), is(true));

        todoMVC.deleteLastToDo();

        int afterDeleteCount = todoMVC.getCountOfTodoDoItems();
        assertThat(afterDeleteCount, is(originalNumberOfTodos));

        assertThat(todoMVC.isFooterVisible(), is(false));
        assertThat(todoMVC.isMainVisible(), is(false));
    }


    @Test
    public void canDeleteAllTodos(){

        int originalNumberOfTodos = todoMVC.getCountOfTodoDoItems();
        int create_X_ToDos = 10;

        for(int createIndex = 0; createIndex<create_X_ToDos; createIndex++){

            todoMVC.enterNewToDo("Delete Me " + createIndex);

            int addedATodoCount = todoMVC.getCountOfTodoDoItems();

            assertThat(addedATodoCount, is(originalNumberOfTodos + createIndex + 1));
        }

        int currentTodoCount = todoMVC.getCountOfTodoDoItems();
        assertThat(currentTodoCount, is(originalNumberOfTodos + create_X_ToDos));

        // now delete them all
        while(currentTodoCount>0){
            todoMVC.deleteLastToDo();
            currentTodoCount--;
        }

        currentTodoCount = todoMVC.getCountOfTodoDoItems();
        assertThat(currentTodoCount, is(0));

        assertThat(todoMVC.isFooterVisible(), is(false));
        assertThat(todoMVC.isMainVisible(), is(false));
    }

    @Test
    public void canEditAnItem(){

        int originalNumberOfTodos = todoMVC.getCountOfTodoDoItems();

        todoMVC.enterNewToDo("Edit Me");

        int addedATodoCount = todoMVC.getCountOfTodoDoItems();

        assertThat(addedATodoCount, is(originalNumberOfTodos + 1));

        todoMVC.editLastItem("Edited Me");

        int editedATodoCount = todoMVC.getCountOfTodoDoItems();

        assertThat(editedATodoCount, is(originalNumberOfTodos + 1));

        assertThat("Edited Me", is(todoMVC.getLastToDoIext()));

    }

    @Test
    public void countInFooterIs(){

        // spec mentions 0 items, but this is never shown so can't check for it
        // assertThat(todoMVC.getCountInFooter(), is(0));
        // assertThat(todoMVC.getCountTextInFooter(), is("items left"));

        todoMVC.enterNewToDo("First Item");
        assertThat(todoMVC.getCountInFooter(), is(1));
        assertThat(todoMVC.getCountTextInFooter(), is("item left"));

        todoMVC.enterNewToDo("Second Item");
        assertThat(todoMVC.getCountInFooter(), is(2));
        assertThat(todoMVC.getCountTextInFooter(), is("items left"));

        todoMVC.enterNewToDo("Third Item");
        assertThat(todoMVC.getCountInFooter(), is(3));
        assertThat(todoMVC.getCountTextInFooter(), is("items left"));
    }

    @Test
    public void canUseTheFilters(){
        todoMVC.enterNewToDo("Need at least one");

        todoMVC.filterOnActive();
        assertThat(driver.getCurrentUrl(), endsWith("#/active"));

        todoMVC.filterOnCompleted();
        assertThat(driver.getCurrentUrl(), endsWith("#/completed"));

        todoMVC.filterOnAll();
        assertThat(driver.getCurrentUrl(), endsWith("#/"));
    }

    @Test
    public void filtersLimitTheListItemsDisplayed(){
        todoMVC.enterNewToDo("First Added Item");
        todoMVC.enterNewToDo("Second Added Item");
        todoMVC.enterNewToDo("Third Added Item");

        int currentCount = todoMVC.getCountOfTodoDoItems();
        assertThat(currentCount, is(3));

        todoMVC.filterOnCompleted();

        int completedCount = todoMVC.getCountOfTodoDoItems();
        assertThat(completedCount, is(0));

        todoMVC.filterOnAll();

        int allCount = todoMVC.getCountOfTodoDoItems();
        assertThat(allCount, is(3));
    }

    @Test
    public void canMarkAnItemCompleted(){
        todoMVC.enterNewToDo("First Added Item");
        todoMVC.enterNewToDo("Second Added Item");
        todoMVC.enterNewToDo("Third Added Item");

        assertThat(todoMVC.getCountOfTodoDoItems(), is(3));

        todoMVC.toggleCompletionOfLastItem();

        assertThat(todoMVC.getCountOfTodoDoItems(), is(3));
        assertThat(todoMVC.getCountOfCompletedTodoDoItems(), is(1));
        assertThat(todoMVC.getCountOfActiveTodoDoItems(), is(2));

        todoMVC.filterOnCompleted();

        assertThat(todoMVC.getCountOfTodoDoItems(), is(1));

        todoMVC.toggleCompletionOfLastItem();
        assertThat(todoMVC.getCountOfTodoDoItems(), is(0));

        todoMVC.filterOnActive();
        assertThat(todoMVC.getCountOfTodoDoItems(), is(3));
        assertThat(todoMVC.getCountOfCompletedTodoDoItems(), is(0));
        assertThat(todoMVC.getCountOfActiveTodoDoItems(), is(3));
    }

    @Test
    public void accessCountInCompletedItemsFooter(){
        todoMVC.enterNewToDo("First Added Item");
        todoMVC.enterNewToDo("Second Added Item");

        assertThat(todoMVC.isClearCompletedVisible(), is(false));

        todoMVC.toggleCompletionOfLastItem();

        assertThat(todoMVC.isClearCompletedVisible(), is(true));
        assertThat(todoMVC.getClearCompletedCount(), is(1));
    }

    @Test
    public void canClearCompletedItems(){
        todoMVC.enterNewToDo("First Added Item");
        todoMVC.enterNewToDo("Second Added Item");
        todoMVC.enterNewToDo("Third Added Item");

        assertThat(todoMVC.isClearCompletedVisible(), is(false));

        todoMVC.toggleCompletionOfLastItem();

        assertThat(todoMVC.isClearCompletedVisible(), is(true));
        assertThat(todoMVC.getClearCompletedCount(), is(1));
        assertThat(todoMVC.getCountOfCompletedTodoDoItems(), is(1));

        todoMVC.enterNewToDo("Fourth Added Item");
        todoMVC.toggleCompletionOfLastItem();
        assertThat(todoMVC.getClearCompletedCount(), is(2));
        assertThat(todoMVC.getCountOfCompletedTodoDoItems(), is(2));

        todoMVC.clearCompleted();

        assertThat(todoMVC.isClearCompletedVisible(), is(false));
        assertThat(todoMVC.getClearCompletedCount(), is(0));
        assertThat(todoMVC.getCountOfCompletedTodoDoItems(), is(0));
    }

    @After
    public void teardown(){

        driver.close();
        driver.quit();
    }
}
