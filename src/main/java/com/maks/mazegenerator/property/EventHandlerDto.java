package com.maks.mazegenerator.property;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public record EventHandlerDto(EventHandler<MouseEvent> clickOnPaneHandler) {
}
