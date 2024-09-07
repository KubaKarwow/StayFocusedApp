package App.Services;

import App.utils.BlockedWebsitesHandler;
import App.utils.MetaDataFileHandler;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.util.List;

public class WebsiteBlockerService {

    public void block(Event currentWorkingEvent) throws IOException, InterruptedException {
        BlockedWebsitesHandler blockedWebsitesHandler = new BlockedWebsitesHandler();
        List<String> websitesToBlock = blockedWebsitesHandler.getWebsitesToBlock();
        ScriptService.runBlockWebsitesScript(websitesToBlock);
        MetaDataFileHandler metaDataFileHandler = new MetaDataFileHandler();
        metaDataFileHandler.handleMetaDataFile("blocked");

        long millisToBlockFor = currentWorkingEvent.getEnd().getDateTime().getValue()-System.currentTimeMillis();
        long secondsToBlockFor = millisToBlockFor/1000;
        while(secondsToBlockFor>0){
            Thread.sleep(1000);
            secondsToBlockFor--;
            System.out.println(secondsToBlockFor +  "till we unblock");
        }
        unblock();
        metaDataFileHandler.handleMetaDataFile("unblocked");

    }
    public void unblock() throws IOException, InterruptedException {
        ScriptService.runUnblockWebsitesScript();
    }
    public void waitAndBlock(Event event) throws InterruptedException, IOException {
        long millisTillStart= event.getStart().getDateTime().getValue()-System.currentTimeMillis();
        long secondsTillStart= millisTillStart/1000;
        while(secondsTillStart>0){
            Thread.sleep(1000);
            secondsTillStart--;
            System.out.println(secondsTillStart + " seconds till we begin the block");
        }
        block(event);
    }
}
