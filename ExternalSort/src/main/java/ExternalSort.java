import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ExternalSort {
    private static final String TEMP_FILE_NAME = "files\\temp\\temp";
    private static int nameCount = 0;

    private static void spliting() throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.print("Input filename: ");
        String filename = input.nextLine();
        BufferedReader reader = new BufferedReader(new FileReader("files\\" + filename));
        int len = 5;

        char[] buffer = new char[len];
        while (reader.ready()) {
            int count = reader.read(buffer, 0, len);
            Arrays.sort(buffer, 0, count);
            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(TEMP_FILE_NAME + nameCount + ".txt")));

            writer.write(buffer, 0, count);
            writer.flush();
            writer.close();
            buffer = new char[len];
            nameCount++;
        }
        reader.close();
    }

    private static void merge(ArrayList<RandomAccessFile> readers, BufferedWriter writer) throws IOException {
        for(int i = 0; i < nameCount; i++){
            while (readers.get(i).getFilePointer() < readers.get(i).length()) {
                char min = (char) readers.get(i).read();
                int indexMin = i;
                for (int j = i + 1; j < nameCount; j++)
                {
                    if (readers.get(j).getFilePointer() < readers.get(j).length()) {
                        char temp = (char) readers.get(j).read();
                        if (temp < min){
                            min = temp;
                            readers.get(indexMin).seek(readers.get(indexMin).getFilePointer() - 1);
                            indexMin = j;
                        }
                        else{
                            readers.get(j).seek(readers.get(j).getFilePointer() - 1);
                        }
                    }
                }
                writer.append(min);
            }
        }
    }

    private static void mergeSorting() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("files\\external.txt"));
        ArrayList<RandomAccessFile> readers = new ArrayList<>();
        for (int i = 0; i < nameCount; i++) {
            readers.add(new RandomAccessFile(TEMP_FILE_NAME + i + ".txt", "r"));
        }
        RandomAccessFile tempReader;

        merge(readers, writer);

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


    private static void delTempFile(){
        for (int i = 0; i < nameCount; i++) {
            new File(TEMP_FILE_NAME + i + ".txt").delete();
        }
    }

    public static void main(String[] args) throws IOException {
        spliting();
        mergeSorting();
    }
}
