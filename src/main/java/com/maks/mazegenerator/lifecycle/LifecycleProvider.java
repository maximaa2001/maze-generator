package com.maks.mazegenerator.lifecycle;

import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public final class LifecycleProvider {
    private static LifecycleProvider instance;
    private final Map<Scene, Lifecycle> lifecycleStore = new HashMap<>();

    private LifecycleProvider() {
    }

    public synchronized static LifecycleProvider getInstance() {
        if (instance == null) {
            instance = new LifecycleProvider();
        }
        return instance;
    }

    public synchronized void addLifecycle(Scene scene, Lifecycle lifecycle) {
        lifecycleStore.put(scene, lifecycle);
    }

    public synchronized void removeLifecycle(Scene scene) {
        lifecycleStore.remove(scene);
    }

    public Lifecycle getLifecycle(Scene scene) {
        return lifecycleStore.get(scene);
    }
}
