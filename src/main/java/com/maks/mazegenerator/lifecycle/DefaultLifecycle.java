package com.maks.mazegenerator.lifecycle;

public class DefaultLifecycle implements Lifecycle {
    private final LifecycleEventListenerRegistry registry;
    private Event event;

    public DefaultLifecycle(LifecycleEventListenerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void registerEvent(Event event) {
        this.event = event;
        registry.notifyAllListeners(event);
    }

    @Override
    public Event getCurrentEvent() {
        return event;
    }

    @Override
    public LifecycleEventListenerRegistry getEventListenerRegistry() {
        return registry;
    }
}
