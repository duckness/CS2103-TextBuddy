package textbuddy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextFileTest {

    private static final String TEST_FILE = "mytestfile.txt";
    private static final String TEST_PATH = "src/" + TEST_FILE;

    private static final String TEST_STRING_1 = "The quick brown fox";
    private static final String TEST_STRING_2 = "jumps over the lazy dog";
    private static final String TEST_STRING_3 = "Lorem ipsum";
    private static final String TEST_STRING_4 = "asdfghjkl";
    private static final String TEST_STRING_5 = "shesellsseashellsbytheseashore";

    private static final String TEST_STRING_5_SUB = "sea";

    private static final String TEST_LIST_EMPTY = "\"" + TEST_FILE + "\" is empty.%n";
    private static final String TEST_LIST_INIT = "1. " + TEST_STRING_1 + "%n" +
                                                 "2. " + TEST_STRING_2 + "%n" +
                                                 "3. " + TEST_STRING_3 + "%n" +
                                                 "4. " + TEST_STRING_4 + "%n";
    private static final String TEST_LIST_SORTED_1 = "1. " + TEST_STRING_4 + "%n" +
                                                     "2. " + TEST_STRING_2 + "%n" +
                                                     "3. " + TEST_STRING_3 + "%n" +
                                                     "4. " + TEST_STRING_1 + "%n";
    private static final String TEST_LIST_SORTED_2 = "1. " + TEST_STRING_4 + "%n" +
                                                     "2. " + TEST_STRING_2 + "%n" +
                                                     "3. " + TEST_STRING_2 + "%n" +
                                                     "4. " + TEST_STRING_3 + "%n" +
                                                     "5. " + TEST_STRING_1 + "%n";
    private static final String TEST_LIST_SORTED_3 = "1. " + TEST_STRING_4 + "%n" +
                                                     "2. " + TEST_STRING_2 + "%n" +
                                                     "3. " + TEST_STRING_2 + "%n" +
                                                     "4. " + TEST_STRING_3 + "%n" +
                                                     "5. " + TEST_STRING_5 + "%n" +
                                                     "6. " + TEST_STRING_1 + "%n";

    private static final String TEST_SEARCH_1 = "Not found.";
    private static final String TEST_SEARCH_2 = "3. " + TEST_STRING_3 + "%n";
    private static final String TEST_SEARCH_3 = "2. " + TEST_STRING_2 + "%n";
    private static final String TEST_SEARCH_4 = "1. " + TEST_STRING_1 + "%n" +
                                                "5. " + TEST_STRING_1 + "%n";
    private static final String TEST_SEARCH_5 = "6. " + TEST_STRING_5 + "%n";

    @Test
    public void testToString() {
        TextFile tester = new TextFile(TEST_PATH);
        assertEquals(TEST_FILE + "is & must be empty before testing can start",
                     TEST_LIST_EMPTY, tester.toString());
    }

    @Test
    public void testInit() {
        TextFile tester = new TextFile(TEST_PATH);
        initTestFile(tester);
        assertEquals("Strings from initTestFile must be added correctly",
                     TEST_LIST_INIT, tester.toString());
    }

    @Test
    public void testIsAdded() {
        TextFile tester = new TextFile(TEST_PATH);
        initTestFile(tester);

        tester.add(TEST_STRING_5);
        assertEquals("New string:" + TEST_STRING_5 + "must be added correctly",
                     TEST_LIST_INIT + "5. " + TEST_STRING_5 + "%n",
                     tester.toString());
    }

    @Test
    public void testisCleared() {
        TextFile tester = new TextFile(TEST_PATH);
        initTestFile(tester);

        tester.clear();
        assertEquals(TEST_FILE + "must be empty.",
                     TEST_LIST_EMPTY, tester.toString());
    }

    @Test
    public void testSort() {
        TextFile tester = new TextFile(TEST_PATH);
        initTestFile(tester);

        tester.sort();
        assertEquals("Strings from init must be sorted", TEST_LIST_SORTED_1,
                     tester.toString());

        tester.add(TEST_STRING_2);
        tester.sort();
        assertEquals("List must be sorted to correct order after adding similar string",
                     TEST_LIST_SORTED_2, tester.toString());

        tester.add(TEST_STRING_5);
        tester.sort();
        assertEquals("List must be sorted to correct order after adding non-similar string",
                TEST_LIST_SORTED_3, tester.toString());
    }

    @Test
    public void testSearch() {
        TextFile tester = new TextFile(TEST_PATH);
        initTestFile(tester);

        assertEquals("\"Not found\" must be returned for a string that is not found",
                     TEST_SEARCH_1, tester.search(TEST_STRING_5));

        assertEquals("Full line must be found", TEST_SEARCH_2,
                     tester.search(TEST_STRING_3));

        assertEquals("Substring of a line must be found", TEST_SEARCH_3,
                     tester.search(TEST_STRING_2.substring(5, 10)));

        tester.add(TEST_STRING_1);
        assertEquals("Multiple lines must be returned if string is found in multiple lines",
                     TEST_SEARCH_4, tester.search(TEST_STRING_1.substring(7)));

        tester.add(TEST_STRING_5);
        assertEquals("The same line must not be returned more than once",
                     TEST_SEARCH_5, tester.search(TEST_STRING_5_SUB));
    }

    private static void initTestFile(TextFile tester) {
        clearTestFile(tester);
        populateTestFile(tester);
    }

    private static void populateTestFile(TextFile tester) {
        tester.add(TEST_STRING_1);
        tester.add(TEST_STRING_2);
        tester.add(TEST_STRING_3);
        tester.add(TEST_STRING_4);
    }

    private static void clearTestFile(TextFile tester) {
        tester.clear();
    }
}