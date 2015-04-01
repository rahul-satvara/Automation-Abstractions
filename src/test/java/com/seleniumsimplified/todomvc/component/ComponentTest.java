package com.seleniumsimplified.todomvc.component;

import com.seleniumsimplified.todomvc.page.functionalvsstructural.ApplicationPageFunctional;
import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ComponentTest {

    private WebDriver driver;
    private TodoMVCSite todoMVCSite;

    private ApplicationPageFunctional todoMVC;
    private ApplicationPageStructuralComponents page;

    @Before
    public void setup(){
        driver = new FirefoxDriver();
        todoMVCSite = new TodoMVCSite();

        todoMVC = new ApplicationPageFunctional(driver, todoMVCSite);
        page = new ApplicationPageStructuralComponents(driver, todoMVCSite);

        todoMVC.get();
    }

    @Test
    public void canMarkAnItemCompleted(){
        todoMVC.enterNewToDo("First Added Item");
        todoMVC.enterNewToDo("Second Added Item");
        todoMVC.enterNewToDo("Third Added Item");

        assertThat(todoMVC.getCountOfTodoDoItems(), is(3));

        page.getToDoEntryAt(todoMVC.getCountOfTodoDoItems()-1).
            markCompleted();

        assertThat(todoMVC.getCountOfTodoDoItems(), is(3));
        assertThat(todoMVC.getCountOfCompletedTodoDoItems(), is(1));
        assertThat(todoMVC.getCountOfActiveTodoDoItems(), is(2));

        todoMVC.filterOnCompleted();

        assertThat(todoMVC.getCountOfTodoDoItems(), is(1));

        page.getToDoEntryAt(todoMVC.getCountOfTodoDoItems()-1).
             markActive();

        assertThat(todoMVC.getCountOfTodoDoItems(), is(0));

        todoMVC.filterOnActive();
        assertThat(todoMVC.getCountOfTodoDoItems(), is(3));
        assertThat(todoMVC.getCountOfCompletedTodoDoItems(), is(0));
        assertThat(todoMVC.getCountOfActiveTodoDoItems(), is(3));
    }


    @After
    public void teardown(){

        driver.close();
        driver.quit();
    }
}
