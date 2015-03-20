package models;



public class Address{

    private String Id;

    private String UserId;

    private int Number;

    public String Street;

    public String Town;

    public String PostCode;

    public String County;

    public Address()
    {

    }
    public Address(String id,String userid,int number, String street, String town, String postCode, String county){
        this.Id = id;
        this.UserId=userid;
        this.Number=number;
        this.Street = street;
        this.Town = town;
        this.PostCode = postCode;
        this.County = county;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public void setTown(String town) {
        Town = town;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getId() {
        return Id;
    }

    public String getUserId() {
        return UserId;
    }

    public int getNumber() {
        return Number;
    }

    public String getStreet() {
        return Street;
    }

    public String getTown() {
        return Town;
    }

    public String getPostCode() {
        return PostCode;
    }

    public String getCounty() {
        return County;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Address && ((Address) o).Id == Id;
    }
}