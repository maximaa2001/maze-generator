package com.maks.mazegenerator.view;

import com.maks.mazegenerator.lifecycle.DefaultLifecycle;
import com.maks.mazegenerator.lifecycle.DefaultLifecycleEventListenerRegistry;
import com.maks.mazegenerator.lifecycle.Lifecycle;
import com.maks.mazegenerator.lifecycle.LifecycleProvider;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class AbstractView {
    protected Stage stage;
    protected Scene scene;
    protected Lifecycle lifecycle;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        LifecycleProvider.getInstance().addLifecycle(scene, lifecycle);
    }

    protected void initLifecycle() {
        lifecycle = new DefaultLifecycle(new DefaultLifecycleEventListenerRegistry());
    }

    abstract void initViewModel();
}
