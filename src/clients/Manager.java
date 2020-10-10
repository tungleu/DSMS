package clients;

import DSMS.DSMSInteface;
import common.Province;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Manager {
    private String managerID;
    private Province province;
    private Registry registry = null;
    private Logger logger = null;
    public Manager(String managerID, Province province) throws Exception {
        this.managerID = managerID;
        this.province = province;
        this.registry = LocateRegistry.getRegistry(1111);
        this.logger = startLogger();
        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        server.addManagerClient(province, managerID);

    }
    public Logger startLogger() {
        Logger logger = Logger.getLogger("ManagerLog");
        FileHandler fh;
        try {
            fh = new FileHandler("src/logs/ClientLogs/"+this.managerID+"_Client.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logger;
    }
    public synchronized void addItem(String itemID, String itemName,int quantity, int price) throws IOException, NotBoundException {
        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        logger.info("Manager client with ID: "+ managerID +" sent a request to add an item with ID: "+itemID);
        if(server.addItem(this.managerID, itemID, itemName, quantity, price)){
            System.out.println("Added item successful");
        }
        else{
            System.out.println("Added item unsuccessful");
        }
    }
    public synchronized void removeItem(String itemID, int quantity) throws RemoteException, NotBoundException {
        logger.info("Manager client with ID: "+ managerID +" sent a request to remove an item with ID: "+itemID);
        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        if(server.removeItem(this.managerID, itemID, quantity)){
            System.out.println("Removed item successful");
        }
        else{
            System.out.println("Removed item unsuccessful");
        }
    }
    public synchronized void listItemAvailability() throws RemoteException, NotBoundException {
        logger.info("Manager client with ID: "+ managerID +" sent a request to listItemAvailability");
        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        System.out.println(server.listItemAvailability(this.managerID));
    }

}
