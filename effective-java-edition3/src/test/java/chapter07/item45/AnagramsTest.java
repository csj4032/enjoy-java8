package chapter07.item45;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AnagramsTest {

    @Test
    @DisplayName(value = "파일 속 단어 정리 결과 확인")
    public void anagrams() throws URISyntaxException, IOException {
        Anagrams anagrams = new Anagrams();
        Map<String, Set<String>> groups = anagrams.groups("anagrams.txt");
        Map<String, List<String>> groupsStream = anagrams.groupsStream("anagrams.txt");
        Map<String, List<String>> groupsStreamWithAlphabetize = anagrams.groupsStreamWithAlphabetize("anagrams.txt");
        Assertions.assertEquals(groups.get("abc"), Set.of("abc", "bac", "cba"));
        Assertions.assertEquals(groupsStream.get("abc"), List.of("abc", "bac", "cba"));
        Assertions.assertEquals(groupsStreamWithAlphabetize.get("abc"), List.of("abc", "bac", "cba"));
    }

    @Test
    public void compact() {
        final ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putChar('H');
        buffer.putChar('e');
        buffer.putChar('l');
        buffer.putChar('l');
        buffer.putChar('o');

        buffer.flip();
        buffer.position(4);
        buffer.compact();
        Assertions.assertEquals(6, buffer.position());

        buffer.putChar('n');
        buffer.putChar('g');

        System.out.println(Integer.reverseBytes(1));

        Assertions.assertEquals( 10, buffer.position());
        buffer.flip();
    }
}