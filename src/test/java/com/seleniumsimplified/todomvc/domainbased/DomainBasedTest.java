package com.seleniumsimplified.todomvc.domainbased;

import com.seleniumsimplified.todomvc.domain.objects.ToDoItem;
import com.seleniumsimplified.todomvc.domain.objects.ToDoList;
import com.seleniumsimplified.todomvc.domain.actors.TodoMVCUser;
import com.seleniumsimplified.todomvc.page.functionalvsstructural.ApplicationPageFunctional;
import com.seleniumsimplified.todomvc.page.functionalvsstructural.ApplicationPageStructural;
import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * This setups domain objects, and uses those in the expected results and tests
 */
public class DomainBasedTest {

    private WebDriver driver;
    private TodoMVCSite todoMVCSite;

    // comment, uncomment to use the different implementations
    // could go further and create factory and interface but leave like this for now
    //private ApplicationPage todoMVC;
    private ApplicationPageFunctional todoMVC;

    @Before
    public void setup(){
        driver = new FirefoxDriver();
        todoMVCSite = new TodoMVCSite();

        //todoMVC = new ApplicationPage(driver, todoMVCSite);
        todoMVC = new ApplicationPageFunctional(driver, todoMVCSite);
        todoMVC.get();
    }

    @Test
    public void canCreateAToDo(){

        ToDoItem todo = new ToDoItem("New Task");

        int originalNumberOfTodos = todoMVC.getCountOfTodoDoItems();

        todoMVC.enterNewToDo(todo.getText());

        int newToDos = todoMVC.getCountOfTodoDoItems();

        assertThat(newToDos, greaterThan(originalNumberOfTodos));
        assertThat(newToDos, is(originalNumberOfTodos + 1));

        assertThat(todo.getText(), is(todoMVC.getLastToDoIext()));
    }

    // domain objects enforce constraints so can't use them here
    // but we have multiple logical levels so use a lower one
    @Test
    public void cannotCreateEmptyTask(){

        int originalNumberOfTodos = todoMVC.getCountOfTodoDoItems();

        todoMVC.enterNewToDo("        ");

        int newToDos = todoMVC.getCountOfTodoDoItems();

        assertThat(newToDos, is(originalNumberOfTodos));
    }

    // Need to keep domain and physical world in sync
    @Test
    public void canDeleteATodo(){

        int originalNumberOfTodos = todoMVC.getCountOfTodoDoItems();

        ToDoList list = new ToDoList();
        list.addNewToDoItem("Delete Me");

        todoMVC.enterNewToDo(list.getItemAtPosition(0).getText());

        int addedATodoCount = todoMVC.getCountOfTodoDoItems();

        assertThat(addedATodoCount, is(originalNumberOfTodos + 1));

        assertThat(todoMVC.isFooterVisible(), is(true));
        assertThat(todoMVC.isMainVisible(), is(true));

        todoMVC.deleteLastToDo();
        list.deleteItemAtPosition(0);

        int afterDeleteCount = todoMVC.getCountOfTodoDoItems();
        assertThat(afterDeleteCount, is(originalNumberOfTodos));

        assertThat(todoMVC.isFooterVisible(), is(false));
        assertThat(todoMVC.isMainVisible(), is(false));
    }


//    Given a user creates 10 todo items
//    When the user deletes 1
//    Then the GUI shows it as missing
    @Test
    public void canDeleteATodoItemFromAList(){

        TodoMVCUser user = new TodoMVCUser(driver, todoMVCSite);

        int create_X_ToDos = 10;
        int todoToDelete = 5;

        //    Given a user creates 10 todo items
        for(int createIndex = 0; createIndex<create_X_ToDos; createIndex++){
            user.createNewToDo("Delete Me " + createIndex);
        }

        assertThat(user.getTodoList().size(), is(create_X_ToDos));
        assertThat(todoMVC.getCountOfTodoDoItems(), is(user.getTodoList().size()));

        //    When the user deletes 1
        user.deleteToDoAt(todoToDelete);

        // Then the GUI shows it as missing
        assertThat(todoMVC.getCountOfTodoDoItems(), is(user.getTodoList().size()));

        ApplicationPageStructural displayedToDos = new ApplicationPageStructural(driver, todoMVCSite);
        for(int position = 0; position < user.getTodoList().size(); position++){

            assertThat(
                    displayedToDos.getToDoText(position),
                    is(user.getTodoList().getItemAtPosition(position).getText())
                    );
        }
    }


    @After
    public void teardown(){

        driver.close();
        driver.quit();
    }
}
