package models;

import java.util.Date;
import java.util.ArrayList;


public class BaseUser {
    @com.google.gson.annotations.SerializedName("Id")
    private String Id;
    @com.google.gson.annotations.SerializedName("email")
    private String email;
    @com.google.gson.annotations.SerializedName("password")
    private String password;
    @com.google.gson.annotations.SerializedName("authenticated")
    private boolean authenticated;
    @com.google.gson.annotations.SerializedName("verified")
    private boolean verified;
    @com.google.gson.annotations.SerializedName("supervisor")
    private String supervisor;
    @com.google.gson.annotations.SerializedName("firstname")
    private String firstname;
    @com.google.gson.annotations.SerializedName("lastname")
    private String lastname;
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
    @com.google.gson.annotations.SerializedName("ReferralContact")
    private String ReferralContact;

    private ArrayList<Area> areas;

    private ArrayList<GPS> Gps;

    public BaseUser() {

    }
    // constructor for register user
    public BaseUser(String e,String p,boolean v,String f,String l,Date d,int u,String ph,String r){
        this.email = e;
        this.password = p;
        this.UserLevel = u;
        this.firstname = f;
        this.lastname = l;
        this.DOB = d;
        this.AddressId = null;
        this.Phone = ph;
        this.SmartPhoneUser = true;
        this.supervisor = null;
        this.verified = v;
        this.ReferralName = r;
    }

    // constructor for log in
    public BaseUser(String em,String pw){
        this.email = em;
        this.password = pw;
        this.UserLevel = 0;
        this.firstname = null;
        this.lastname = null;
        this.DOB = null;
        this.AddressId = null;
        this.Phone = null;
        this.SmartPhoneUser = true;
        this.supervisor = null;
        this.verified = false;
        this.ReferralName = null;
    }

    // user level Standard = 0, Admin = 1, Garda = 2
    public BaseUser(String id, String email, String password, String supervisor, boolean verified, String firstName, String lastName, Date dob,
                    String phone, boolean smartPhoneUser, int userLevel, String addressId,
                    String referralName) {
        this.Id=id;
        this.email = email;
        this.password = password;
        this.UserLevel = userLevel;
        this.firstname = firstName;
        this.lastname = lastName;
        this.DOB = dob;
        this.AddressId = addressId;
        this.Phone = phone;
        this.SmartPhoneUser = smartPhoneUser;
        this.supervisor = supervisor;
        this.verified = verified;
        this.ReferralName = referralName;
        /*ArrayList<Area> areas, ArrayList<GPS> gps
        areas = new ArrayList<Area>(areas);
        Gps = new ArrayList<GPS>(gps);*/
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
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

    public String getReferralContact() {
        return ReferralContact;
    }

    public ArrayList<Area> getAreas() {
        return areas;
    }

    public ArrayList<GPS> getGps() {
        return Gps;
    }

    public void setEmail(String email) {
        email = email;
    }

    public void setPassword(String password) {
        password = password;
    }

    public void setSupervisor(String supervisor) {
        supervisor = supervisor;
    }

    public void setVerified(boolean verified) {
        verified = verified;
    }

    public void setFirstName(String firstName) {
        firstName = firstName;
    }

    public void setLastName(String lastName) {
        lastName = lastName;
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

    public void setReferralContact(String referralContact) {
        ReferralContact = referralContact;
    }

    public void setAreas(ArrayList<Area> areas) {
        areas = areas;
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
        return o instanceof BaseUser && ((BaseUser) o).Id == Id;
    }

}


