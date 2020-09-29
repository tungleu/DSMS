package servers;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class DSMSImplement extends UnicastRemoteObject implements DSMSInteface {
    private Registry registry = LocateRegistry("localhost", 3000);

    protected DSMSImplement() throws RemoteException {
        super();
    }

    @Override
    public boolean addItem(String maangerID, String itemID, String itemName, int quantity, int price) {
        return false;
    }

    @Override
    public boolean removeItem(String managerID, String itemID, int quantity) {
        return false;
    }

    @Override
    public String listItemAvailability(String managerID) {
        return null;
    }

    @Override
    public boolean purchaseItem(String customerID, String itemID) {
        return false;
    }
}
