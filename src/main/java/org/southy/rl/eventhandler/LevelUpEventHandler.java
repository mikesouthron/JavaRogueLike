package org.southy.rl.eventhandler;

import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.exceptions.Impossible;
import org.southy.sdl.SDL;

public class LevelUpEventHandler implements EventHandler {

    private static class Stats {
        int strength;
        int agility;
        int constitution;
        int intelligence;

        public Stats(int strength, int agility, int constitution, int intelligence) {
            this.strength = strength;
            this.agility = agility;
            this.constitution = constitution;
            this.intelligence = intelligence;
        }
    }

    Engine engine;

    int selectedIdx;

    int pointsToAssign;

    Stats startingStats;

    Stats newStats;

    public LevelUpEventHandler(Engine engine, int pointsToAssign) {
        this.engine = engine;
        this.pointsToAssign = pointsToAssign;
        startingStats = new Stats(engine.player.fighter.strength, engine.player.fighter.agility, engine.player.fighter.constitution, engine.player.fighter.intelligence);
        newStats = new Stats(engine.player.fighter.strength, engine.player.fighter.agility, engine.player.fighter.constitution, engine.player.fighter.intelligence);
    }

    @Override
    public void handleEvents(SDL sdl) throws Impossible {
        if (sdl == null) {
            return;
        }
        var keyEvent = sdl.SDLGetEvent();
        if (keyEvent == null) {
            return;
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            selectedIdx--;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            selectedIdx++;
        }

        if (selectedIdx < 0) {
            selectedIdx = 4;
        }
        if (selectedIdx >= 5) {
            selectedIdx = 0;
        }


        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (pointsToAssign > 0) {
                switch (selectedIdx) {
                    case 0:
                        newStats.strength++;
                        pointsToAssign--;
                        break;
                    case 1:
                        newStats.agility++;
                        pointsToAssign--;
                        break;
                    case 2:
                        newStats.constitution++;
                        pointsToAssign--;
                        break;
                    case 3:
                        newStats.intelligence++;
                        pointsToAssign--;
                        break;
                    default:
                }
            }
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            switch (selectedIdx) {
                case 0:
                    if (newStats.strength > startingStats.strength) {
                        newStats.strength--;
                        pointsToAssign++;
                    }
                    break;
                case 1:
                    if (newStats.agility > startingStats.agility) {
                        newStats.agility--;
                        pointsToAssign++;
                    }
                    break;
                case 2:
                    if (newStats.constitution > startingStats.constitution) {
                        newStats.constitution--;
                        pointsToAssign++;
                    }
                    break;
                case 3:
                    if (newStats.intelligence > startingStats.intelligence) {
                        newStats.intelligence--;
                        pointsToAssign++;
                    }
                    break;
                default:
            }
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selectedIdx == 4 && pointsToAssign == 0) {
                engine.player.fighter.strength = newStats.strength;
                engine.player.fighter.agility = newStats.agility;
                engine.player.fighter.constitution = newStats.constitution;
                engine.player.fighter.intelligence = newStats.intelligence;
                engine().gameWorld.generateFloor();
                engine().eventHandler = new MainGameEventHandler(engine);
                engine.updateFov();
            }
        }
    }

    static String LEVEL_UP_TEXT = "You feel more powerful, upgrade your stats!";

    @Override
    public void onRender(SDL sdl) {
        int yOffset = sdl.getHeightInCharacters() / 2 - sdl.getHeightInCharacters() / 4;

        sdl.write(LEVEL_UP_TEXT, sdl.getWidthInCharacters() / 2 - LEVEL_UP_TEXT.length() / 2, yOffset, ColorUtils.DESCEND);

        sdl.write("Strength", sdl.getWidthInCharacters() / 2 - 15, yOffset + 2, ColorUtils.DESCEND);
        sdl.write(String.valueOf(newStats.strength), sdl.getWidthInCharacters() / 2, yOffset + 2, ColorUtils.DESCEND, selectedIdx == 0 ? ColorUtils.CYAN : sdl.getDefaultBackgroundColor());
        sdl.write("Agility", sdl.getWidthInCharacters() / 2 - 15, yOffset + 3, ColorUtils.DESCEND);
        sdl.write(String.valueOf(newStats.agility), sdl.getWidthInCharacters() / 2, yOffset + 3, ColorUtils.DESCEND, selectedIdx == 1 ? ColorUtils.CYAN : sdl.getDefaultBackgroundColor());
        sdl.write("Constitution", sdl.getWidthInCharacters() / 2 - 15, yOffset + 4, ColorUtils.DESCEND);
        sdl.write(String.valueOf(newStats.constitution), sdl.getWidthInCharacters() / 2, yOffset + 4, ColorUtils.DESCEND, selectedIdx == 2 ? ColorUtils.CYAN : sdl.getDefaultBackgroundColor());
        sdl.write("Intelligence", sdl.getWidthInCharacters() / 2 - 15, yOffset + 5, ColorUtils.DESCEND);
        sdl.write(String.valueOf(newStats.intelligence), sdl.getWidthInCharacters() / 2, yOffset + 5, ColorUtils.DESCEND, selectedIdx == 3 ? ColorUtils.CYAN : sdl.getDefaultBackgroundColor());


        String doneText = "Done";
        if (selectedIdx == 4) {
            doneText = "* Done *";
        }
        sdl.write(doneText, sdl.getWidthInCharacters() / 2 - 10, yOffset + 6, ColorUtils.DESCEND);
    }

    @Override
    public Engine engine() {
        return engine;
    }

}
