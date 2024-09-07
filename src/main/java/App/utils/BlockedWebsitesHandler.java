package App.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BlockedWebsitesHandler {
    private static final String WEBSITES_TO_BLOCK_FILE_LOCATION = "src/main/resources/websitesToBlock.txt";

    public List<String> getWebsitesToBlock() throws IOException {
        return Files.readAllLines(Paths.get(WEBSITES_TO_BLOCK_FILE_LOCATION));
    }
}
