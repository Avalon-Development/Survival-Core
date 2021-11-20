package net.avalondevs.avaloncore.Utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataParser {

    public static Object tryToParse(String string, Class<?> classType) {

        System.out.println(classType.getSimpleName());

        switch (classType.getSimpleName()) {

            case "String":
                return string;
            case "int":
                return Integer.parseInt(string);
            case "double":
                return Double.parseDouble(string);
            case "long":
                return Long.parseLong(string);

        }

        return null;

    }

    public static boolean isType(String data, Class<?> type) {

        return tryToParse(data, type) != null;

    }

    public static boolean isEitherType(String data, Class<?>... types) {

        for (Class<?> type : types) {
            if (isType(data, type))
                return true;
        }

        return false;

    }

    /**
     * Converts the short form of an item:
     * {@code MATERIAL:DATA:AMOUNT} (where {@code :AMOUNT} is optional)
     * into an {@link ItemStack}
     *
     * @param string the short form
     * @return the resulting item stack, null if the material is invalid
     */
    @Nullable
    public static ItemStack readItem(@NotNull String string) {

        Validate.notNull(string, "Input cannot be null");

        String[] split = string.split(":");

        Material material = StrUtil.nameToEnum(split[0], Material.class);

        if (material == null)
            return null;

        int amount = Integer.parseInt(ArrayUtil.getOrDefault(split, 1, "1"));

        return new ItemStack(material, amount);
    }

    public static long readTime(String time) {

        long timeBuffer = 0;

        StringBuilder buffer = new StringBuilder();
        for (char c : time.toCharArray()) {

            if (Character.isDigit(c)) {

                buffer.append(c);

            } else {

                String finalS = buffer.toString();

                if (buffer.isEmpty())
                    continue;

                long res = Long.parseLong(finalS);

                switch (c) {

                    case 'y' -> res *= 31557600000L;
                    case 'w' -> res *= 604800000L;
                    case 'd' -> res *= 86400000L;
                    case 'h' -> res *= 3600000L;
                    case 'm' -> res *= 60000L;
                    case 's' -> res *= 1000L;

                }

                buffer = new StringBuilder();

                timeBuffer += res;

            }

        }

        return timeBuffer;

    }

}
