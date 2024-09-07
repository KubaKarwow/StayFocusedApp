package App;

import App.Services.EventService;
import App.dtos.IsDuringSessionDTO;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws GeneralSecurityException, IOException, InterruptedException {
        List<Event> events = GoogleCalendarConnection.getEvents();
        EventService eventService = new EventService(events);
        eventService.processEvents();
        IsDuringSessionDTO inWorkingSession = eventService.isInWorkingSession();
        System.out.println(inWorkingSession.isInSession() + " is in session");
        System.out.println(inWorkingSession.getMilisUntilEndOfSession() +  " milliseconds until the end") ;
    }
}
