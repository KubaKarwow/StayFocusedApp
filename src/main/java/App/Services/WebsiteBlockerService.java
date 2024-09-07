package App.Services;

import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.util.List;

public class WebsiteBlockerService {

    public void block(Event currentWorkingEvent) throws IOException, InterruptedException {
        ScriptService.runBlockWebsitesScript(List.of("facebook.com","youtube.com"));
        System.out.println("script wywolany");
        Thread.sleep(4000);
        System.out.println("TU BEDZIEMY ODBLOKOWYWAC STRONY ");
        ScriptService.runUnblockWebsitesScript();
    }

    public void waitAndBlock(Event event) {

    }
}
