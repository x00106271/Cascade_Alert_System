package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Alert{

    public String Id;

public String Title;

public String Body;

public boolean Complete;

public boolean Active;

public String CreatedBy;

public boolean Broadcast;

public int Priority;

public Date StartDateTime;

public Date EndDateTime;

public ArrayList<User> RespondingGarda;

public ArrayList<Area> Areas;

public Map<String, Integer> Recipients;

public Map<String, Integer> ReadBy;

public Map<String, Integer> AcceptedBy;

public ArrayList<MediaAsset> MediaAssets;

public Alert() {

        }

public Alert(String itemId, String createdBy, String title, String body,
        Date startDateTime, Date endDateTime,
        boolean broadcast, int priority, boolean active, boolean complete)
        {
        this.Id = itemId;
        this.CreatedBy = createdBy;
        this.Title = title;
        this.Body = body;
        this.StartDateTime = startDateTime;
        this.EndDateTime = endDateTime;
        this.Broadcast = broadcast;
        this.Priority = priority;
        this.Active = active;
        this.Complete = complete;

        Areas = new ArrayList<Area>();
        RespondingGarda = new ArrayList<User>();
        Recipients = new HashMap<String, Integer>();
        ReadBy = new HashMap<String, Integer>();
        AcceptedBy = new HashMap<String, Integer>();
        MediaAssets = new ArrayList<MediaAsset>();
        }

    @Override
    public boolean equals(Object o) {
        return o instanceof Alert && ((Alert) o).Id == Id;
    }


}
