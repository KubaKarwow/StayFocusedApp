package App.dtos;

public class PomodoroSession {
    private int amountOfWorkingSeconds;
    private int amountOfBreakSeconds;


    public PomodoroSession(int amountOfWorkingSeconds, int amountOfBreakSeconds) {
        this.amountOfWorkingSeconds = amountOfWorkingSeconds;
        this.amountOfBreakSeconds = amountOfBreakSeconds;
    }

    public int getAmountOfWorkingSeconds() {
        return amountOfWorkingSeconds;
    }

    public void setAmountOfWorkingSeconds(int amountOfWorkingSeconds) {
        this.amountOfWorkingSeconds = amountOfWorkingSeconds;
    }

    public int getAmountOfBreakSeconds() {
        return amountOfBreakSeconds;
    }

    public void setAmountOfBreakSeconds(int amountOfBreakSeconds) {
        this.amountOfBreakSeconds = amountOfBreakSeconds;
    }

    @Override
    public String toString() {
        return "PomodoroSession{" +
                "amountOfWorkingSeconds=" + amountOfWorkingSeconds +
                ", amountOfBreakSeconds=" + amountOfBreakSeconds +
                '}';
    }
}
