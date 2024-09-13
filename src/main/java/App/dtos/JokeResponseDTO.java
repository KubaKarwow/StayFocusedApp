package App.dtos;

public class JokeResponseDTO {

    private String setup;
    private String delivery;

    public JokeResponseDTO(String setup, String delivery) {
        this.setup = setup;
        this.delivery = delivery;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    @Override
    public String toString() {
        return setup + " " + delivery;
    }
}
