package org.southy.rl.eventhandler;

import org.southy.rl.Engine;
import org.southy.rl.asciipanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public interface EventHandler extends Serializable {

    void handleEvents(KeyEvent event);

    default void onRender(AsciiPanel panel) {
        engine().render(panel);
    }

    Engine engine();

}
