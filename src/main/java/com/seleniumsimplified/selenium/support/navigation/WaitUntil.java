package com.seleniumsimplified.selenium.support.navigation;

import org.openqa.selenium.support.ui.LoadableComponent;

public class WaitUntil {

    /**
     * Static method to create a PublicLoadableComponentWaiter which Waits until a loadable component hasLoaded
     * this is just a bit of syntactic sugar so we can write WaitUntil(aPage).hasLoaded();
     * @param aPage
     * @return
     */
    public static PublicLoadableComponentWaiter page(LoadableComponent aPage){
        return new PublicLoadableComponentWaiter(aPage);
    }
}
