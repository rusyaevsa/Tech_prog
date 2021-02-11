package logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class ParseFile {
    private static final String TEMP_FILE_NAME = "files\\temp\\temp";
    private static int nameCount = 0;
    private static int maxLen = 0;

    // метод в котором происходит сортировка ArrayList и запись из него строк во временный файл
    private static void recordFromArray(ArrayList<String> buffer, int count) throws IOException {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(TEMP_FILE_NAME + nameCount + ".txt"));
        int i = 0;
        buffer.sort(String::compareToIgnoreCase);
        for (String line: buffer) {
            i++;
            writer.write(line);
            maxLen = Math.max(maxLen, line.length());
            if (i != count) writer.newLine();
        }

        writer.close();
        buffer.clear();
        nameCount++;
    }

    // метод для записи во временные файлы
    private static void tempRecord(BufferedReader reader) throws IOException {
        int bufferSize = 5;
        Iterator<String> iter = reader.lines().iterator();
        ArrayList<String> buffer = new ArrayList<String>();
        while (iter.hasNext()) {
            buffer.add(iter.next());
            if (buffer.size() == bufferSize) recordFromArray(buffer, 5);
        }
        recordFromArray(buffer, buffer.size());
    }

    // в этом методе считываем файл и разбиваем его на временные
    public static int spliting () {
        Scanner input = new Scanner(System.in);
        BufferedReader reader;
        boolean flag = true;
        do {
            System.out.print("Input filename: ");
            String filename = input.nextLine();
            try {
                reader = new BufferedReader(new FileReader("files\\" + filename));
                tempRecord(reader);
                flag = false;
            } catch (FileNotFoundException e) {
                System.out.println("File not found!");
            } catch (IOException e) {
                System.out.println("Failed to create temporary file");
            }
        } while (flag);
        return maxLen;
    }
}

