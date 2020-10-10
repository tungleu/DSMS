package DSMS;

import common.Province;

import java.io.IOException;
import java.rmi.*;
import java.util.Date;

public interface DSMSInteface extends Remote {
    //Manager operations
    public boolean addItem(String managerID, String itemID, String itemName, int quantity, int price) throws IOException, NotBoundException;
    public boolean removeItem(String managerID, String itemID, int quantity) throws RemoteException;
    public String listItemAvailability(String managerID) throws RemoteException;

    //User operations
    public String purchaseItem(String customerID, String itemID, Date dateOfPurchase) throws RemoteException,IOException, NotBoundException;
    public String findItem(String customerID, String itemName) throws RemoteException, IOException, NotBoundException;
    public boolean returnItem(String customerID, String itemID, Date dateOfReturn) throws RemoteException;

    //Support operations
    public void addWaitList(String customerID, String itemID) throws RemoteException;
    public void addCustomerClient(Province province, String id) throws Exception;
    public void addManagerClient(Province province, String id) throws Exception;

}
