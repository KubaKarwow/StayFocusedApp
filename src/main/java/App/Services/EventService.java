package App.Services;

import App.dtos.IsDuringSessionDTO;
import App.utils.BlockedWebsitesHandler;
import App.utils.MetaDataFileHandler;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EventService {
    private List<Event> eventList;
    private static final String WORKING_EVENT_NAME = "Working";
    private WebsiteBlockerService websiteBlockerService;
    public EventService(List<Event> eventList) {
        this.eventList = eventList;
        websiteBlockerService = new WebsiteBlockerService();
    }


    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<Event> getTodaysWorkingEvents() {
        return eventList.stream()
                .filter(event -> event.getSummary() != null)
                .filter(event -> event.getSummary().equals(WORKING_EVENT_NAME)).toList();
    }
    private void handleMetaDataFile(boolean shouldBeBlocked) throws IOException, InterruptedException {
        MetaDataFileHandler metaDataFileHandler = new MetaDataFileHandler();
        String currentState = metaDataFileHandler.getCurrentState();
        if(!shouldBeBlocked && currentState.equals("blocked")){
            websiteBlockerService.unblock();
            metaDataFileHandler.handleMetaDataFile("unblocked");
        }
        // maybe this will be needed
       // if(shouldBeBlocked && currentState.equals("unblocked")){}
    }
    public void processEvents() throws IOException, InterruptedException {
        List<Event> todaysWorkingEvents = getTodaysWorkingEvents();
        if (todaysWorkingEvents.isEmpty()) {
            System.out.println("no working events today :C");
            handleMetaDataFile(false);
            return;
        }
        // sprawdzamy czy nie jestesmy w sesji jakiegos bo jak tak to we prolly should start sesja
        Optional<Event> currentWorkingEvent = todaysWorkingEvents.stream().filter(e1 -> e1.getStart().getDateTime().getValue() <= System.currentTimeMillis() && System.currentTimeMillis() <= e1.getEnd().getDateTime().getValue())
                .findFirst();
        if(currentWorkingEvent.isPresent()) {
            websiteBlockerService.block(currentWorkingEvent.get());
            System.out.println("we blokin");
            return;
        }
        handleMetaDataFile(false);
        // jak nie jestesmy w zadnej sesji to czekamy na najblizszy
        Optional<Event> closestWorkingEvent = todaysWorkingEvents.stream()
                .filter(e1 -> e1.getStart().getDateTime().getValue() >= System.currentTimeMillis())
                .max((e1, e2) -> Math.toIntExact(e2.getStart().getDateTime().getValue() - e1.getStart().getDateTime().getValue()));

        if(closestWorkingEvent.isPresent()) {
            WebsiteBlockerService websiteBlockerService = new WebsiteBlockerService();
            websiteBlockerService.waitAndBlock(closestWorkingEvent.get());
            System.out.println("we waiting and blocking after " + (closestWorkingEvent.get().getStart().getDateTime().getValue()-System.currentTimeMillis()) +" millis");
            return;
        }
        List<Event> remainingEventsForToday = todaysWorkingEvents.stream().filter(e1 -> e1.getStart().getDateTime().getValue() >= System.currentTimeMillis()).toList();
        if(!remainingEventsForToday.isEmpty()){
            processEvents();
        }
    }

    public IsDuringSessionDTO isInWorkingSession() {
        long currentTimeMillis = System.currentTimeMillis();  // Pobierz bieżący czas w milisekundach
        DateTime currentTime = new DateTime(currentTimeMillis);  // Konwertuj na DateTime (Google API)

        for (Event event : eventList) {
            DateTime startTime = event.getStart().getDateTime();  // Czas rozpoczęcia
            DateTime endTime = event.getEnd().getDateTime();      // Czas zakończenia


            if (startTime == null && endTime == null) {
                // Całodniowe, więc do końca dzisiejszego dnia
                LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
                long remainingMinutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), endOfDay);
                return new IsDuringSessionDTO(true, remainingMinutes);
            }

            // Wydarzenie bez startu, ale z zakończeniem
            if (startTime == null && endTime != null) {
                if (currentTime.getValue() <= endTime.getValue()) {
                    long remainingMinutes = ChronoUnit.MINUTES.between(LocalDateTime.now(),
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime.getValue()), ZoneId.systemDefault()));
                    return new IsDuringSessionDTO(true, remainingMinutes);
                }
            }

            // Wydarzenie ze startem, ale bez zakończenia
            if (startTime != null && endTime == null) {
                if (currentTime.getValue() >= startTime.getValue()) {
                    // Brak zakończenia, więc zakładamy, że trwa bez końca
                    return new IsDuringSessionDTO(true, -1); // -1 oznacza brak końca
                }
            }

            // Wydarzenie z określonym startem i zakończeniem
            if (startTime != null && endTime != null) {
                if (currentTime.getValue() >= startTime.getValue() && currentTime.getValue() <= endTime.getValue()) {
                    long remainingMinutes = ChronoUnit.MINUTES.between(LocalDateTime.now(),
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime.getValue()), ZoneId.systemDefault()));
                    return new IsDuringSessionDTO(true, remainingMinutes);
                }
            }
        }
        return new IsDuringSessionDTO(false, 0);
    }
}
