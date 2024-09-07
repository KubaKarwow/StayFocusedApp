package App.dtos;

import java.time.Period;
import java.time.temporal.ChronoUnit;

public class IsDuringSessionDTO {
    private boolean isInSession;
    private long milisUntilEndOfSession;
    public IsDuringSessionDTO(boolean isInSession, long milisUntilEndOfSession) {
        this.isInSession = isInSession;
        this.milisUntilEndOfSession = milisUntilEndOfSession;
    }

    public boolean isInSession() {
        return isInSession;
    }

    public void setInSession(boolean inSession) {
        isInSession = inSession;
    }

    public long getMilisUntilEndOfSession() {
        return milisUntilEndOfSession;
    }

    public void setMilisUntilEndOfSession(long milisUntilEndOfSession) {
        this.milisUntilEndOfSession = milisUntilEndOfSession;
    }
}
