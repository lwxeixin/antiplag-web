package it.zielke.moji;

public enum Stage {
    DISCONNECTED,
    AWAITING_INITIALIZATION,
    AWAITING_LANGUAGE,
    AWAITING_FILES,
    AWAITING_QUERY,
    AWAITING_RESULTS,
    AWAITING_END;

    Stage() {
    }
}
