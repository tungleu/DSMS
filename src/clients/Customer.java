package clients;

import DSMS.DSMSInteface;
import common.Province;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Customer {
    private String customerID;
    private Province province;
    private Registry registry = null;
    private Logger logger = null;
    public Customer(String customerID, Province province) throws Exception {
        this.customerID = customerID;
        this.province = province;
        this.registry = LocateRegistry.getRegistry(1111);
        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        this.logger = startLogger();
        server.addCustomerClient(province,customerID);
        logger.info("Customer client is created with ID: "+ this.customerID);
    }
    public Logger startLogger() {
        Logger logger = Logger.getLogger("CustomerLog");
        FileHandler fh;
        try {
            fh = new FileHandler("src/logs/ClientLogs/"+this.customerID+"_Client.log");
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
    public synchronized void purchaseItem(String itemID, Date date) throws IOException, NotBoundException {
        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        String result = server.purchaseItem(this.customerID, itemID, date);
        logger.info("Customer client with ID: "+ customerID +" sent a request to purchase an item with ID: "+itemID);
        if(result.equals("SUCCESSFUL")){
            System.out.println("Item purchased successfully!");
        }
        else if(result.equals("OUT OF BUDGET")){
            System.out.println("Insufficient budget to purchase item! ");
        }
        else if(result.equals("OUT OF STOCK")){
            System.out.println("Item is out of stock, would you like to be in waitlist ?");
            Scanner scanner = new Scanner(System.in);
            String option = scanner.next();
            if(option.equals("yes")){
                server.addWaitList(this.customerID, itemID);
                System.out.println("Added to the waitlist");
            }
        }
        else if(result.equals("WRONG ID")){
            System.out.println("The given item id does not exist");

        }
        else{
            System.out.println(result);
        }

    }
    public synchronized void findItem (String itemName) throws IOException, NotBoundException {

        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        logger.info("Customer client with ID: "+ customerID +" sent a request to find item");
        System.out.println(server.findItem(this.customerID, itemName));
    }
    public synchronized void returnItem(String itemID, Date date) throws IOException, NotBoundException{
        DSMSInteface server = (DSMSInteface)registry.lookup(this.province.toString());
        boolean returnResult = server.returnItem(this.customerID,itemID,date);
        logger.info("Customer client with ID: "+ customerID +" sent a request to return item with ID: "+ itemID);
        if (returnResult){
            System.out.println("Return successful");
        }
        else{
            System.out.println("Return unsuccessful");
        }
    }
}
