package com.seleniumsimplified.todomvc.site;


import com.seleniumsimplified.todomvc.localstorage.TodoMvcLocalStorage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class TodoMVCSite {

    public String getURL(){
        return "http://todomvc.com/architecture-examples/backbone/";
        // local on mac via virtualHostX
        //return "http://todomvc.site/architecture-examples/backbone/";
        // local on pc
        //return "file:///C:/Users/Alan/Downloads/todomvc-f57e0b773db14f094ef09274af90042f83328412/todomvc-f57e0b773db14f094ef09274af90042f83328412/architecture-examples/backbone/index.html";
    }

    public String getName(){
        return "backbone";
    }

    public TodoMvcLocalStorage getLocalStorage(WebDriver driver) {
        return new BackBoneTodoMVCLocalStorage("todos-" + getName(), (JavascriptExecutor) driver);
    }
}
