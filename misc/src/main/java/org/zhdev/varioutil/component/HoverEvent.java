package org.zhdev.varioutil.component;

public enum HoverEvent {
    SHOW_TEXT("show_text"),
    SHOW_ITEM("show_item"),
    SHOW_ENTITY("show_entity");

    private final String value;

    HoverEvent(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
