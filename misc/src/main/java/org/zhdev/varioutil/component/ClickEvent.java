package org.zhdev.varioutil.component;

public enum ClickEvent {
    OPEN_URL("open_url"),
    RUN_COMMAND("run_command"),
    SUGGEST_COMMAND("suggest_command"),
    CHANGE_PAGE("change_page"),
    COPY_TO_CLIPBOARD("copy_to_clipboard");

    private final String value;

    ClickEvent(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
