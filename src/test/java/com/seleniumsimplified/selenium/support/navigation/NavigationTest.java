package com.seleniumsimplified.selenium.support.navigation;


import com.seleniumsimplified.todomvc.domain.actors.TodoMVCUser;
import com.seleniumsimplified.todomvc.navigation.TodoMVCNav;
import com.seleniumsimplified.todomvc.page.fluent.ApplicationPageFunctionalFluent;
import com.seleniumsimplified.todomvc.page.navigation.pages.get.ActiveToDosPage;
import com.seleniumsimplified.todomvc.page.navigation.pages.get.AllToDosPage;
import com.seleniumsimplified.todomvc.page.navigation.pages.get.CompletedToDosPage;
import com.seleniumsimplified.todomvc.page.navigation.pages.noload.ActiveToDosPageNoLoad;
import com.seleniumsimplified.todomvc.page.navigation.pages.noload.AllToDosPageNoLoad;
import com.seleniumsimplified.todomvc.page.navigation.pages.noload.CompletedToDosPageNoLoad;
import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static com.seleniumsimplified.todomvc.navigation.TodoMVCNav.Pages.*;
import static com.seleniumsimplified.todomvc.page.functionalvsstructural.StructuralEnums.Filter.*;

public class NavigationTest {

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

        TodoMVCUser user = new TodoMVCUser(driver, todoMVCSite);

        user.
                createNewToDo("do this").
                and().
                createNewToDo("do that").
                and().
                createNewToDo("do the next thing").
                and().
                createNewToDo("and this");

        user.
                completesToDo("do that").
                and().
                completesToDo("and this");

        todoMVC.filterOnAll();
    }


    // direct navigation will only work through a server, not direct file access
    @Test
    public void navigateDirectlyFromPageObjectGet(){

        // navigate directly from page objects
        // ignores system navigation flow
        // simulates url manipulation by user

        // expect the All ToDos Page to Load
        AllToDosPage allPage = new AllToDosPage(driver, todoMVCSite);
        allPage.get();

        // expect the Active ToDos Page to Load
        ActiveToDosPage activePage = new ActiveToDosPage(driver, todoMVCSite);
        activePage.get();

        // expect the Completed ToDos Page to Load
        CompletedToDosPage completedPage = new CompletedToDosPage(driver, todoMVCSite);
        completedPage.get();

        allPage.get();
    }

    @Test
    public void navigateAsSideEffectsWithSlowLoadableComponent(){

        // Sometimes we don't want to navigate to the page because it happens with
        // normal usage flow. But we still want to synchronise on the page loading.
        // easy to do out the box with SlowLoadableComponent by leaving the load blank
        // but then we can't do the above tests

         // no direct navigation, but check that correct pages have loaded after
        // the methods that trigger page load have finished
        // harder to do with existing support classes in webdriver since a page is 'getable' or not
        // and synchronisation is on the 'getable' pages, not the loading method
        // we get around that with the PublicLoadableComponentWaiter here, but really a
        // different type of loadable component would be better for these type of pages
        // i.e. a page we can 'get' and a page we can 'waitTillReady'


        // expect the All ToDos Page to Load
        AllToDosPage allPage = new AllToDosPage(driver, todoMVCSite);
        WaitUntil.page(allPage).hasLoaded();

        // perform an action that causes a navigation to occur
        allPage.body().clickOnFilter(ACTIVE);

        // expect the Active ToDos Page to Load
        ActiveToDosPage activePage = new ActiveToDosPage(driver, todoMVCSite);
        WaitUntil.page(activePage).hasLoaded();

        // perform an action that causes a navigation to occur
        allPage.body().clickOnFilter(COMPLETED);

        // expect the Completed ToDos Page to Load
        CompletedToDosPage completedPage = new CompletedToDosPage(driver, todoMVCSite);
        WaitUntil.page(completedPage).hasLoaded();

        completedPage.body().clickOnFilter(ALL);
        WaitUntil.page(allPage).hasLoaded();
    }

    @Test
    public void navigateAsSideEffectsWithSlowLoadableComponentWithoutDriverGet(){

        // SlowloadableComponent without a load method implementing a driver.get
        // means we use it for synchronisation but not 'get'
        // although we do it through the 'get' method

        // expect the All ToDos Page to Load
        AllToDosPageNoLoad allPage = new AllToDosPageNoLoad(driver, todoMVCSite);
        allPage.get();

        // perform an action that causes a navigation to occur
        allPage.body().clickOnFilter(ACTIVE);

        // expect the Active ToDos Page to Load
        ActiveToDosPageNoLoad activePage = new ActiveToDosPageNoLoad(driver, todoMVCSite);
        activePage.get();

        // perform an action that causes a navigation to occur
        allPage.body().clickOnFilter(COMPLETED);

        // expect the Completed ToDos Page to Load
        CompletedToDosPageNoLoad completedPage = new CompletedToDosPageNoLoad(driver, todoMVCSite);
        completedPage.get();

        completedPage.body().clickOnFilter(ALL);
        allPage.get();
    }

    // direct navigation will only work through a server, not direct file access
    @Test
    public void directNavigationObjectsForSlowLoadableComponentsWithoutDriverGet(){

        TodoMVCNav navigate = new TodoMVCNav(driver, todoMVCSite);

        navigate.open(ALL_TODOS_PAGE);

        // expect the All ToDos Page to Load
        AllToDosPageNoLoad allPage = new AllToDosPageNoLoad(driver, todoMVCSite);
        allPage.get();

        navigate.open(ACTIVE_TODOS_PAGE);

        // expect the Active ToDos Page to Load
        ActiveToDosPageNoLoad activePage = new ActiveToDosPageNoLoad(driver, todoMVCSite);
        activePage.get();

        navigate.open(COMPLETED_TODOS_PAGE);

        // expect the Completed ToDos Page to Load
        CompletedToDosPageNoLoad completedPage = new CompletedToDosPageNoLoad(driver, todoMVCSite);
        completedPage.get();

        navigate.open(ALL_TODOS_PAGE);
        allPage.get();
    }

    @After
    public void teardown(){
        driver.close();
        driver.quit();
    }
}
