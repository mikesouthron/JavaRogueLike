package org.southy.rl;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.util.List;

public class Engine {

    List<Entity> entities;
    Entity player;

    Logger logger;

    GameMap gameMap;

    public Engine(List<Entity> entities, Entity player, GameMap gameMap, Logger logger) {
        this.entities = entities;
        this.player = player;
        this.logger = logger;
        this.gameMap = gameMap;
    }

    public void handleEvent(KeyEvent event) {
        var option = EventHandler.keyDown(event, logger);
        option.ifPresent(action -> action.perform(this, player));
    }

    public void render(AsciiPanel panel) {
        panel.clear();
        gameMap.render(panel);
        entities.forEach(e -> panel.write(e.str, e.x, e.y, e.fg, e.bg));
        panel.repaint();
    }
}
