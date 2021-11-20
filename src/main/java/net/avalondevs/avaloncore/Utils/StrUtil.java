package net.avalondevs.avaloncore.Utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

public class StrUtil {

    public static final String EMPTY_COLOR = ChatColor.RESET.toString();

    public static String[] v(String... args) {

        return args;

    }

    /**
     * Replaces the popular enum naming style for enums (ENUM_NAME)
     * to a readable format (Enum Name)
     *
     * @param enumT the enum to convert
     * @return the enum name converted
     */
    public static String extractNameFromEnum(Enum<?> enumT) {

        String name = enumT.name();

        name = name.replaceAll("_", " ").toLowerCase();

        name = WordUtils.capitalize(name);

        return name;

    }

    /**
     * Reverse method to {@link #extractNameFromEnum(Enum)}
     * takes in a name, converts it to enum style, and an enum class and returns the respective enum
     *
     * @param name      the name to use
     * @param enumClazz the original enum class
     * @return the enum instance or null if none found
     */
    @Nullable
    public static <T extends Enum<T>> T nameToEnum(String name, Class<T> enumClazz) {

        String converted = name.replaceAll(" ", "_").toUpperCase();

        Enum[] values = enumClazz.getEnumConstants();

        for (Enum value : values) {
            if (value.name().equals(converted))
                return (T) value;
        }

        return null;
    }

}
