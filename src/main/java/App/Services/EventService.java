package App.Services;

import App.dtos.IsDuringSessionDTO;
import App.utils.MetaDataFileHandler;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

    private void updateStateOfMetadataFile(boolean shouldBeBlocked) throws IOException, InterruptedException {
        MetaDataFileHandler metaDataFileHandler = new MetaDataFileHandler();
        String currentState = metaDataFileHandler.getCurrentState();
        if(!shouldBeBlocked && currentState.equals("blocked")){
            websiteBlockerService.unblock();
            metaDataFileHandler.handleMetaDataFile("unblocked");
        }
    }

    private boolean checkIfNoEventsToday(List<Event> todaysWorkingEvents){
        return todaysWorkingEvents.isEmpty();
    }

    private Optional<Event> getCurrentSession(List<Event> todaysWorkingEvents){
        return todaysWorkingEvents.stream()
                .filter(e1 -> e1.getStart().getDateTime().getValue() <= System.currentTimeMillis()
                        && System.currentTimeMillis() <= e1.getEnd().getDateTime().getValue())
                .findFirst();
    }

    private Optional<Event> getClosestWorkingSession(List<Event> todaysWorkingEvents){
        return todaysWorkingEvents.stream()
                .filter(e1 -> e1.getStart().getDateTime().getValue() >= System.currentTimeMillis())
                .max((e1, e2) -> Math.toIntExact(e2.getStart().getDateTime().getValue() - e1.getStart().getDateTime().getValue()));
    }

    public String processEvents() throws IOException, InterruptedException {
        List<Event> todaysWorkingEvents = getTodaysWorkingEvents();
        if(checkIfNoEventsToday(todaysWorkingEvents)){
            System.out.println("no working events today :C");
            updateStateOfMetadataFile(false);
            return "No working events today.";
        }
        // sprawdzamy czy nie jestesmy w sesji jakiegos bo jak tak to we prolly should start sesja
        Optional<Event> currentSession = getCurrentSession(todaysWorkingEvents);
        if(currentSession.isPresent()) {
            websiteBlockerService.block(currentSession.get());
            System.out.println("we blokin");
            return "applied blocks";
        } else{
            updateStateOfMetadataFile(false);
        }
        // jak nie jestesmy w zadnej sesji to czekamy na najblizszy
        Optional<Event> closestWorkingSession = getClosestWorkingSession(todaysWorkingEvents);
        if(closestWorkingSession.isPresent()) {
            WebsiteBlockerService websiteBlockerService = new WebsiteBlockerService();
            websiteBlockerService.waitAndBlock(closestWorkingSession.get());
            System.out.println("we waiting and blocking after " + (closestWorkingSession.get().getStart().getDateTime().getValue()-System.currentTimeMillis()) +" millis");
            return "waiting and starting a block at " + formatDate(closestWorkingSession.get().getStart().getDateTime());
        }
        List<Event> remainingEventsForToday = todaysWorkingEvents.stream().filter(e1 -> e1.getStart().getDateTime().getValue() >= System.currentTimeMillis()).toList();
        if(!remainingEventsForToday.isEmpty()){
            processEvents();
        }
        return "No remaining events today.";
    }

    private String formatDate(DateTime dateTime){
        String dateTimeString = dateTime.toString();
        int startIndex = dateTimeString.indexOf('T');
        int endIndex = dateTimeString.indexOf('.');
        return dateTime.toString().substring(startIndex+1, endIndex);
    }

}
