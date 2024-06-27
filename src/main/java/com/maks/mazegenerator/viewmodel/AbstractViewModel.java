package com.maks.mazegenerator.viewmodel;

import com.maks.mazegenerator.lifecycle.Event;
import com.maks.mazegenerator.lifecycle.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractViewModel {
    private static final Logger logger = LoggerFactory.getLogger(AbstractViewModel.class);
    protected final Lifecycle lifecycle;

    public AbstractViewModel(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        createListener();
    }

    private void createListener() {
        lifecycle.getEventListenerRegistry().addListener(event -> {
            switch (event) {
                case START -> handleStart(event);
                case STOP -> handleStop(event);
            }
        });
    }

    protected void handleStart(Event event) {
        logger.debug(String.format("handle start event by %s", getViewModelId()));
    }

    protected void handleStop(Event event) {
        logger.debug(String.format("handle stop event by %s", getViewModelId()));
    }

    private String getViewModelId() {
        return getClass().getName();
    }
}
