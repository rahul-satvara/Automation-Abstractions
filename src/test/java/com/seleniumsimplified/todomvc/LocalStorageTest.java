package com.seleniumsimplified.todomvc;


import com.seleniumsimplified.selenium.support.html5.Storage;
import com.seleniumsimplified.todomvc.localstorage.TodoMvcLocalStorage;
import com.seleniumsimplified.todomvc.page.pojo.ApplicationPage;
import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.html5.LocalStorage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class LocalStorageTest {


    private WebDriver driver;
    private TodoMVCSite todoMVCSite;
    private ApplicationPage todoMVC;

    @Before
    public void setup(){
        driver = new FirefoxDriver();
        todoMVCSite = new TodoMVCSite();

        todoMVC = new ApplicationPage(driver, todoMVCSite);
        todoMVC.get();
    }

    @Test
    public void updatesLocalStorageInitiallyUglyCode(){

        // initially code for backbone
        // the spec is loosely interpreted by different frameworks
        // so this will not be generic
        // todos-[framework]

        String storage_namespace = "todos-" + todoMVCSite.getName();
        LocalStorage storage = new Storage((JavascriptExecutor)driver);

        int initialSize = 0;
        String[] keys;

        try{
            keys = storage.getItem(storage_namespace).split(",");
            initialSize = keys.length;
        }catch(NullPointerException e){
            // the key might not exist
        }

        todoMVC.enterNewToDo("First Added Item");

        keys = storage.getItem(storage_namespace).split(",");
        int newSize = keys.length;

        assertThat(newSize, greaterThan(initialSize));

        boolean foundit = false;

        // has title
        keys = storage.getItem(storage_namespace).split(",");
        for(String key : keys){
            String itemKey = storage_namespace + "-" + key;
            String item = storage.getItem(itemKey);
            // should really parse json
            if(item.contains("\"" + "First Added Item" + "\""))
                foundit = true;
        }

        assertThat(foundit, is(true));
    }

    @Test
    public void updatesLocalStorageRefactored(){


        // I'm not that comfortable with adding driver to the todoMVCsite,
        // but done it here for expediency, can refactor later
        TodoMvcLocalStorage todoStorage = todoMVCSite.getLocalStorage(driver);

        assertThat(todoStorage.length(), is(0L));

        todoMVC.enterNewToDo("First Added Item");

        assertThat(todoStorage.length(), is(1L));

        assertThat(todoStorage.containsTitle("First Added Item"), is(true));

        todoMVC.enterNewToDo("Next Added Item");
        assertThat(todoStorage.length(), is(2L));

        todoMVC.enterNewToDo("Third Added Item");
        assertThat(todoStorage.length(), is(3L));
    }

    @After
    public void teardown(){

        driver.close();
        driver.quit();
    }
}
