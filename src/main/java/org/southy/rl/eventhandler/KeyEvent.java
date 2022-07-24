package org.southy.rl.eventhandler;

public class KeyEvent {

    public static int VK_UP = 1073741906;
    public static int VK_DOWN = 1073741905;
    public static int VK_LEFT = 1073741904;
    public static int VK_RIGHT = 1073741903;
    public static int VK_NUMPAD1 = 1073741913;
    public static int VK_NUMPAD2 = 1073741914;
    public static int VK_NUMPAD3 = 1073741915;
    public static int VK_NUMPAD4 = 1073741916;
    public static int VK_NUMPAD5 = 1073741917;
    public static int VK_NUMPAD6 = 1073741918;
    public static int VK_NUMPAD7 = 1073741919;
    public static int VK_NUMPAD8 = 1073741920;
    public static int VK_NUMPAD9 = 1073741921;
    public static int VK_PAGE_DOWN = 1073741902;
    public static int VK_END = 1073741901;
    public static int VK_HOME = 1073741898;
    public static int VK_PAGE_UP = 1073741899;
    public static int VK_ESCAPE = 27;
    public static int VK_ENTER = 13;

    public static int VK_KP_ENTER = 1073741912;

    public static int VK_A = 'a';
    public static int VK_B = 'b';
    public static int VK_C = 'c';
    public static int VK_D = 'd';
    public static int VK_E = 'e';
    public static int VK_F = 'f';
    public static int VK_G = 'g';
    public static int VK_H = 'h';
    public static int VK_I = 'i';
    public static int VK_J = 'j';
    public static int VK_K = 'k';
    public static int VK_L = 'l';
    public static int VK_M = 'm';
    public static int VK_N = 'n';
    public static int VK_O = 'o';
    public static int VK_P = 'p';
    public static int VK_Q = 'q';
    public static int VK_R = 'r';
    public static int VK_S = 's';
    public static int VK_T = 't';
    public static int VK_U = 'u';
    public static int VK_V = 'v';
    public static int VK_X = 'x';
    public static int VK_Y = 'y';
    public static int VK_Z = 'z';

    public static int VK_COMMA = ',';
    public static int VK_PERIOD = '.';
    public static int VK_CLEAR = -1;

    boolean keyUp = false;
    boolean keyDown = false;

    int keyCode;

    char keyChar;

    int keyMod;

    public boolean isKeyUp() {
        return keyUp;
    }

    public void setKeyUp(boolean keyUp) {
        this.keyUp = keyUp;
    }

    public boolean isKeyDown() {
        return keyDown;
    }

    public void setKeyDown(boolean keyDown) {
        this.keyDown = keyDown;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public char getKeyChar() {
        return (char)keyCode;
    }

    public void setKeyChar(char keyChar) {
        this.keyChar = keyChar;
    }


    public void setKeyMod(int keyMod) {
        this.keyMod = keyMod;
    }

    static int SHIFT_MASK = 0x0001 | 0x0002;

    static int CTRL_MASK = 0x0040 | 0x0080;

    public int getKeyMod() {
        return keyMod;
    }

    public boolean isShiftDown() {
        return (keyMod & SHIFT_MASK) > 0;
    }

    public boolean isControlDown() {
        return (keyMod & CTRL_MASK) > 0;
    }

}

