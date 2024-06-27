package com.maks.mazegenerator.view;

import com.maks.mazegenerator.lifecycle.DefaultLifecycle;
import com.maks.mazegenerator.lifecycle.DefaultLifecycleEventListenerRegistry;
import com.maks.mazegenerator.lifecycle.Lifecycle;
import javafx.stage.Stage;

public abstract class AbstractView {
    protected Stage stage;
    protected Lifecycle lifecycle;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    protected void initLifecycle() {
        lifecycle = new DefaultLifecycle(new DefaultLifecycleEventListenerRegistry());
    }

    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    abstract void initViewModel();
}
