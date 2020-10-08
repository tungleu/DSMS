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

public class customerClient {
    private String customerID;
    private int budget = 1000;
    private Province province;
    private Registry registry = LocateRegistry.getRegistry("1111");
    private PrintWriter pw ;
    public customerClient(Province province, String customerID) throws Exception {
        if(!customerID.substring(0,2).equals(this.province.toString()) && customerID.charAt(3) == 'U'){
            throw new Exception("Incorrect ID format");
        }
        this.province = province;
        this.customerID = customerID;
        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        server.addCustomerClient(this);
        File file = new File("src/logs/ClientLogs/" + this.customerID + "_CLient.log");
        FileWriter fw = new FileWriter(file);
        this.pw = new PrintWriter(fw);
    }
    public String purchaseItem(String itemID, Date date) throws IOException, NotBoundException {

        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        return server.purchaseItem(this.customerID, itemID, date);
    }
    public HashMap<String, String> findItem(){
        return null;
    }
    public void returnItem(){

    }
    public String getID(){
        return this.customerID;
    }
    public int getBudget(){
        return this.budget;
    }
    public void setBudget(int budget){
        this.budget = budget;
    }
    public void log(String message) {
        this.pw.println(helper.current_time() + ": " + message);
    }


}
