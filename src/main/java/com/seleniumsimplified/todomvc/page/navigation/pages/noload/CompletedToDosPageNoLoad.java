package com.seleniumsimplified.todomvc.page.navigation.pages.noload;

import com.seleniumsimplified.todomvc.page.slowloadablecomponent.ApplicationPageStructuralSlowLoadable;
import com.seleniumsimplified.todomvc.site.TodoMVCSite;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.SlowLoadableComponent;
import org.openqa.selenium.support.ui.SystemClock;

public class CompletedToDosPageNoLoad extends SlowLoadableComponent<AllToDosPageNoLoad> {

    private final WebDriver driver;
    private final TodoMVCSite site;
    private final ApplicationPageStructuralSlowLoadable page;

    public CompletedToDosPageNoLoad(WebDriver driver, TodoMVCSite site){
        super(new SystemClock(), 10);
        this.driver = driver;
        this.site = site;

        // delegate to the main page
        // normally we would do this for components, but since this is a
        // single page app, the 'pages' are somewhat artificial and this
        // avoids duplicated code
        page = new ApplicationPageStructuralSlowLoadable(driver, site);
    }

    @Override
    protected void load() {
        // assume we navigate to this page
    }

    @Override
    protected void isLoaded() throws Error {

        // are the components loaded?
        page.isLoaded();

        try{
            if(!page.getSelectedFilterText().contentEquals("Completed")){
                throw new Error("Selected Filter is not Completed");
            }
        }catch(Exception e){
            throw new Error("Completed ToDos Page Not loaded " + e.getMessage());
        }
    }

    public ApplicationPageStructuralSlowLoadable body(){
        return page;
    }
}
