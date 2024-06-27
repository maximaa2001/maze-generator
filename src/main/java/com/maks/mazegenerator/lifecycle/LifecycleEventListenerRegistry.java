package com.maks.mazegenerator.lifecycle;

public interface LifecycleEventListenerRegistry {
    void addListener(LifecycleEventListener listener);
    void notifyAllListeners(Event event);
}
