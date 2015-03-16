package Model;

public class UserArea {
public String UserId;

public String AreaId;

public boolean UserIsAdmin;

public UserArea()
        {

        }

public UserArea(String itemId, BaseUser user, Area area, boolean userIsAdmin)
        {
        // this.Id = itemId;
        // this.UserId = user.Id;
        // this.AreaId = area.Id;
        this.UserIsAdmin = userIsAdmin;
        }
        }
