package org.southy.rl;

import java.util.ArrayList;

public class Logger {

    static class LogEntry {
        final String message;
        final Long timestamp;

        public LogEntry(String message, Long timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }

        public void print() {
            System.out.println(message);
        }
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final java.util.List<LogEntry> log = new ArrayList<>();

    public void log(String message) {
        var entry = new LogEntry(message, System.currentTimeMillis());
        log.add(entry);
        entry.print();
    }

}
