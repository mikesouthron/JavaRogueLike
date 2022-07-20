package org.southy.rl.eventhandler;

import org.southy.rl.Engine;
import org.southy.rl.asciipanel.AsciiPanel;

import java.awt.event.KeyEvent;

public interface EventHandler {

    void handleEvents(KeyEvent event);

    default void onRender(AsciiPanel panel) {
        engine().render(panel);
    }

    Engine engine();

}
