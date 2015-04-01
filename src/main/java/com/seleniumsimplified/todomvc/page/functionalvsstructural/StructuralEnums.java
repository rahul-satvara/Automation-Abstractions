package com.seleniumsimplified.todomvc.page.functionalvsstructural;

public class StructuralEnums {

    public static enum Filter {
        ALL(0), ACTIVE(1), COMPLETED(2);

        private int filterIndex;
        Filter(int index){
            this.filterIndex = index;
        }

        public int index(){
            return filterIndex;
        }
    }

    /**
     * The Structural Factory example doesn't need the enum constructor and this could have been...
     *     public static enum ItemsInState {
                VISIBLE,
                VISIBLE_COMPLETED,
                VISIBLE_ACTIVE;
          }
        for that example only
     */
    public static enum ItemsInState {
        VISIBLE("ul#todo-list li:not(.hidden)"),
        VISIBLE_COMPLETED("ul#todo-list li.completed:not(.hidden)"),
        VISIBLE_ACTIVE("ul#todo-list li:not(.completed):not(.hidden)");

        private String cssselector;

        ItemsInState(String cssselector) {
            this.cssselector = cssselector;
        }

        public String cssSelector(){
            return this.cssselector;
        }
    }

}


