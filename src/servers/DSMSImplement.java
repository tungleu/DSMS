package servers;
import clients.customerClient;
import clients.managerClient;

import java.beans.Customizer;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;


public class DSMSImplement extends UnicastRemoteObject implements DSMSInteface {

    private Map<String, String> store = new HashMap<String, String>();
    private Map<String, PriorityQueue<String>> waitlist = new HashMap<String, PriorityQueue<String>>();
    private Province province;
    private PrintWriter pw;
    private HashMap<String, customerClient> customerClients = new HashMap<String, customerClient>();
    private HashMap<String, managerClient> managerClients = new HashMap<String, managerClient>();
    private HashMap<String, Integer> portMap = new HashMap<String, Integer>();
    private ArrayList<String> purchaseLog = new ArrayList<String>();
    protected DSMSImplement(Province province, int port) throws IOException {
        super();
        this.province = province;
        File file = new File("src/logs/ServerLogs/" + this.province + "_Server.log");
        FileWriter fw = new FileWriter(file);
        this.pw = new PrintWriter(fw);
        this.portMap.put("QC", 1111);
        this.portMap.put("ON", 2222);
        this.portMap.put("BC", 3333);
        this.receive();
    }

    public void log(String message) {
        this.pw.println(helper.current_time() + ": " + message);
    }

    @Override
    public boolean addItem(String managerID, String itemID, String itemName, int quantity, int price) throws RemoteException  {
        if (this.store.containsKey(itemID)) {
            String[] info = this.store.get(itemID).split(",");
            info[1] = Integer.toString(Integer.parseInt(info[1]) + quantity);
            store.replace(itemID, String.join(",", info));
            this.log("Manager with id: " + managerID + "updated store info on this item:" + itemID);
            return true;
        } else {
            store.put(itemID, itemName + "," + quantity + "," + price);
            this.log("Manager with id:" + managerID + "added new item into the store:" + itemID);
            return true;
        }

    }


    @Override
    public boolean removeItem(String managerID, String itemID, int quantity) throws RemoteException {
        if (this.store.containsKey(itemID)) {
            String[] info = this.store.get(itemID).split(",");
            info[1] = Integer.toString(Integer.parseInt(info[1]) + quantity);
            if (Integer.parseInt(info[1]) <= 0) {
                this.store.remove(itemID);
                this.log("Manager with id:" + managerID + "removed this item out of the store:" + itemID);
            } else {
                store.replace(itemID, String.join(",", info));
                this.log("Manager with id:" + managerID + "decreased quantity of this item out of the store:" + itemID);
            }
            return true;
        } else {
            System.out.println("The given item ID doesn't exist");
            this.log("Manager with id:" + managerID + "gave non existent itemID");
            return false;
        }
    }

    @Override
    public String listItemAvailability(String managerID) throws RemoteException {
        this.log("Manager with id: " + managerID + "requested for listItemAvailability()");
        String result = "";
        for (Map.Entry<String,String> entry: this.store.entrySet()){
            result += entry.getKey() + ":" +entry.getValue() +"\n";
        }
        return result;
    }

    @Override
    public String purchaseItem(String customerID, String itemID, Date dateOfPurchase) throws RemoteException,IOException, NotBoundException {
        String serverName = itemID.substring(0,2);
        if (serverName.equals(this.province.toString())) {
            this.log("Customer with id: " + customerID + "requested to purchase an item in local shop");
            return this.purchaseLocalItem(customerID, itemID, dateOfPurchase);
        } else {
            this.log("Customer with id: " + customerID + "requested to purchase an item in "+serverName + " store");
            this.log("Sending UDP mesasge to "+serverName +" store");
            String message = "PURCHASE, "+ ","+ customerID +","+ customerClients.get(customerID).getBudget()+"," + dateOfPurchase.toString() ;
            return this.sendMessage(this.portMap.get(itemID.substring(0,2)),message);
        }
    }


    @Override
    public String purchaseLocalItem(String customerID, String itemID, Date dateOfPurchase) throws RemoteException {
        customerClient customer = customerClients.get(customerID);
        if (this.store.containsKey(itemID)) {
            String[] info = this.store.get(itemID).split(",");
            if(Integer.parseInt(info[1]) > 0){
                if(customer.getBudget() > Integer.parseInt(info[2])){
                    customer.setBudget(customer.getBudget() - Integer.parseInt(info[2]));
                    info[1] = Integer.toString(Integer.parseInt(info[1]) -1);
                    store.replace(itemID, String.join(",", info));
                    this.purchaseLog.add(itemID+","+customerID+","+dateOfPurchase.toString());
                    this.log("Customer with id: " + customerID + "purchased successfully");
                    return "SUCCESSFUL";
                }
                else{
                    this.log("Customer with id: " + customerID + "is out of budget");
                    return "OUT OF BUDGET";
                }
            }
            else{
                this.log("Customer with id: " + customerID + "is out of budget");
                return "OUT OF STOCK";
            }
        } else {
            System.out.println("The given item ID doesn't exist");
            return "WRONG ID";
        }
    }

    @Override
    public String purchaseFromOutside(String customerID, int budget, String itemID, Date dateOfPurchase) throws RemoteException {
        if (this.store.containsKey(itemID)) {
            String[] info = this.store.get(itemID).split(",");
            if(Integer.parseInt(info[1]) > 0){
                if(budget > Integer.parseInt(info[2])){
                    int returnBudget = budget- Integer.parseInt(info[2]);
                    info[1] = Integer.toString(Integer.parseInt(info[1]) -1);
                    store.replace(itemID, String.join(",", info));
                    this.purchaseLog.add(itemID+","+customerID+","+dateOfPurchase.toString());
                    return "SUCCESSFUL, "+returnBudget;
                }
                else{
                    return "OUT OF BUDGET";
                }
            }
            else{
                return "OUT OF STOCK";
            }
        } else {
            System.out.println("The given item ID doesn't exist");
            return "WRONG ID";
        }
    }

    @Override
    public String returnItemfromOutside(String customerID, String itemID, Date dateOfReturn) {
        for(String log : this.purchaseLog){
            String[] logParams = log.split(",");
            if(logParams[0].equals(itemID) && logParams[1].equals(customerID)){
                Date datePurchased = new Date(logParams[2]);
                long day30 = 30l * 24 * 60 * 60 * 1000;
                if(dateOfReturn.compareTo(new Date(datePurchased.getTime()+day30)) >0){
                    return "FALSE";
                }
                else{
                    String[] info = this.store.get(itemID).split(",");
                    info[1] = Integer.toString(Integer.parseInt(info[1]) +1);
                    store.replace(itemID, String.join(",", info));
                    this.log("Customer with id: " + customerID + "returned this item:" + itemID);
                    String price = this.store.get(itemID).split(",")[1];
                    return "TRUE"+ price;
                }
            }
        }
        return "FALSE";
    }

    @Override
    public void addWaitList(String customerID, String itemID) throws RemoteException {
        if (itemID.substring(0, 2).equals(this.province.toString())) {
            this.addLocalWaitList(customerID, itemID);
        }
        else{
            int port = this.portMap.get(itemID.substring(0,2));
            String message = "WAITLIST," + customerID +","+ itemID;
            this.sendMessage(port,message);
        }
    }
    public void addLocalWaitList(String customerID, String itemID) throws RemoteException{
        if (this.waitlist.containsKey(itemID)) {
            this.waitlist.get(itemID).add(customerID);
        } else {
            PriorityQueue<String> queue = new PriorityQueue<String>();
            queue.add(customerID);
            this.waitlist.put(itemID, queue);
        }
        this.log("Update priority queue");
    }

    @Override
    public String findItem(String customerID, String itemName) throws IOException, NotBoundException {
        //Current store
        String result = this.findLocalItem(itemName);
        for(Map.Entry<String,Integer> entry: this.portMap.entrySet()){
            if(entry.getKey().equals(this.province.toString())){
                continue;
            }
            String message = "ITEM_INFO," + itemName;
            this.log("Server send UDP request for Item info");
            result = result +";"+this.sendMessage(entry.getValue(),message);
        }
        return result;
    }

    @Override
    public boolean returnItem(String customerID, String itemID, Date dateOfReturn) {
        customerClient customer = this.customerClients.get(customerID);
        if(itemID.substring(0,2).equals(this.province.toString())){
            for(String log : this.purchaseLog){
                String[] logParams = log.split(",");
                if(logParams[0].equals(itemID) && logParams[1].equals(customerID)){
                    Date datePurchased = new Date(logParams[2]);
                    long day30 = 30l * 24 * 60 * 60 * 1000;
                    if(dateOfReturn.compareTo(new Date(datePurchased.getTime()+day30)) >0){
                        return false;
                    }
                    else{
                        String[] info = this.store.get(itemID).split(",");
                        info[1] = Integer.toString(Integer.parseInt(info[1]) +1);
                        store.replace(itemID, String.join(",", info));
                        customer.setBudget(customer.getBudget()+Integer.parseInt(info[2]));
                        this.log("Customer with id: " + customerID + "returned this item:" + itemID);
                        return true;
                    }
                }
            }
        }
        else{
            int port = this.portMap.get(itemID.substring(0,2));
            this.log("Sending UDP request to return Item");
            String reply = this.sendMessage(port,"RETURN,"+itemID+","+customerID+","+dateOfReturn.toString());
            if(reply.startsWith("FALSE")){
                return false;
            }
            else if(reply.startsWith("TRUE")){
                customer.setBudget(customer.getBudget()+Integer.parseInt(reply.split(",")[2]));
                this.log("Customer with id: " + customerID + "returned this item:" + itemID);
                return true;
            }
        }
        return true;
    }



    public String findLocalItem(String itemName) throws RemoteException {
        String result = "";
        for (Map.Entry<String, String> entry : this.store.entrySet()) {
            if (itemName.equals(entry.getValue().split(",")[0])) {
                result = result + ", " + entry.getKey();
            }
        }
        return result;
    }




    @Override
    public void addCustomerClient(customerClient customerClient) throws RemoteException {
        this.customerClients.put(customerClient.getID(), customerClient);
    }



    @Override
    public void addManagerClient(managerClient managerClient) throws RemoteException {
        this.managerClients.put(managerClient.getID(), managerClient);
    }



    public String sendMessage(int serverPort, String messageToSend){
        DatagramSocket aSocket = null;
        String replyMessage = null;
        try {
            aSocket = new DatagramSocket();
            byte[] message = messageToSend.getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(message, messageToSend.length(), aHost, serverPort);
            aSocket.send(request);
            System.out.println("Request message sent from the client to server with port number " + serverPort + " is: "
                    + new String(request.getData()));
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

            aSocket.receive(reply);
            replyMessage = new String(reply.getData());
            System.out.println("Reply received from the server with port number " + serverPort + " is: "
                    + new String(reply.getData()));
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();

        }
        return replyMessage;
    }

    public void receive(){
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(this.portMap.get(this.province.toString()));
            byte[] buffer = new byte[1000];
            System.out.println("Server 8888 Started............");
            String replyMessage = null;
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                String[] requestParam = new String(request.getData()).split(",");
                if(requestParam[0].equals("PURCHASE")){
                    String customerID = requestParam[1];
                    int budget = Integer.parseInt(requestParam[2]);
                    String itemID = requestParam[3];
                    Date date = new Date(requestParam[4]);
                    replyMessage = this.purchaseFromOutside(customerID,budget,itemID, date);
                }else if(requestParam[0].equals("WAITLIST")){
                    //ADD WAITLIST TO OTHER SERVER
                    String customerID = requestParam[1];
                    String itemID = requestParam[2];
                    this.addLocalWaitList(customerID,itemID);
                }else if(requestParam[0].equals("ITEM_INFO")){
                    String itemName = requestParam[1];
                    replyMessage = this.findLocalItem(itemName);
                }else if(requestParam[0].equals("RETURN")){
                    String customerID = requestParam[1];
                    String itemID = requestParam[2];
                    Date dateOfReturn = new Date(requestParam[3]);
                    replyMessage = this.returnItemfromOutside(customerID,itemID,dateOfReturn);
                }
                DatagramPacket reply = new DatagramPacket(replyMessage.getBytes(), replyMessage.length(), request.getAddress(),
                        request.getPort());
                aSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }


}



