package org.southy.sdl;

import org.southy.rl.Color;
import org.southy.rl.ColorUtils;
import org.southy.rl.eventhandler.KeyEvent;

public class SDL {

    static {
        System.loadLibrary("JavaSDL");
    }

    public void write(String text, int x, int y, Color fg, Color bg) {
        SDLWriteText(text, x, y, fg, bg);
    }

    public void write(String text, int x, int y, Color fg) {
        write(text, x, y, fg, getDefaultBackgroundColor());
    }

    public void write(String text, int x, int y) {
        write(text, x, y, getDefaultForegroundColor(), getDefaultBackgroundColor());
    }

    public void write(char ch, int x, int y, Color fg, Color bg) {
        write(String.valueOf(ch), x, y, fg, bg);
    }

    public void write(char ch, int x, int y, Color fg) {
        write(ch, x, y, fg, getDefaultBackgroundColor());
    }

    public void write(char ch, int x, int y) {
        write(ch, x, y, getDefaultForegroundColor(), getDefaultBackgroundColor());
    }

    public int getWidthInCharacters() {
        return 100;
    }

    public int getHeightInCharacters() {
        return 60;
    }

    public Color getDefaultForegroundColor() {
        return ColorUtils.WHITE;
    }

    public Color getDefaultBackgroundColor() {
        return ColorUtils.BLACK;
    }

    public native void SDLInit(int width, int height);
    public native void SDLClear();

    public native void SDLWriteText(String text, int x, int y, Color fg, Color bg);

    public native void SDLPaint();

    public native KeyEvent SDLGetEvent();

}
