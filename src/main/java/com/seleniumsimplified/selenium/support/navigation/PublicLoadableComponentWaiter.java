package com.seleniumsimplified.selenium.support.navigation;

import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.SystemClock;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
    A wrapper around loadable component to have a hasLoaded method which does not throw errors
    and uses polling to check the loading in a similar way to the 'get' method on a loadable component
    but it does not 'load' the page
 */
public class PublicLoadableComponentWaiter {

    private final Clock clock;
    private final PublicIsLoadableComponent isLoadedPage;
    private final String componentName;
    public int pollingTimeWait = 100;
    public int timeoutInSeconds = 10;

    public PublicLoadableComponentWaiter(LoadableComponent aComponent) {
        clock = new SystemClock();
        this.componentName =  aComponent.getClass().getCanonicalName();
        this.isLoadedPage = new PublicIsLoadableComponent(aComponent);
    }

    public void hasLoaded() {
        // relies on isLoaded being public or accessible to this class
        // code is an amendment of SlowLoadableComponent get()
        // this code throws exception rather than error

        try{
            long end = clock.laterBy(SECONDS.toMillis(timeoutInSeconds));

            while (clock.isNowBefore(end)) {
                try {
                    isLoadedPage.isLoaded();
                    return;
                } catch ( ComponentLoadingException e){
                    // we will propogate this exception since there was a problem
                    throw new Exception(e);
                } catch (Error e) {
                    // Loadable components typically throw an error
                    // Not a problem here, we could still be loading
                } catch (Exception e){
                    // Ignore exceptions since we might still be loading
                }

                Thread.sleep(pollingTimeWait);
            }

            isLoadedPage.isLoaded();

        }catch(Error e){
            throw new ComponentLoadingException(componentName, e.getMessage());
        }catch(Exception e){
            throw new ComponentLoadingException(componentName, e.getMessage());
        }
    }

}
