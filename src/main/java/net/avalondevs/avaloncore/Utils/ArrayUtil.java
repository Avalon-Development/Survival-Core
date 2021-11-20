package net.avalondevs.avaloncore.Utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;

@UtilityClass
public class ArrayUtil {

    public <T> T getOrDefault(T[] array, int index, T defaultElement) {

        if (index < 0 || index >= array.length)
            return defaultElement;

        return array[index];

    }

    public <T> T getNextElement(Collection<T> set, T element) {

        ArrayList<T> list = new ArrayList<>(set);

        int index = list.indexOf(element);

        if (index == -1)
            return null;

        if (index + 1 >= list.size())
            return null;

        return list.get(index + 1);

    }

}
