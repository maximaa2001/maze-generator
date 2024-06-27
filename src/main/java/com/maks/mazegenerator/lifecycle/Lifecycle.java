package com.maks.mazegenerator.lifecycle;

public interface Lifecycle {
    void registerEvent(Event event);
    Event getCurrentEvent();
    LifecycleEventListenerRegistry getEventListenerRegistry();
}
