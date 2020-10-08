package servers;

import clients.customerClient;
import clients.managerClient;

import java.beans.Customizer;
import java.io.IOException;
import java.net.SocketException;
import java.rmi.*;
import java.util.Date;
import java.util.HashMap;

public interface DSMSInteface extends Remote {
    //Manager operations
    public boolean addItem(String managerID, String itemID, String itemName, int quantity, int price) throws RemoteException;
    public boolean removeItem(String managerID, String itemID, int quantity) throws RemoteException;
    public String listItemAvailability(String managerID) throws RemoteException;

    //User operations
    public String purchaseItem(String customerID, String itemID, Date dateOfPurchase) throws RemoteException,IOException, NotBoundException;
    public String findItem(String customerID, String itemName) throws RemoteException, IOException, NotBoundException;
    public boolean returnItem(String customerID, String itemID, Date dateOfReturn) throws RemoteException;


    //Support operations
    public String findLocalItem(String itemID) throws RemoteException;
    public String purchaseLocalItem(String customerID, String itemID, Date dateOfPurchase) throws RemoteException;
    public String purchaseFromOutside(String customerID, int budget, String itemID, Date dateOfPurchase) throws RemoteException;
    public String returnItemfromOutside(String customerID, String itemID, Date dateOfReturn);
    public void addWaitList(String customerID, String itemID) throws RemoteException;
//    public String addWaitList(String customerID, String itemID);



    public void addCustomerClient(customerClient customerClient) throws RemoteException;
    public void addManagerClient(managerClient managerClient) throws  RemoteException;

}
