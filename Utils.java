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
     * 读取文件全部内容
     * read full content of given file
     *
     *
     * @param file file to read
     * @return file content as string
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

    public static String concatenateItems(List<String> items) {
        String result = "";
        for (Object item: items) {
            result += item + ",";
        }
        return result;
    }

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

    public static void asyncExecute(List<Runnable> actions) {
        int threadCount = Math.min(actions.size(), 10);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (Runnable action : actions) {
            executor.submit(action);
        }
    }
}
