package clients;

import servers.DSMSInteface;
import servers.Province;
import servers.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.HashMap;

public class managerClient {
    private String customerID;
    private Province province;
    private PrintWriter pw;
    private Registry registry = LocateRegistry.getRegistry("1111");
    public managerClient(Province province) throws IOException {
        this.province = province;
        File file = new File("src/logs/ClientLogs/" + this.customerID + "_CLient.log");
        FileWriter fw = new FileWriter(file);
        this.pw = new PrintWriter(fw);

    }
    public void addItem(String itemID, String itemName,int quantity, int price) throws RemoteException, NotBoundException {
        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        if(server.addItem(this.customerID, itemID, itemName, quantity, price)){
            this.log("Added item successful");
        }
        else{
            this.log("Added item unsuccessful");
        }
    }
    public void removeItem(String itemID, int quantity) throws RemoteException, NotBoundException {
        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        if(server.removeItem(this.customerID, itemID, quantity)){
            this.log("Removed item successful");
        }
        else{
            this.log("Removed item unsuccessful");
        }
    }
//    public HashMap<String, String> listTemAvailability() throws RemoteException, NotBoundException {
//        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
//        return server.listItemAvailability(this.customerID);
//    }
    public void log(String message) {
        this.pw.println(helper.current_time() + ": " + message);
    }
    public String getID(){
        return this.customerID;
    }
}
