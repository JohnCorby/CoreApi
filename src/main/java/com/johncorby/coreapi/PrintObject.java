package com.johncorby.coreapi;

import java.util.ArrayList;
import java.util.List;

import static com.johncorby.coreapi.util.MessageHandler.MessageType.*;
import static com.johncorby.coreapi.util.MessageHandler.logP;

public interface PrintObject {
    default void info(Object... msgs) {
        logP(INFO, toString(), msgs);
    }

    default void warn(Object... msgs) {
        logP(WARN, toString(), msgs);
    }

    default void error(Object... msgs) {
        logP(ERROR, toString(), msgs);
    }

    default void debug(Object... msgs) {
        logP(DEBUG, toString(), msgs);
    }

    default List<String> getDebug() {
        List<String> r = new ArrayList<>();
        r.add(toString());
        return r;
    }
}
