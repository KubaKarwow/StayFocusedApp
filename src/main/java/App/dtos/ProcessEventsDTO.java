package App.dtos;

import java.util.List;

public class ProcessEventsDTO {
    private String processingOutcome;
    private List<PomodoroSession> pomodoroSessions;
    private long timeToWaitTillTheBlockBegins;

    public ProcessEventsDTO(String processingOutcome, List<PomodoroSession> pomodoroSessions, long timeToWaitTillTheBlockBegins) {
        this.processingOutcome = processingOutcome;
        this.pomodoroSessions = pomodoroSessions;
        this.timeToWaitTillTheBlockBegins = timeToWaitTillTheBlockBegins;
    }

    public String getProcessingOutcome() {
        return processingOutcome;
    }

    public void setProcessingOutcome(String processingOutcome) {
        this.processingOutcome = processingOutcome;
    }

    public List<PomodoroSession> getPomodoroSessions() {
        return pomodoroSessions;
    }

    public void setPomodoroSessions(List<PomodoroSession> pomodoroSessions) {
        this.pomodoroSessions = pomodoroSessions;
    }



    public long getTimeToWaitTillTheBlockBegins() {
        return timeToWaitTillTheBlockBegins;
    }

    public void setTimeToWaitTillTheBlockBegins(long timeToWaitTillTheBlockBegins) {
        this.timeToWaitTillTheBlockBegins = timeToWaitTillTheBlockBegins;
    }
}
