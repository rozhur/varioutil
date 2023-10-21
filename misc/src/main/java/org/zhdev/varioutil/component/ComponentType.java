package org.zhdev.varioutil.component;

public enum ComponentType {
    TEXT("text"),
    TRANSLATE("translate"),
    KEYBIND("keybind"),
    SCORE("score"),
    SELECTOR("selector");

    private final String key;

    ComponentType(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}