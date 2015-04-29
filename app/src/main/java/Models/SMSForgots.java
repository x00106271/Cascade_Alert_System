package models;


public class SMSForgots {
    @com.google.gson.annotations.SerializedName("Id")
    private String Id;
    @com.google.gson.annotations.SerializedName("email")
    private String email;


    public SMSForgots(){}

    public SMSForgots(String email){
        this.email=email;
    }

    public String getId() {
        return Id;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SMSForgots && ((SMSForgots) o).Id == Id;
    }
}
