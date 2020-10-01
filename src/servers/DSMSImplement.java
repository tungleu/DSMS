package servers;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;


public class DSMSImplement extends UnicastRemoteObject implements DSMSInteface {

    private Map<String, String> store = new HashMap<String, String>();
    private Map<String, ArrayList<String>> waitlist = new HashMap<String, ArrayList<String>>();
    private Province province;
    private PrintWriter pw;

    protected DSMSImplement(Province province) throws IOException {
        super();
        this.province = province;
        File file = new File("src/logs/ServerLogs/"+ this.province+"_Server.log");
        FileWriter fw = new FileWriter(file);
        this.pw = new PrintWriter(fw);
        this.pw.println(helper.current_time() + ":" +this.province + "Server started");

    }

    @Override
    public boolean addItem(String managerID, String itemID, String itemName, int quantity, int price) {

        if(this.store.containsKey(itemID)) {
            String[] info = this.store.get(itemID).split(",");
            info[1] = Integer.toString(Integer.parseInt(info[1]) + quantity);
            store.replace(itemID, String.join(",", info));
            this.pw.println(helper.current_time() + " : " + "Updated info on this item:" + itemID);
            return true;
        }
        else{
            store.put(itemID,itemName+","+quantity+""+price);
            this.pw.println(helper.current_time() + " : " + "Added new item into the store:" + itemID);
            return true;
        }


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
