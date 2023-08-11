package org.zhdev.varioutil.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.zhdev.varioutil.Version;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class BukkitUtils {
    public static final Version VERSION;

    private static final CommandMap COMMAND_MAP = BukkitReflectionUtils.getCommandMap();
    private static final Map<String, Command> KNOWN_COMMANDS = BukkitReflectionUtils.getKnownCommands();

    private static final Map<String, GameProfile> PROFILE_CACHE = new HashMap<>();

    public static Command getCommand(String label) {
        return KNOWN_COMMANDS.get(label);
    }

    public static void registerCommand(String fallbackPrefix, String label, Command command) {
        COMMAND_MAP.register(label, fallbackPrefix, command);
    }

    public static Command unregisterCommand(String label) {
        Command command = KNOWN_COMMANDS.remove(label);
        if (command != null) command.unregister(COMMAND_MAP);
        return command;
    }

    public static void unregisterCommandIf(Predicate<Command> predicate) {
        KNOWN_COMMANDS.values().removeIf(command -> {
            if (predicate.test(command)) {
                command.unregister(COMMAND_MAP);
                return true;
            }
            return false;
        });
    }

    public static void setSkullTexture(SkullMeta meta, String base64) {
        GameProfile profile = PROFILE_CACHE.get(base64);
        if (profile == null) {
            profile = new GameProfile(UUID.randomUUID(), base64);
            profile.getProperties().put("textures", new Property("textures", base64));
            PROFILE_CACHE.put(base64, profile);
        }
        BukkitReflectionUtils.setProfile(meta, profile);
    }

    public static String stacksToString(ItemStack[] contents) {
        try (ByteArrayOutputStream str = new ByteArrayOutputStream();
             BukkitObjectOutputStream data = new BukkitObjectOutputStream(str)) {
            data.writeInt(contents.length);
            for (ItemStack stack : contents) {
                data.writeObject(stack);
            }
            return Base64.getEncoder().encodeToString(str.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to convert items to string", e);
        }
    }

    public static ItemStack[] stringToStacks(String inventoryData) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(inventoryData));
             BukkitObjectInputStream data = new BukkitObjectInputStream(stream)) {
            int length = data.readInt();
            ItemStack[] stacks = new ItemStack[length];
            for (int i = 0; i < length; i++) {
                stacks[i] = (ItemStack) data.readObject();
            }
            return stacks;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Unable to convert string to items", e);
        }
    }

    static {
        String version = Bukkit.getServer().getVersion();
        VERSION = Version.fromString(version.substring(version.indexOf("(MC: ") + 5, version.length() - 1));
    }
}
