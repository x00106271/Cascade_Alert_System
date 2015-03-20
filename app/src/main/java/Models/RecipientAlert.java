package models;

public class RecipientAlert{

public String AlertId;

// RecipientType user = 0, group = 1, area = 2, all = 3
public int RecipientType;

// recipientId = null then broadcast
public String RecipientId;

public RecipientAlert()
        {

        }

public RecipientAlert(String itemId, Alert alert, int recipientType, String recipientId)
        {
        // this.Id = itemId;
        // this.AlertId = alert.Id;
        this.RecipientType = recipientType;
        this.RecipientId = recipientId;
        }

        }
