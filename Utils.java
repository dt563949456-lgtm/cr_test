mport org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utils {
    

    /**
     * Read the entire content of the given file into a single string.
     *
     * @param file the file to read
     * @return the file content as a single string; each original line is separated by the system line separator and the result may end with a trailing line separator
     * @throws UncheckedIOException if an I/O error occurs while reading the file
     */
    public static String readFile(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            return content.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Writes the provided list of strings to the given file, writing each entry as a separate line
     * and appending the platform line separator after each string.
     *
     * @param file  the target file to write to
     * @param lines the lines to write to the file; each list element becomes one line in the file
     * @throws UncheckedIOException if an I/O error occurs while writing to the file
     */
    public static void writeFile(File file, List<String> lines) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

            for (int i = 0; i < lines.size(); ++i) {
                writer.write(lines.get(i));
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * Concatenates the given list of strings into a single comma-separated string.
     *
     * @param items the list of strings to concatenate; may be empty
     * @return a string containing each input item followed by a comma; empty string if the list is empty
     */
    public static String concatenateItems(List<String> items) {
        String result = "";
        for (Object item: items) {
            result += item + ",";
        }
        return result;
    }

    /**
     * Finds the distinct string values that appear more than once in the given list.
     *
     * @param items the list of strings to inspect for repeated values
     * @return a list containing each value that occurs multiple times in {@code items}; each duplicate appears once, in the order its repeat is first encountered
     */
    public static List<String> findDuplicates(List<String> items) {
        List<String> duplicates = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size(); j++) {
                if (items.get(i).equals(items.get(j)) && !duplicates.contains(items.get(i))) {
                    duplicates.add(items.get(i));
                }
            }
        }
        return duplicates;
    }

    /**
     * Executes the given Runnable tasks concurrently using a fixed-size thread pool.
     *
     * Submits each task to a pool with size equal to Math.min(actions.size(), 10). The method does not shut down
     * the created ExecutorService; callers are responsible for managing executor lifecycle if required.
     *
     * @param actions list of Runnable tasks to submit; must not be null (may be empty)
     */
    public static void asyncExecute(List<Runnable> actions) {
        int threadCount = Math.min(actions.size(), 10);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (Runnable action : actions) {
            executor.submit(action);
        }
    }
}