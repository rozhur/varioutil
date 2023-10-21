package org.zhdev.varioutil.component;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.zhdev.varioutil.color.ChatColor;
import org.zhdev.varioutil.color.NamedChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentSerializer { 
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?://)?([-\\w_.]{2,}\\.[a-z]{2,63})(/\\S*)?$");

    private static String readAsString(JSONObject object, String key) {
        Object element = object.get(key);
        return element instanceof String ? (String) element : null;
    }

    private static Boolean readAsBoolean(JSONObject object, String key) {
        Object element = object.get(key);
        return element instanceof Boolean ? (Boolean) element : null;
    }

    private static <T extends Enum<T>> T readAsEnum(JSONObject object, Class<T> enumClass) {
        Object element = object.get("action");
        if (!(element instanceof String)) {
            return null;
        }

        try {
            return Enum.valueOf(enumClass, ((String) element).toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static ChatColor readAsColor(JSONObject object) {
        Object element = object.get("color");
        if (!(element instanceof String)) {
            return null;
        }

        String value = (String) element;
        try {
            return value.startsWith("#") ? ChatColor.valueOf(Integer.decode(value)) : ChatColor.valueOf(value.toLowerCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static JSONObject readAsObject(JSONObject object, String key) {
        Object element = object.get(key);
        return element instanceof JSONObject ? (JSONObject) element : null;
    }

    private static void writeRawProperty(StringBuilder builder, String key, String value) {
        builder.append(',').append('"').append(JSONValue.escape(key)).append('"').append(':').append(value);
    }

    private static void writeRawPropertyWithoutComma(StringBuilder builder, String key, String value) {
        builder.append('"').append(JSONValue.escape(key)).append('"').append(':').append(value);
    }

    private static void writeProperty(StringBuilder builder, String key, Object value) {
        if (value != null) writeRawProperty(builder, key, JSONValue.toJSONString(value));
    }

    private static void writePropertyWithoutComma(StringBuilder builder, String key, Object value) {
        if (value != null) writeRawPropertyWithoutComma(builder, key, JSONValue.toJSONString(value));
    }

    private static void writeProperty(StringBuilder builder, ChatColor value) {
        if (value != null) writeRawProperty(builder, "color", JSONValue.toJSONString(value.getName()));
    }

    private static Component deserialize0(JSONObject object) {
        ComponentType type;

        Object element;
        if ((element = object.get("text")) != null) {
            type = ComponentType.TEXT;
        } else if ((element = object.get("translate")) != null) {
            type = ComponentType.TRANSLATE;
        } else if ((element = object.get("keybind")) != null) {
            type = ComponentType.KEYBIND;
        } else if ((element = object.get("score")) != null) {
            type = ComponentType.SCORE;
        } else if ((element = object.get("selector")) != null) {
            type = ComponentType.SELECTOR;
        } else {
            return Component.EMPTY;
        }

        String value = element.toString();
        Boolean bold = readAsBoolean(object, "bold");
        Boolean italic = readAsBoolean(object, "italic");
        Boolean underlined = readAsBoolean(object, "underlined");
        Boolean strikethrough = readAsBoolean(object, "strikethrough");
        Boolean obfuscated = readAsBoolean(object, "obfuscated");
        MinecraftFont font = MinecraftFont.byNamespace(readAsString(object, "font"));
        ChatColor color = readAsColor(object);
        String insertion = readAsString(object, "insertion");

        ClickEvent clickEvent;
        String clickEventValue;

        JSONObject clickEventObject = readAsObject(object, "clickEvent");
        if (clickEventObject != null) {
            clickEvent = readAsEnum(clickEventObject, ClickEvent.class);
            clickEventValue = readAsString(clickEventObject, "value");
        } else {
            clickEvent = null;
            clickEventValue = null;
        }

        HoverEvent hoverEvent;
        Component[] hoverEventValue;

        JSONObject hoverEventObject = readAsObject(object, "hoverEvent");
        if (hoverEventObject != null) {
            hoverEvent = readAsEnum(hoverEventObject, HoverEvent.class);
            hoverEventValue = deserialize(readAsObject(hoverEventObject, "value"));
        } else {
            hoverEvent = null;
            hoverEventValue = null;
        }

        Component[] extra = null;

        Object extraElement = object.get("extra");
        if (extraElement != null) {
            extra = deserialize(extraElement);
        }

        Component[] with = null;

        Object withElement = object.get("with");
        if (withElement != null) {
            with = deserialize(withElement);
        }

        return new Component(type, value, bold, italic, underlined, strikethrough, obfuscated, font, color, insertion,
                clickEvent, clickEventValue, hoverEvent, hoverEventValue, extra, with);
    }

    public static Component[] deserialize(Object json) {
        Component[] components;
        if (json instanceof JSONArray) {
            JSONArray array = (JSONArray) json;
            components = new Component[array.size()];
            for (int i = 0; i < components.length; i++) {
                Object e = array.get(i);
                if (!(e instanceof JSONObject)) {
                    continue;
                }
                components[i] = deserialize0((JSONObject) e);
            }
        } else if (json instanceof JSONObject) {
            components = new Component[] {deserialize0((JSONObject) json)};
        } else {
            components = new Component[] {Component.EMPTY};
        }

        return components;
    }

    public static Component[] deserialize(String json) {
        return deserialize(JSONValue.parse(json));
    }

    public static Component[] deserializeLegacy(String value, char colorChar, ChatColor resetColor) {
        List<Component> components = new ArrayList<>();

        StringBuilder valueBuilder = new StringBuilder();
        int lastIndex = value.length() - 1;
        Matcher urlMatcher = URL_PATTERN.matcher(value);
        
        ChatColor color = null;
        Boolean bold = null;
        Boolean italic = null;
        Boolean underlined = null;
        Boolean strikethrough = null;
        Boolean obfuscated = null;

        for (int i = 0; i < value.length(); i++) {
            char currentChar = value.charAt(i);
            if (currentChar == colorChar) {
                int j = i + 1;
                if (j > lastIndex) {
                    valueBuilder.append(colorChar);
                    break;
                }

                ChatColor nextColor;
                char nextChar = Character.toLowerCase(value.charAt(j));
                if (nextChar == 'x') {
                    StringBuilder hexValueBuilder = new StringBuilder(7).append('#');
                    j++;
                    int n = j + 12;
                    if (n > lastIndex) {
                        valueBuilder.append(colorChar);
                        continue;
                    }
                    for (; j < n; j++) {
                        nextChar = value.charAt(j);
                        if (nextChar == colorChar) {
                            continue;
                        }
                        hexValueBuilder.append(nextChar);
                    }

                    if (hexValueBuilder.length() < 7) {
                        valueBuilder.append(colorChar);
                        continue;
                    }

                    try {
                        nextColor = ChatColor.valueOf(Integer.decode(hexValueBuilder.toString()));
                        if (nextColor == null) {
                            valueBuilder.append(colorChar);
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        valueBuilder.append(colorChar);
                        continue;
                    }
                    i = j - 1;
                } else {
                    nextColor = ChatColor.valueOf(nextChar);
                    if (nextColor == null) {
                        valueBuilder.append(colorChar);
                        continue;
                    }
                    i++;
                }

                if (valueBuilder.length() > 0) {
                    components.add(new Component(valueBuilder.toString(), bold, italic, underlined, strikethrough,
                            obfuscated, color));
                    valueBuilder.setLength(0);
                }

                if (nextColor instanceof NamedChatColor) {
                    if (nextColor == NamedChatColor.BOLD) {
                        bold = true;
                        continue;
                    } else if (nextColor == NamedChatColor.ITALIC) {
                        italic = true;
                        continue;
                    } else if (nextColor == NamedChatColor.UNDERLINED) {
                        underlined = true;
                        continue;
                    } else if (nextColor == NamedChatColor.STRIKETHROUGH) {
                        strikethrough = true;
                        continue;
                    } else if (nextColor == NamedChatColor.OBFUSCATED) {
                        obfuscated = true;
                        continue;
                    } else if (nextColor == NamedChatColor.RESET) {
                        nextColor = resetColor;
                    }
                }

                bold = null;
                italic = null;
                underlined = null;
                strikethrough = null;
                obfuscated = null;
                color = nextColor;
                continue;
            }

            int nextIndex = value.indexOf(' ', i);
            if (nextIndex == -1) {
                nextIndex = value.length();
            }

            if (urlMatcher.region(i, nextIndex).find()) {
                if (valueBuilder.length() > 0) {
                    components.add(new Component(valueBuilder.toString(), bold, italic, underlined, strikethrough,
                            obfuscated, color));
                    valueBuilder.setLength(0);
                }

                String proto = urlMatcher.group(1);

                StringBuilder urlBuilder = new StringBuilder();
                if (proto == null || proto.length() == 0) {
                    urlBuilder.append("http://");
                } else {
                    urlBuilder.append(proto);
                }

                String domain = urlMatcher.group(2);
                urlBuilder.append(domain);

                String uri = urlMatcher.group(3);
                if (uri != null && uri.length() > 0) {
                    urlBuilder.append(uri);
                }

                String url = urlBuilder.toString();
                components.add(new Component(urlMatcher.group(), bold, italic, underlined, strikethrough, obfuscated,
                        color, ClickEvent.OPEN_URL, url));

                i += nextIndex - i - 1;
                continue;
            }
            
            valueBuilder.append(currentChar);
        }

        components.add(new Component(valueBuilder.toString(), bold, italic, underlined, strikethrough, obfuscated, color));

        return components.toArray(new Component[0]);
    }

    public static Component[] deserializeLegacy(String value, char colorChar) {
        return deserializeLegacy(value, colorChar, null);
    }

    public static Component[] deserializeLegacy(String value) {
        return deserializeLegacy(value, ChatColor.CHAR);
    }

    public static String serialize(Component component) {
        StringBuilder builder = new StringBuilder().append('{');
        String value = component.getValue();
        writePropertyWithoutComma(builder, component.getType().toString(), value);
        writeProperty(builder, "bold", component.getBold());
        writeProperty(builder, "italic", component.getItalic());
        writeProperty(builder, "underlined", component.getUnderlined());
        writeProperty(builder, "strikethrough", component.getStrikethrough());
        writeProperty(builder, "obfuscated", component.getObfuscated());
        writeProperty(builder, "font", component.getFont());
        writeProperty(builder, component.getColor());
        writeProperty(builder, "insertion", component.getInsertion());

        if (component.getClickEvent() != null) {
            StringBuilder clickEventBuilder = new StringBuilder().append('{');
            writePropertyWithoutComma(clickEventBuilder, "action", component.getClickEvent().toString());
            writeProperty(clickEventBuilder, "value", component.getClickEventValue());
            writeRawProperty(builder, "clickEvent", clickEventBuilder.append('}').toString());
        }

        if (component.getHoverEvent() != null) {
            StringBuilder hoverEventBuilder = new StringBuilder().append('{');
            writePropertyWithoutComma(hoverEventBuilder, "action", component.getHoverEvent().toString());
            writeRawProperty(hoverEventBuilder, "value", serialize(component.getHoverEventValue()));
            writeRawProperty(builder, "hoverEvent", hoverEventBuilder.append('}').toString());
        }

        if (component.getExtra() != null) {
            writeRawProperty(builder, "extra", serialize(component.getExtra()));
        }

        if (component.getWith() != null) {
            writeRawProperty(builder, "with", serialize(component.getWith()));
        }

        return builder.append('}').toString();
    }

    public static String serialize(Component... components) {
        if (components.length == 0) {
            return "{\"text\":\"\"}";
        } else if (components.length == 1) {
            return serialize(components[0]);
        } else {
            StringBuilder builder = new StringBuilder();
            for (Component component : components) {
                if (builder.length() == 0) {
                    builder.append('[');
                } else {
                    builder.append(',');
                }
                builder.append(serialize(component));
            }
            return builder.append(']').toString();
        }
    }
}
