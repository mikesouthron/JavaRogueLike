package org.southy.rl;

import org.southy.rl.exceptions.Impossible;

public interface Action {
    boolean perform() throws Impossible;
}
