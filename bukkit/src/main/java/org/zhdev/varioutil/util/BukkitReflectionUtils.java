package org.zhdev.varioutil.util;

import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static org.zhdev.varioutil.util.ReflectionUtils.*;

class BukkitReflectionUtils {
    private static final String VERSION = Bukkit.getServer().getClass().getName().split("\\.")[3];

    private static final Class<?> __CraftServer__CLASS = searchType("org.bukkit.craftbukkit." + VERSION + ".CraftServer");

    private static final Method __setProfile__CraftMetaSkull__METHOD = ReflectionUtils.methodSearcher()
            .typeOf("org.bukkit.craftbukkit." + VERSION + ".inventory.CraftMetaSkull")
            .methodOf("setProfile")
            .parameters(GameProfile.class)
            .returns(void.class)
            .search();

    private static final Field __commandMap__CraftServer__FIELD = fieldSearcher()
            .fieldOf("commandMap")
            .type(__CraftServer__CLASS)
            .fieldType(CommandMap.class)
            .search();
    private static final Field __knownCommands__SimpleCommandMap__FIELD = fieldSearcher()
            .fieldOf("knownCommands")
            .type(SimpleCommandMap.class)
            .fieldType(Map.class)
            .search();

    static CommandMap getCommandMap() {
        return (CommandMap) getFieldValue(__commandMap__CraftServer__FIELD, Bukkit.getServer());
    }

    @SuppressWarnings("unchecked")
    static Map<String, Command> getKnownCommands() {
        return (Map<String, Command>) getFieldValue(__knownCommands__SimpleCommandMap__FIELD, getCommandMap());
    }

    static void setProfile(SkullMeta meta, GameProfile profile) {
        ReflectionUtils.invokeMethod(meta, __setProfile__CraftMetaSkull__METHOD, profile);
    }
}
