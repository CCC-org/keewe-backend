package ccc.keewecore.utils;

import java.util.List;

public class ListUtils {
    public static <T> T getLast(List<T> iterables) {
        return iterables.get(iterables.size() - 1);
    }
}
