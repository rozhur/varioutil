package org.zhdev.varioutil.component;

import org.zhdev.varioutil.color.ChatColor;

import java.util.*;

public final class Component {
    public static final Component EMPTY = new Component(ComponentType.TEXT, "");

    private final ComponentType type;

    private final String value;

    private final Boolean bold;
    private final Boolean italic;
    private final Boolean underlined;
    private final Boolean strikethrough;
    private final Boolean obfuscated;

    private final MinecraftFont font;
    private final ChatColor color;

    private final String insertion;

    private final ClickEvent clickEvent;
    private final String clickEventValue;

    private final HoverEvent hoverEvent;
    private final Component[] hoverEventValue;

    private final Component[] extra;
    private final Component[] with;

    public Component(ComponentType type, String value, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, MinecraftFont font, ChatColor color, String insertion, ClickEvent clickEvent, String clickEventValue, HoverEvent hoverEvent, Component[] hoverEventValue, Component[] extra, Component[] with) {
        this.type = type;
        this.value = value;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
        this.font = font;
        this.color = color;
        this.insertion = insertion;
        this.clickEvent = clickEvent;
        this.clickEventValue = clickEventValue;
        this.hoverEvent = hoverEvent;
        this.hoverEventValue = hoverEventValue;
        this.extra = extra;
        this.with = with;
    }

    public Component(String value, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, ChatColor color, ClickEvent clickEvent, String clickEventValue) {
        this(ComponentType.TEXT, value, bold, italic, underlined, strikethrough, obfuscated, null, color, null, clickEvent, clickEventValue, null, null, null, null);
    }

    public Component(String value, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, ChatColor color) {
        this(value, bold, italic, underlined, strikethrough, obfuscated, color, null, null);
    }

    public Component(ComponentType type, String value) {
        this(type, value, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public ComponentType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Boolean getBold() {
        return bold;
    }

    public Boolean getItalic() {
        return italic;
    }

    public Boolean getUnderlined() {
        return underlined;
    }

    public Boolean getStrikethrough() {
        return strikethrough;
    }

    public Boolean getObfuscated() {
        return obfuscated;
    }

    public MinecraftFont getFont() {
        return font;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getInsertion() {
        return insertion;
    }

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public String getClickEventValue() {
        return clickEventValue;
    }

    public HoverEvent getHoverEvent() {
        return hoverEvent;
    }

    public Component[] getHoverEventValue() {
        return hoverEventValue;
    }

    public Component[] getExtra() {
        return extra;
    }

    public Component[] getWith() {
        return with;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Component component = (Component) o;

        if (type != component.type) return false;
        if (!Objects.equals(value, component.value)) return false;
        if (!Objects.equals(bold, component.bold)) return false;
        if (!Objects.equals(italic, component.italic)) return false;
        if (!Objects.equals(underlined, component.underlined)) return false;
        if (!Objects.equals(strikethrough, component.strikethrough))
            return false;
        if (!Objects.equals(obfuscated, component.obfuscated)) return false;
        if (font != component.font) return false;
        if (!Objects.equals(color, component.color)) return false;
        if (!Objects.equals(insertion, component.insertion)) return false;
        if (clickEvent != component.clickEvent) return false;
        if (!Objects.equals(clickEventValue, component.clickEventValue))
            return false;
        if (hoverEvent != component.hoverEvent) return false;
        if (!Arrays.equals(hoverEventValue, component.hoverEventValue)) return false;
        if (!Arrays.equals(extra, component.extra)) return false;
        return Arrays.equals(with, component.with);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (bold != null ? bold.hashCode() : 0);
        result = 31 * result + (italic != null ? italic.hashCode() : 0);
        result = 31 * result + (underlined != null ? underlined.hashCode() : 0);
        result = 31 * result + (strikethrough != null ? strikethrough.hashCode() : 0);
        result = 31 * result + (obfuscated != null ? obfuscated.hashCode() : 0);
        result = 31 * result + (font != null ? font.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (insertion != null ? insertion.hashCode() : 0);
        result = 31 * result + (clickEvent != null ? clickEvent.hashCode() : 0);
        result = 31 * result + (clickEventValue != null ? clickEventValue.hashCode() : 0);
        result = 31 * result + (hoverEvent != null ? hoverEvent.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(hoverEventValue);
        result = 31 * result + Arrays.hashCode(extra);
        result = 31 * result + Arrays.hashCode(with);
        return result;
    }

    @Override
    public String toString() {
        return "Component{" + "type=" + type + ", value='" + value + "', bold=" + bold + ", italic=" + italic + ", underlined=" + underlined + ", strikethrough=" + strikethrough + ", obfuscated=" + obfuscated + ", font=" + font + ", color=" + color + ", insertion='" + insertion + "', clickEvent=" + clickEvent + ", clickEventValue='" + clickEventValue + "', hoverEvent=" + hoverEvent + ", hoverEventValue=" + Arrays.toString(hoverEventValue) + ", extra=" + Arrays.toString(extra) + ", with=" + Arrays.toString(with) + '}';
    }

    public static Builder of(String value) {
        return new Builder(value);
    }

    public static Builder textOf(String value) {
        return new Builder(value).type(ComponentType.TEXT);
    }

    public static Builder translateOf(String value) {
        return of(value).type(ComponentType.TRANSLATE);
    }

    public static Builder keybindOf(String value) {
        return of(value).type(ComponentType.KEYBIND);
    }

    public static Builder scoreOf(String value) {
        return of(value).type(ComponentType.SCORE);
    }

    public static Builder selectorOf(String value) {
        return of(value).type(ComponentType.SELECTOR);
    }

    public static final class Builder {
        private final List<Component> components;

        private ComponentType type = ComponentType.TEXT;
        private String value;
        private Boolean bold;
        private Boolean italic;
        private Boolean underlined;
        private Boolean strikethrough;
        private Boolean obfuscated;
        private MinecraftFont font;
        private ChatColor color;
        private String insertion;
        private ClickEvent clickEvent;
        private String clickEventValue;
        private HoverEvent hoverEvent;
        private Component[] hoverEventValue;
        private Component[] extra;
        private Component[] with;

        public Builder(String value, int initialCapacity) {
            this.components = new ArrayList<>(initialCapacity);
            this.value = value;
        }

        public Builder(String value) {
            this.components = new ArrayList<>();
            this.value = value;
        }

        public Builder type(ComponentType type) {
            this.type = type;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder text(String value) {
            this.type = ComponentType.TEXT;
            this.value = value;
            return this;
        }

        public Builder translate(String value) {
            this.type = ComponentType.TRANSLATE;
            this.value = value;
            return this;
        }

        public Builder keybind(String value) {
            this.type = ComponentType.KEYBIND;
            this.value = value;
            return this;
        }

        public Builder score(String value) {
            this.type = ComponentType.SCORE;
            this.value = value;
            return this;
        }

        public Builder selector(String value) {
            this.type = ComponentType.SELECTOR;
            this.value = value;
            return this;
        }

        public Builder bold(Boolean bold) {
            this.bold = bold;
            return this;
        }

        public Builder italic(Boolean italic) {
            this.italic = italic;
            return this;
        }

        public Builder underlined(Boolean underlined) {
            this.underlined = underlined;
            return this;
        }

        public Builder strikethrough(Boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }

        public Builder obfuscated(Boolean obfuscated) {
            this.obfuscated = obfuscated;
            return this;
        }

        public Builder font(MinecraftFont font) {
            this.font = font;
            return this;
        }

        public Builder color(ChatColor color) {
            this.color = color;
            return this;
        }

        public Builder insertion(String insertion) {
            this.insertion = insertion;
            return this;
        }

        public Builder click(ClickEvent clickEvent, String clickEventValue) {
            this.clickEvent = clickEvent;
            this.clickEventValue = clickEventValue;
            return this;
        }

        public Builder click(String clickEventValue) {
            return click(ClickEvent.OPEN_URL, clickEventValue);
        }

        public Builder hover(HoverEvent hoverEvent, Component... hoverEventValue) {
            this.hoverEvent = hoverEvent;
            this.hoverEventValue = hoverEventValue;
            return this;
        }

        public Builder hover(Component... hoverEventValue) {
            return hover(HoverEvent.SHOW_TEXT, hoverEventValue);
        }

        public Builder extra(Component... extra) {
            this.extra = extra;
            return this;
        }

        public Builder with(Component... with) {
            this.with = with;
            return this;
        }

        public Component create() {
            return new Component(type, value, bold, italic, underlined, strikethrough, obfuscated, font, color, insertion, clickEvent, clickEventValue, hoverEvent, hoverEventValue, extra, with);
        }

        public Builder push() {
            Component component = create();
            components.add(component);
            return this;
        }

        public void add(Component component) {
            this.components.add(component);
        }

        public void addAll(Collection<Component> components) {
            this.components.addAll(components);
        }

        public void addAll(Component... components) {
            Collections.addAll(this.components, components);
        }

        public Builder append(String value) {
            return push().value(value);
        }

        public Builder appendText(String value) {
            return push().type(ComponentType.TEXT).value(value);
        }

        public Builder appendTranslate(String value) {
            return push().type(ComponentType.TRANSLATE).value(value);
        }

        public Builder appendKeybind(String value) {
            return push().type(ComponentType.KEYBIND).value(value);
        }

        public Builder appendScore(String value) {
            return push().type(ComponentType.SCORE).value(value);
        }

        public Builder appendSelector(String value) {
            return push().type(ComponentType.SELECTOR).value(value);
        }

        public Component[] result() {
            return components.toArray(new Component[0]);
        }

        public Component[] build() {
            return push().result();
        }
    }
}
