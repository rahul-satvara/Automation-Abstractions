package com.seleniumsimplified.todomvc.page.functionalvsstructural;

import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import static com.seleniumsimplified.todomvc.page.functionalvsstructural.StructuralEnums.Filter.*;
import static com.seleniumsimplified.todomvc.page.functionalvsstructural.StructuralEnums.ItemsInState.*;

public class ApplicationPageFunctional {

    // switch between different structural implementations
    private ApplicationPageStructural structure;
    //private ApplicationPageStructuralFactory structure;
    //private ApplicationPageStructuralLoadable structure;
    //private ApplicationPageStructuralSlowLoadable structure;

    public ApplicationPageFunctional(WebDriver driver, TodoMVCSite todoMVCSite) {

        structure = new ApplicationPageStructural(driver, todoMVCSite);
        //structure = new ApplicationPageStructuralFactory(driver, todoMVCSite);
        //structure = new ApplicationPageStructuralLoadable(driver, todoMVCSite);
        //structure = new ApplicationPageStructuralSlowLoadable(driver, todoMVCSite);

    }

    public int getCountOfTodoDoItems() {
        return structure.getCountOfTodo(VISIBLE);
    }

    public int getCountOfCompletedTodoDoItems() {
        return structure.getCountOfTodo(VISIBLE_COMPLETED);
    }

    public int getCountOfActiveTodoDoItems() {
        return structure.getCountOfTodo(VISIBLE_ACTIVE);
    }

    public String getLastToDoIext() {
        return structure.getToDoText(getCountOfTodoDoItems()-1);
    }

    public void enterNewToDo(String todoTitle) {
        // could be Keys.ENTER on 2.40.0 and below
        // but needs to be Keys.RETURN on 2.41.0
        structure.typeIntoNewToDo(todoTitle, Keys.RETURN);
    }

    public void get() {
        structure.get();
    }

    public boolean isFooterVisible() {
        // catch any not found exceptions at functional layer
        try{
            return structure.isFooterVisible();
        }catch(NoSuchElementException e){
            return false;
        }
    }

    public boolean isMainVisible() {
        try{
            return structure.isMainVisible();
        }catch(NoSuchElementException e){
            return false;
        }
    }

    public void deleteLastToDo() {
        structure.deleteTodoItem(getCountOfTodoDoItems()-1);
    }


    public void editLastItem(String editTheTitleTo) {
        structure.editItem(getCountOfTodoDoItems()-1,editTheTitleTo);
    }


    public Integer getCountInFooter() {
        try{
            return structure.getCountInFooter();
        }catch(NoSuchElementException e){
            return 0;
        }
    }

    public String getCountTextInFooter() {

        String countText = "";
        try{
            countText = structure.getCountTextInFooter();
        }catch(NoSuchElementException e){

        }

        return countText;
    }

    public void filterOnAll() {
        structure.clickOnFilter(ALL);
    }

    public void filterOnActive() {
        structure.clickOnFilter(ACTIVE);
    }

    public void filterOnCompleted() {
        structure.clickOnFilter(COMPLETED);
    }

    public void toggleCompletionOfLastItem() {
        structure.toggleCompletionOfItem(getCountOfTodoDoItems() - 1);
    }

    public boolean isClearCompletedVisible() {
        return structure.isClearCompletedVisible();
    }

    public Integer getClearCompletedCount() {

        return structure.getClearCompletedCount();
    }

    public void clearCompleted() {
        structure.clickClearCompleted();
    }
}
