package com.seleniumsimplified.todomvc.domain.actors;


import com.seleniumsimplified.todomvc.domain.objects.ToDoList;
import com.seleniumsimplified.todomvc.page.functionalvsstructural.ApplicationPageFunctional;
import com.seleniumsimplified.todomvc.page.functionalvsstructural.ApplicationPageStructural;
import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.openqa.selenium.WebDriver;

/**
 * A high level user object which wraps 'user' functionality making a user focussed DSL without
 * bringing in Cucumber
 * It uses lower level abstractions as appropriate e.g. structural and functional pages
 */
public class TodoMVCUser {
    private final WebDriver driver;
    private final TodoMVCSite site;
    private final ToDoList todoList;


    public TodoMVCUser(WebDriver driver, TodoMVCSite todoMVCSite) {
        this.driver = driver;
        this.site = todoMVCSite;

        this.todoList = new ToDoList();
    }

    public TodoMVCUser opensApplication() {
        driver.get(site.getURL());
        return this;
    }

    public TodoMVCUser createNewToDo(String toDoText) {
        todoList.addNewToDoItem(toDoText);

        ApplicationPageFunctional page = new ApplicationPageFunctional(driver, site);

        page.enterNewToDo(toDoText);

        return this;
    }

    public ToDoList getTodoList() {
        return todoList;
    }

    public TodoMVCUser deleteToDoAt(int positionInList) {
        todoList.deleteItemAtPosition(positionInList);

        ApplicationPageStructural structural = new ApplicationPageStructural(driver, site);

        structural.deleteTodoItem(positionInList);

        return this;
    }

    public TodoMVCUser and(){
        return this;
    }

    public TodoMVCUser completesToDo(String toDoText) {
        int position = todoList.getPositionOf(toDoText);

        ApplicationPageStructural structural = new ApplicationPageStructural(driver, site);
        structural.toggleCompletionOfItem(position);

        return this;
    }
}
