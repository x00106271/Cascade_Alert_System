package Model;

import java.util.ArrayList;
import java.util.Date;

public class User {
    @com.google.gson.annotations.SerializedName("Id")
    private String Id;
    @com.google.gson.annotations.SerializedName("Email")
    private String Email;
    @com.google.gson.annotations.SerializedName("Password")
    private String Password;
    @com.google.gson.annotations.SerializedName("Supervisor")
    private String Supervisor;
    @com.google.gson.annotations.SerializedName("Verified")
    private boolean Verified;
    @com.google.gson.annotations.SerializedName("FirstName")
    private String FirstName;
    @com.google.gson.annotations.SerializedName("LastName")
    private String LastName;
    @com.google.gson.annotations.SerializedName("DOB")
    private Date DOB;
    @com.google.gson.annotations.SerializedName("Phone")
    private String Phone;
    @com.google.gson.annotations.SerializedName("SmartPhoneUser")
    private boolean SmartPhoneUser;
    @com.google.gson.annotations.SerializedName("UserLevel")
    private int UserLevel;
    @com.google.gson.annotations.SerializedName("AddressId")
    private String AddressId;
    @com.google.gson.annotations.SerializedName("ReferralName")
    private String ReferralName;

    private ArrayList<Area> Areas;

    private ArrayList<GPS> Gps;

    public User() {

    }

    // user level Standard = 0, Admin = 1, Garda = 2
    public User(String id,String email, String password,String supervisor,boolean verified, String firstName, String lastName, Date dob,
                     String phone, boolean smartPhoneUser,int userLevel,String addressId,
                     String referralName) {
        this.Id=id;
        this.Email = email;
        this.Password = password;
        this.UserLevel = userLevel;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.DOB = dob;
        this.AddressId = addressId;
        this.Phone = phone;
        this.SmartPhoneUser = smartPhoneUser;
        this.Supervisor = supervisor;
        this.Verified = verified;
        this.ReferralName = referralName;
        /*ArrayList<Area> areas, ArrayList<GPS> gps
        Areas = new ArrayList<Area>(areas);
        Gps = new ArrayList<GPS>(gps);*/
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getSupervisor() {
        return Supervisor;
    }

    public boolean isVerified() {
        return Verified;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public Date getDOB() {
        return DOB;
    }

    public String getPhone() {
        return Phone;
    }

    public boolean isSmartPhoneUser() {
        return SmartPhoneUser;
    }

    public int getUserLevel() {
        return UserLevel;
    }

    public String getAddressId() {
        return AddressId;
    }

    public String getReferralName() {
        return ReferralName;
    }

    public ArrayList<Area> getAreas() {
        return Areas;
    }

    public ArrayList<GPS> getGps() {
        return Gps;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setSupervisor(String supervisor) {
        Supervisor = supervisor;
    }

    public void setVerified(boolean verified) {
        Verified = verified;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setSmartPhoneUser(boolean smartPhoneUser) {
        SmartPhoneUser = smartPhoneUser;
    }

    public void setUserLevel(int userLevel) {
        UserLevel = userLevel;
    }

    public void setAddressId(String addressId) {
        AddressId = addressId;
    }

    public void setReferralName(String referralName) {
        ReferralName = referralName;
    }

    public void setAreas(ArrayList<Area> areas) {
        Areas = areas;
    }

    public void setGps(ArrayList<GPS> gps) {
        Gps = gps;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof User && ((User) o).Id == Id;
    }

}


