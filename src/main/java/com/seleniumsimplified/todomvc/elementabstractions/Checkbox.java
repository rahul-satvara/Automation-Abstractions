package com.seleniumsimplified.todomvc.elementabstractions;

public interface Checkbox {

    public boolean isChecked();
    public Checkbox check();
    public Checkbox uncheck();
    public Checkbox toggle();
}
