package logic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SortFile {
    private static final String TEMP_FILE_NAME = "files\\temp";
    private static final Path PATH = Paths.get(TEMP_FILE_NAME);
    private static int nameCount;

    static {
        try {
            nameCount = (int) Files.list(PATH).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void delTempFile() throws IOException {
        for (int i = 0; i < nameCount; i++) {
            new File(TEMP_FILE_NAME + "\\temp" + i + ".txt").delete();
        }
    }

    private static void merge(ArrayList<BufferedReader> readers, BufferedWriter writer, int maxLen) throws IOException {
        for(int i = 0; i < nameCount; i++){
            BufferedReader current = readers.get(i);
            while (current.ready()) {
                readers.get(i).mark(maxLen);
                String min = readers.get(i).readLine();
                int indexMin = i;

                for (int j = i + 1; j < nameCount; j++)
                {
                    BufferedReader temp = readers.get(j);
                    temp.mark(maxLen);
                    if (temp.ready()) {
                        String tempString = temp.readLine();
                        if (tempString.compareToIgnoreCase(min) == -1) {
                            min = tempString;
                            readers.get(indexMin).reset();
                            readers.get(indexMin).mark(maxLen);
                            indexMin = j;
                        }
                        else {
                            temp.reset();
                            temp.mark(maxLen);
                        }
                    }
                }
                writer.write(min);
                writer.newLine();
            }
        }
    }

    public static void mergeSorting(int maxLen) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("files\\external.txt"));
        ArrayList<BufferedReader> readers = new ArrayList<>();

        for (int i = 0; i < nameCount; i++) {
            readers.add(new BufferedReader(new FileReader(TEMP_FILE_NAME + "\\temp" + i + ".txt")));
        }

        merge(readers, writer, maxLen);

        readers.forEach(x -> {
            try {
                x.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writer.close();
        delTempFile();
    }
}
