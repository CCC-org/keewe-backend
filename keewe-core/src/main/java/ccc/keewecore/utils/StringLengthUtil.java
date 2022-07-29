package ccc.keewecore.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.BreakIterator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringLengthUtil {

    public static long getGraphemeLength(String s) {
        if (StringUtils.isEmpty(s)) {
            return 0;
        }
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
}
