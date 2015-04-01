package com.seleniumsimplified.todomvc.page.fluent;

import com.seleniumsimplified.todomvc.page.functionalvsstructural.ApplicationPageFunctional;
import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.openqa.selenium.WebDriver;

public class ApplicationPageFunctionalFluent {

    private ApplicationPageFunctional functional;

    // for the purposes of this example, I have chosen to delegate out to the existing
    // functional abstraction, and simply make the calls fluent in return with additional methods
    // the differences can be seen in the fluent tests
    public ApplicationPageFunctionalFluent(WebDriver driver, TodoMVCSite todoMVCSite) {
        functional = new ApplicationPageFunctional(driver, todoMVCSite);
    }

    public int getCountOfTodoDoItems() {
        return functional.getCountOfTodoDoItems();
    }

    public int getCountOfCompletedTodoDoItems() {
        return functional.getCountOfCompletedTodoDoItems();
    }

    public int getCountOfActiveTodoDoItems() {
        return functional.getCountOfActiveTodoDoItems();
    }

    public String getLastToDoIext() {
        return functional.getLastToDoIext();
    }

    public ApplicationPageFunctionalFluent enterNewToDo(String todoTitle) {
        functional.enterNewToDo(todoTitle);
        return this;
    }

    public ApplicationPageFunctionalFluent get() {
        functional.get();
        return this;
    }

    public boolean isFooterVisible() {
        return functional.isFooterVisible();
    }

    public boolean isMainVisible() {
        return functional.isMainVisible();
    }

    public ApplicationPageFunctionalFluent deleteLastToDo() {
        functional.deleteLastToDo();
        return this;
    }

    public ApplicationPageFunctionalFluent editLastItem(String editTheTitleTo) {
        functional.editLastItem(editTheTitleTo);
        return this;
    }


    public Integer getCountInFooter() {
        return functional.getCountInFooter();
    }

    public String getCountTextInFooter() {
        return functional.getCountTextInFooter();
    }

    public ApplicationPageFunctionalFluent filterOnAll() {
        functional.filterOnAll();
        return this;
    }

    public ApplicationPageFunctionalFluent filterOnActive() {
        functional.filterOnActive();
        return this;
    }

    public ApplicationPageFunctionalFluent filterOnCompleted() {
        functional.filterOnCompleted();
        return this;
    }

    public ApplicationPageFunctionalFluent toggleCompletionOfLastItem() {
        functional.toggleCompletionOfLastItem();
        return this;
    }

    public boolean isClearCompletedVisible() {
        return functional.isClearCompletedVisible();
    }

    public Integer getClearCompletedCount() {
        return functional.getClearCompletedCount();
    }

    public ApplicationPageFunctionalFluent clearCompleted() {
        functional.clearCompleted();
        return this;
    }

    public ApplicationPageFunctionalFluent and(){
        return this;
    }

    public ApplicationPageFunctionalFluent then(){
        return this;
    }

    public ApplicationPageFunctionalFluent also(){
        return this;
    }
}

