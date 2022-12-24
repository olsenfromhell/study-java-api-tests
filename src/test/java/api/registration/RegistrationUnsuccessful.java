package api.registration;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

public class RegistrationUnsuccessful {
    private String error;

    public String getError() {
        return error;
    }
}
