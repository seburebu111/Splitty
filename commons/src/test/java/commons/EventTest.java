package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class EventTest {
    @Test
    public void checkConstructor() {
        var e = new Event("title");
        assertEquals("title", e.title);
    }

    @Test
    public void equalsHashCode() {
        var a = new Event("title");
        var b = new Event("title");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void checkTitle() {
        var testTitle = "title";
        var e = new Event(testTitle);
        assertEquals(testTitle, e.getTitle());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new Event("title");
        var b = new Event("title2");
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }





}
