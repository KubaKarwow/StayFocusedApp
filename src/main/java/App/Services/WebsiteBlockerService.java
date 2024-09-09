package App.Services;

import App.utils.BlockedWebsitesHandler;
import App.utils.MetaDataFileHandler;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.util.List;

public class WebsiteBlockerService {

    public void block(Event currentWorkingEvent) throws IOException {
        BlockedWebsitesHandler blockedWebsitesHandler = new BlockedWebsitesHandler();
        List<String> websitesToBlock = blockedWebsitesHandler.getWebsitesToBlock();

        try {
            ScriptService.runBlockWebsitesScript(websitesToBlock);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            MetaDataFileHandler metaDataFileHandler = new MetaDataFileHandler();
            metaDataFileHandler.handleMetaDataFile("blocked");
        }


        new Thread(() -> {
            long millisToBlockFor = currentWorkingEvent.getEnd().getDateTime().getValue()-System.currentTimeMillis();
            long secondsToBlockFor = millisToBlockFor/1000;
            while(secondsToBlockFor>0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                secondsToBlockFor--;
                System.out.println(secondsToBlockFor +  "till we unblock");
            }
            try {
                unblock();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            MetaDataFileHandler metaDataFileHandler = new MetaDataFileHandler();
            try {
                metaDataFileHandler.handleMetaDataFile("unblocked");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();


    }
    public void unblock() throws IOException, InterruptedException {
        ScriptService.runUnblockWebsitesScript();
    }
    public void waitAndBlock(Event event) throws InterruptedException, IOException {
        new Thread( () -> {
            long millisTillStart= event.getStart().getDateTime().getValue()-System.currentTimeMillis();
            long secondsTillStart= millisTillStart/1000;
            while(secondsTillStart>0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                secondsTillStart--;
                System.out.println(secondsTillStart + " seconds till we begin the block");
            }
            try {
                block(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}
