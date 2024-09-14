package App.dtos;

public class JokeResponseDTO {

    private String setup;
    private String delivery;
    private String joke;

    public JokeResponseDTO(String setup, String delivery) {
        this.joke = setup + " " + delivery;
    }

    public JokeResponseDTO(String joke) {
        this.joke = joke;
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
        return joke;
    }
}
