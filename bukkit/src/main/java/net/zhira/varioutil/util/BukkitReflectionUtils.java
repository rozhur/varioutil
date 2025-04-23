package net.zhira.varioutil.util;

import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

class BukkitReflectionUtils {
    private static final String OBC_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    private static final Class<?> __CraftServer__CLASS = ReflectionUtils.searchType(OBC_PACKAGE + ".CraftServer");
    private static final Class<?> __CraftMetaSkull__CLASS = ReflectionUtils.searchType(OBC_PACKAGE + ".inventory.CraftMetaSkull");

    private static final Field __commandMap__CraftServer__FIELD = ReflectionUtils.fieldSearcher()
        .type(__CraftServer__CLASS)
        .fieldOf("commandMap")
        .fieldType(CommandMap.class)
        .search();
    private static final Field __knownCommands__SimpleCommandMap__FIELD = ReflectionUtils.fieldSearcher()
        .type(SimpleCommandMap.class)
        .fieldOf("knownCommands")
        .fieldType(Map.class)
        .search();

    private static final GameProfileConsumer GAME_PROFILE_CONSUMER;

    static CommandMap getCommandMap() {
        return (CommandMap) ReflectionUtils.getFieldValue(Bukkit.getServer(), __commandMap__CraftServer__FIELD);
    }

    @SuppressWarnings("unchecked")
    static Map<String, Command> getKnownCommands() {
        return (Map<String, Command>) ReflectionUtils.getFieldValue(getCommandMap(), __knownCommands__SimpleCommandMap__FIELD);
    }

    static void setProfile(SkullMeta meta, GameProfile profile) {
        GAME_PROFILE_CONSUMER.accept(meta, profile);
    }

    private interface GameProfileConsumer {
        void accept(SkullMeta meta, GameProfile profile);
    }

    private static class ModernGameProfileConsumer implements GameProfileConsumer {
        private final Class<?> __ResolvableProfile_CLASS = ReflectionUtils.getType("net.minecraft.world.item.component.ResolvableProfile");
        private final Constructor<?> __ResolvableProfile_CONSTRUCTOR;
        protected final Method __setProfile__CraftMetaSkull__METHOD = ReflectionUtils.methodSearcher()
            .type(__CraftMetaSkull__CLASS)
            .methodOf("setProfile")
            .parameters(__ResolvableProfile_CLASS)
            .returns(void.class)
            .search();

        ModernGameProfileConsumer() {
            Constructor<?> resolvableConstructor;
            try {
                resolvableConstructor = __ResolvableProfile_CLASS.getConstructor(GameProfile.class);
            } catch (NoSuchMethodException e) {
                throw new NoSuchMethodError("Cannot get constructor of " + __ResolvableProfile_CLASS);
            }
            __ResolvableProfile_CONSTRUCTOR = resolvableConstructor;
        }

        @Override
        public void accept(SkullMeta meta, GameProfile profile) {
            try {
                Object resolvableProfile = __ResolvableProfile_CONSTRUCTOR.newInstance(profile);
                ReflectionUtils.invokeMethod(meta, __setProfile__CraftMetaSkull__METHOD, resolvableProfile);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class LegacyGameProfileConsumer implements GameProfileConsumer {
        protected final Method __setProfile__CraftMetaSkull__METHOD = ReflectionUtils.methodSearcher()
            .type(__CraftMetaSkull__CLASS)
            .methodOf("setProfile")
            .parameters(GameProfile.class)
            .returns(void.class)
            .search();

        @Override
        public void accept(SkullMeta meta, GameProfile profile) {
            ReflectionUtils.invokeMethod(meta, __setProfile__CraftMetaSkull__METHOD, profile);
        }
    }

    private static class AncientGameProfileConsumer implements GameProfileConsumer {
        private final Field __profile__CraftMetaSkull__FIELD = ReflectionUtils.fieldSearcher()
            .type(__CraftMetaSkull__CLASS)
            .fieldOf("profile")
            .fieldType(GameProfile.class)
            .search();

        @Override
        public void accept(SkullMeta meta, GameProfile profile) {
            ReflectionUtils.setFieldValue(meta, __profile__CraftMetaSkull__FIELD, profile);
        }
    }

    @SafeVarargs
    private static GameProfileConsumer tryCreateGameProfileConsumer(Supplier<GameProfileConsumer>... suppliers) {
        List<String> errorMessages = new ArrayList<>();
        for (Supplier<GameProfileConsumer> supplier : suppliers) {
            try {
                return supplier.get();
            } catch (Exception | Error e) {
                errorMessages.add(e.getClass() + ": " + e.getMessage());
            }
        }
        throw new IllegalStateException("Cannot create GameProfileConsumer: " + String.join("; ", errorMessages));
    }

    static {
        GAME_PROFILE_CONSUMER = tryCreateGameProfileConsumer(
            ModernGameProfileConsumer::new,
            LegacyGameProfileConsumer::new,
            AncientGameProfileConsumer::new
        );
    }
}
