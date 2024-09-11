package App.dtos;

public class PomodoroSession {
    private int amountOfWorkingMinutes;
    private int amountOfBreakMinutes;


    public PomodoroSession(int amountOfWorkingMinutes, int amountOfBreakMinutes) {
        this.amountOfWorkingMinutes = amountOfWorkingMinutes;
        this.amountOfBreakMinutes = amountOfBreakMinutes;
    }

    public int getAmountOfWorkingMinutes() {
        return amountOfWorkingMinutes;
    }

    public void setAmountOfWorkingMinutes(int amountOfWorkingMinutes) {
        this.amountOfWorkingMinutes = amountOfWorkingMinutes;
    }

    public int getAmountOfBreakMinutes() {
        return amountOfBreakMinutes;
    }

    public void setAmountOfBreakMinutes(int amountOfBreakMinutes) {
        this.amountOfBreakMinutes = amountOfBreakMinutes;
    }

    @Override
    public String toString() {
        return "PomodoroSession{" +
                "amountOfWorkingMinutes=" + amountOfWorkingMinutes +
                ", amountOfBreakMinutes=" + amountOfBreakMinutes +
                '}';
    }
}
