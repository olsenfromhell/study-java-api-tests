package api;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class RegistrationSuccess {
    private int id;
    private String token;

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
