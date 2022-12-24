package api.registration;


public class Registration {
    private String email;
    private String password;

    public Registration(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

