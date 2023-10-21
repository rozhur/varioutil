package org.zhdev.varioutil.color;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class ChatColorCache {
    static final Map<Integer, ChatColor> BY_VALUE = new ConcurrentHashMap<>();
    static final Map<Character, ChatColor> BY_CODE = new LinkedHashMap<>(21);
    static final Map<String, ChatColor> BY_NAME = new LinkedHashMap<>(21);
}
