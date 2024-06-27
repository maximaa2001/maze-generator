package com.maks.mazegenerator.lifecycle;

import java.util.ArrayList;
import java.util.List;

public class DefaultLifecycleEventListenerRegistry implements LifecycleEventListenerRegistry {
    private static LifecycleEventListenerRegistry instance;
    private final List<LifecycleEventListener> storage = new ArrayList<>();

    @Override
    public synchronized void addListener(LifecycleEventListener listener) {
        storage.add(listener);
    }

    @Override
    public void notifyAllListeners(Event event) {
        storage.forEach(e -> e.onEvent(event));
    }
}
