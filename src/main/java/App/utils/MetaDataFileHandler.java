package App.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class MetaDataFileHandler {
    private static final String META_DATA_FILE_LOCATION = "src/main/resources/isBlockCurrentlyActive.txt";

    public void handleMetaDataFile(String blockState) throws IOException {
        Files.write(Paths.get(META_DATA_FILE_LOCATION),blockState.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    public String getCurrentState() throws IOException {
        List<String> strings = Files.readAllLines(Paths.get(META_DATA_FILE_LOCATION));
        return strings.get(0);
    }
}
