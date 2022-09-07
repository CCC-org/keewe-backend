package ccc.keewecore.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.BreakIterator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeeweStringUtil {

    public static long getGraphemeLength(String s) {
        long length = getCharacterBoundaryCount(s) - get4ByteEmojiCount(s);
        return length;
    }

    private static long get4ByteEmojiCount(String s) {
        return s.chars()
                .filter(i -> Character.isSurrogate((char) i))
                .count() / 4;
    }

    private static long getCharacterBoundaryCount(String s) {
        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(s);
        long count = 0;
        while (it.next() != BreakIterator.DONE) {
            count++;
        }

        return count;
    }

    public static String compressWhiteSpaces(String s) {
        return s.trim().replaceAll("\b", "").replaceAll("\\s+", " ");
    }
}
