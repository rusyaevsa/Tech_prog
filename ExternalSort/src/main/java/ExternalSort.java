import logic.ParseFile;
import logic.SortFile;

import java.io.IOException;

public class ExternalSort {
    public static void main(String[] args) throws IOException {
        ParseFile.spliting();
        SortFile.mergeSorting();
    }
}
