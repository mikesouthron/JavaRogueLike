package org.southy.rl;

import org.southy.rl.asciipanel.AsciiPanel;

import java.awt.*;
import java.util.ArrayList;

public class Logger {

    static class LogEntry {
        final String message;
        final Color fg;
        int count = 1;

        public LogEntry(String message, Color fg) {
            this.message = message;
            this.fg = fg;
        }

        String fullText() {
            if (count > 1) {
                return message + " x" + count;
            } else {
                return message;
            }
        }
    }

    public final java.util.List<LogEntry> log = new ArrayList<>();

    public void addMessage(String message, Color fg) {
        addMessage(message, fg, false);
    }

    public void addMessage(String message, Color fg, boolean stack) {
        if (stack && log.size() > 0 && log.get(log.size() - 1).message.equals(message)) {
            log.get(log.size() - 1).count++;
            return;
        }

        log.add(new LogEntry(message, fg));
    }

    public void render(AsciiPanel panel, int x, int y, int width, int height) {
        renderMessages(panel, x, y, width, height, this.log);
    }

    private static java.util.List<String> wrap(String str, int width) {
        if (str.length() < width) {
            return java.util.List.of(str);
        }
        var list = new ArrayList<String>();
        var words = str.split(" ");
        var line = "";
        for (String word : words) {
            var newLine = line.length() > 0 ? (line + " ") + word : word;
            if (newLine.length() > width) {
                list.add(line);
                line = word;
            } else {
                line = newLine;
            }
        }
        if (line.length() > 0) {
            list.add(line);
        }
        return list;
    }

    public static void renderMessages(AsciiPanel panel, int x, int y, int width, int height, java.util.List<LogEntry> messages) {
        var yOffset = height - 1;
        for (int i = messages.size() - 1; i >= 0; i--) {
            var msg = messages.get(i);
            var lines = wrap(msg.fullText(), width);
            for (int j = lines.size() - 1; j >= 0; j--) {
                panel.write(lines.get(j), x, y + yOffset, msg.fg);
                yOffset--;
                if (yOffset < 0) {
                    return;
                }
            }
        }
    }
}
