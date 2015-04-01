package com.seleniumsimplified.todomvc.fluent;

import com.seleniumsimplified.todomvc.page.fluent.ApplicationPageFunctionalFluent;
import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SequentialCreationOfTestFluentSubset {

    private WebDriver driver;
    private TodoMVCSite todoMVCSite;

    private ApplicationPageFunctionalFluent todoMVC;

    @Before
    public void setup(){
        driver = new FirefoxDriver();
        todoMVCSite = new TodoMVCSite();

        //todoMVC = new ApplicationPage(driver, todoMVCSite);
        todoMVC = new ApplicationPageFunctionalFluent(driver, todoMVCSite);
        todoMVC.get();
    }

    @Test
    public void demonstrateFluentUse(){

        todoMVC.enterNewToDo("First Completed Item").
                and().
                toggleCompletionOfLastItem().
                then().
                    enterNewToDo("Still to do this").
                    and().
                    enterNewToDo("Still to do this too").
                then().
                filterOnCompleted();

        assertThat(todoMVC.getCountOfTodoDoItems(), is(1));

        todoMVC.filterOnActive();

        assertThat(todoMVC.getCountOfTodoDoItems(), is(2));
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

        int newToDos = todoMVC.enterNewToDo("    trimmed task    ").
                        and().
                        getCountOfTodoDoItems();

        assertThat(newToDos, greaterThan(originalNumberOfTodos));
        assertThat(newToDos, is(originalNumberOfTodos + 1));

        assertThat("trimmed task", is(todoMVC.getLastToDoIext()));
    }


    @Test
    public void cannotCreateEmptyTask(){

        int originalNumberOfTodos = todoMVC.getCountOfTodoDoItems();

        assertThat(
                    todoMVC.enterNewToDo("        ").
                    and().
                    getCountOfTodoDoItems(),
                    is(originalNumberOfTodos));
    }


    @Test
    public void canDeleteATodo(){

        int originalNumberOfTodos = todoMVC.getCountOfTodoDoItems();

        assertThat(
                todoMVC.enterNewToDo("Delete Me").
                        and().
                        getCountOfTodoDoItems(),
                is(originalNumberOfTodos + 1));

        assertThat(todoMVC.isFooterVisible(), is(true));
        assertThat(todoMVC.isMainVisible(), is(true));

        assertThat(
                todoMVC.deleteLastToDo().
                        and().
                        getCountOfTodoDoItems(),
                is(originalNumberOfTodos));

        assertThat(todoMVC.isFooterVisible(), is(false));
        assertThat(todoMVC.isMainVisible(), is(false));
    }


    @Test
    public void filtersLimitTheListItemsDisplayed(){

        todoMVC.enterNewToDo("First Added Item").
                and().
                enterNewToDo("Second Added Item").
                then().
                enterNewToDo("Third Added Item");

        assertThat(todoMVC.
                    getCountOfTodoDoItems(),
                    is(3));

        assertThat(todoMVC.
                    filterOnCompleted().
                    and().
                    getCountOfTodoDoItems(),
                    is(0));

        assertThat(todoMVC.
                    filterOnAll().
                    and().
                    getCountOfTodoDoItems(),
                    is(3));
    }



    @After
    public void teardown(){

        driver.close();
        driver.quit();
    }
}
