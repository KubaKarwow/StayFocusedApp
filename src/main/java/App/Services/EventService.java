package App.Services;

import App.dtos.IsDuringSessionDTO;
import App.dtos.PomodoroSession;
import App.dtos.ProcessEventsDTO;
import App.utils.MetaDataFileHandler;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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
    private List<PomodoroSession> getPomodoroSessionsFromTimeFrame(long amountOfSeconds){
        int amountOfSessions = (int) (amountOfSeconds/1800);
        System.out.println("AMOUNT OF SECONDS " + amountOfSeconds);
        System.out.println("AMOUNT OF SESSIONS " + amountOfSessions);
        List<PomodoroSession> result = new ArrayList<>();
        for(int i = 0; i < amountOfSessions; i++){
            if(amountOfSeconds<=1500){
                result.add(new PomodoroSession((int)amountOfSeconds,0));
            } else if(amountOfSeconds > 1500 && amountOfSeconds <=1800){
                result.add(new PomodoroSession(1500,(int)amountOfSeconds-1500));
            }
            result.add(new PomodoroSession(1500,300));
            amountOfSeconds-=1800;
        }
        if(amountOfSeconds<=1500){
            result.add(new PomodoroSession((int)amountOfSeconds,0));
        } else if(amountOfSeconds > 1500 && amountOfSeconds <=1800){
            result.add(new PomodoroSession(1500,(int)amountOfSeconds-1500));
        }
        return result;
    }
    public ProcessEventsDTO processEvents() throws IOException, InterruptedException {
        List<Event> todaysWorkingEvents = getTodaysWorkingEvents();
        if(checkIfNoEventsToday(todaysWorkingEvents)){
            System.out.println("no working events today :C");
            updateStateOfMetadataFile(false);
            return new ProcessEventsDTO("No working events today.",null,0);
        }
        // sprawdzamy czy nie jestesmy w sesji jakiegos bo jak tak to we prolly should start sesja
        Optional<Event> currentSession = getCurrentSession(todaysWorkingEvents);
        if(currentSession.isPresent()) {
            websiteBlockerService.block(currentSession.get());
            System.out.println("we blokin");
            long amountOfSecondsOfBLock = (currentSession.get().getEnd().getDateTime().getValue()-System.currentTimeMillis()) /1000;
            return new ProcessEventsDTO("applied blocks", getPomodoroSessionsFromTimeFrame(amountOfSecondsOfBLock),0);
        } else{
            updateStateOfMetadataFile(false);
        }
        // jak nie jestesmy w zadnej sesji to czekamy na najblizszy
        Optional<Event> closestWorkingSession = getClosestWorkingSession(todaysWorkingEvents);
        if(closestWorkingSession.isPresent()) {
            WebsiteBlockerService websiteBlockerService = new WebsiteBlockerService();
            websiteBlockerService.waitAndBlock(closestWorkingSession.get());
            long amountOfSecondsOfBlock=(closestWorkingSession.get().getEnd().getDateTime().getValue()-System.currentTimeMillis())/1000;
            System.out.println("we waiting and blocking after " + (closestWorkingSession.get().getStart().getDateTime().getValue()-System.currentTimeMillis()) +" millis");
            return new ProcessEventsDTO("waiting and starting a block at " + formatDate(closestWorkingSession.get().getStart().getDateTime())
                    ,getPomodoroSessionsFromTimeFrame(amountOfSecondsOfBlock), (closestWorkingSession.get().getStart().getDateTime().getValue()-System.currentTimeMillis()));
        }
        List<Event> remainingEventsForToday = todaysWorkingEvents.stream().filter(e1 -> e1.getStart().getDateTime().getValue() >= System.currentTimeMillis()).toList();
        if(!remainingEventsForToday.isEmpty()){
            processEvents();
        }
        return new ProcessEventsDTO("No remaining events today.",null,0);
    }

    private String formatDate(DateTime dateTime){
        String dateTimeString = dateTime.toString();
        int startIndex = dateTimeString.indexOf('T');
        int endIndex = dateTimeString.indexOf('.');
        return dateTime.toString().substring(startIndex+1, endIndex);
    }

}
