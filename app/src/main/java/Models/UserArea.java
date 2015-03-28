package models;

public class UserArea {
    @com.google.gson.annotations.SerializedName("Id")
    private String Id;
    @com.google.gson.annotations.SerializedName("userid")
    public String userId;
    @com.google.gson.annotations.SerializedName("areaid")
    public String areaId;
    @com.google.gson.annotations.SerializedName("userisadmin")
    private boolean userIsAdmin;

public UserArea()
        {

        }

public UserArea(String itemId, String user,String area, boolean userIsAdmin)
        {
        this.Id = itemId;
        this.userId = user;
        this.areaId = area;
        this.userIsAdmin = userIsAdmin;
        }

    public UserArea(String user,String area, boolean userIsAdmin)
    {
        this.userId = user;
        this.areaId = area;
        this.userIsAdmin = userIsAdmin;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public void setUserIsAdmin(boolean userIsAdmin) {
        this.userIsAdmin = userIsAdmin;
    }

    public String getId() {
        return Id;
    }

    public String getUserId() {
        return userId;
    }

    public String getAreaId() {
        return areaId;
    }

    public boolean isUserIsAdmin() {
        return userIsAdmin;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UserArea && ((UserArea) o).Id == Id;
    }
}
