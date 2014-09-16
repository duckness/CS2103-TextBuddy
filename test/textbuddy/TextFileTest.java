package textbuddy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextFileTest {

    private static final String TEST_FILE = "mytestfile.txt";
    private static final String TEST_PATH = "src/" + TEST_FILE;

    @Test
    public void testToString() {
        TextFile tester = new TextFile(TEST_PATH);
        assertEquals(TEST_FILE + "is & must be empty before testing can start",
                      "\"" + TEST_FILE + "\" is empty.%n",tester.toString());
    }
}