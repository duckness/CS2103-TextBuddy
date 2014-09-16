package textbuddy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextFileTest {

    private static final String TEST_FILE = "mytestfile.txt";
    private static final String TEST_PATH = "src/" + TEST_FILE;

    private static final String TEST_EMPTY = "\"" + TEST_FILE + "\" is empty.%n";
    private static final String TEST_STRING_INIT = "1. The quick brown fox%n" +
                                                   "2. jumps over the lazy dog.%n" +
                                                   "3. Lorem ipsum%n" +
                                                   "4. asdfghjkl%n";

    private static final String TEST_ADD = "shesellsseashellsbytheseashore";


    @Test
    public void testToString() {
        TextFile tester = new TextFile(TEST_PATH);
        assertEquals(TEST_FILE + "is & must be empty before testing can start",
                     TEST_EMPTY, tester.toString());
    }

    @Test
    public void testInit() {
        TextFile tester = new TextFile(TEST_PATH);
        initTestFile(tester);
        assertEquals("Strings from initTestFile must be added correctly",
                     TEST_STRING_INIT, tester.toString());
    }

    @Test
    public void testIsAdded() {
        TextFile tester = new TextFile(TEST_PATH);
        initTestFile(tester);

        tester.add(TEST_ADD);
        assertEquals("New string:" + TEST_ADD + "must be added correctly",
                     TEST_STRING_INIT + "5. " + TEST_ADD + "%n",
                     tester.toString());
    }

    @Test
    public void testisCleared() {
        TextFile tester = new TextFile(TEST_PATH);
        initTestFile(tester);

        tester.clear();
        assertEquals(TEST_FILE + "must be empty.",
                     TEST_EMPTY, tester.toString());
    }

    private static void initTestFile(TextFile tester) {
        clearTestFile(tester);
        populateTestFile(tester);
    }

    private static void populateTestFile(TextFile tester) {
        tester.add("The quick brown fox");
        tester.add("jumps over the lazy dog.");
        tester.add("Lorem ipsum");
        tester.add("asdfghjkl");
    }

    private static void clearTestFile(TextFile tester) {
        tester.clear();
    }
}