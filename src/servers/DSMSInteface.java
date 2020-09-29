package servers;

import java.rmi.*;

public interface DSMSInteface extends Remote {
    //Manager operations
    public boolean addItem(String managerID, String itemID, String itemName, int quantity, int price);
    public boolean removeItem(String managerID, String itemID, int quantity);
    public String listItemAvailability(String managerID);

    //User operations
    public boolean purchaseItem(String customerID, String itemID);
}
