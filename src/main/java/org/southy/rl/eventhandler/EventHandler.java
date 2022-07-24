package org.southy.rl.eventhandler;

import org.southy.rl.Engine;
import org.southy.rl.exceptions.Impossible;
import org.southy.sdl.SDL;

import java.io.Serializable;

public interface EventHandler extends Serializable {

    void handleEvents(SDL sdl) throws Impossible;

    default void onRender(SDL sdl) {
        engine().render(sdl);
    }

    Engine engine();

}