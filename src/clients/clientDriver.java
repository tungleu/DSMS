package clients;

import servers.Province;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class clientDriver {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose your province: QC, ON, BC");
        String input = scanner.next();
        Province province = null;
        switch (input){
            case "ON":
                province = Province.ON;
                break;
            case "QC":
                province = Province.QC;
                break;
            case "BC":
                province = Province.BC;
                break;
        }
        System.out.println("Please specify your type of client");
        System.out.println("1. Customer client");
        System.out.println("2. Manager client");
        int option = scanner.nextInt();
        while(option != 1 || option != 2){
            System.out.println("Please choose valid option");
            option = scanner.nextInt();
        }
        System.out.println("Please enter your id");
        String IDNumber = scanner.next();
        String clientID = null;
        if(option == 1){
            clientID = province.toString() + "U" + clientID;
            customerClient customer = new customerClient(province, clientID);
            while(true){
                System.out.println("Please choose your action ");
                System.out.println("1.Purchase Item");
                System.out.println("2. Find Item ");
                System.out.println("3. Return Item ");
            }
        }
        else{
            clientID = province.toString() + "M" + clientID;
            managerClient managerClient = new managerClient(province);
            while(true){
                System.out.println("Please choose your action ");
                System.out.println("1. Add item ");
                System.out.println("2, Remove item ");
                System.out.println("3. List item availability");
            }
        }
    }
}
