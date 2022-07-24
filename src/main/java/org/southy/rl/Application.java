package org.southy.rl;

import org.southy.rl.exceptions.Impossible;
import org.southy.sdl.SDL;

@SuppressWarnings("BusyWait")
public class Application {

    public static final int screenWidth = 100;
    public static final int screenHeight = 60;

    public static final int mapWidth = 120;

    public static final int mapHeight = 86;

    public static final int roomMaxSize = 10;
    public static final int roomMinSize = 6;
    public static final int maxRooms = 30;

    public static final int maxMonstersPerRoom = 2;
    public static final int maxItemsPerRoom = 2;

    public SDL sdl;

    public Application() {
        sdl = new SDL();
        sdl.SDLInit(screenWidth * 16, screenHeight * 16);
    }

    public static Engine engine;
    public static Camera normalCamera = new Camera();
    public static Camera fullMapCamera = new Camera();
    public static Camera camera = normalCamera;

    @SuppressWarnings("InfiniteLoopStatement")
    public void execute() throws InterruptedException {
        fullMapCamera.height = screenHeight * 2;
        fullMapCamera.width = screenWidth * 2;
        engine = new Engine();

        while (true) {
            try {
                engine.eventHandler.handleEvents(sdl);
            } catch (Impossible e) {
                engine.logger.addMessage(e.getMessage(), ColorUtils.IMPOSSIBLE, true);
            }
            sdl.SDLClear();
            engine.eventHandler.onRender(sdl);
            sdl.SDLPaint();
            if (engine.fastMove != null) {
                Thread.sleep(10);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var app = new Application();
        app.execute();
    }

}
