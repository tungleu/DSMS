package clients;

import common.Province;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        while(option != 1 && option != 2){
            System.out.println("Please choose valid option");
            option = scanner.nextInt();
        }
        System.out.println("Please enter your id");
        String IDNumber = scanner.next();
        String clientID = null;
        if(option == 1){
            clientID = province.toString() + "U" + IDNumber;
            System.out.println("Your ID is :"+ clientID);
            Customer customer = new Customer(clientID,province);
            int customerOption;
            String itemID;
            String inputDate;
            String itemName;
            DateFormat format = new SimpleDateFormat("MMMM d, yyyy");
            Date date;
            while(true){
                System.out.println("Please choose your action ");
                System.out.println("1.Purchase Item");
                System.out.println("2. Find Item ");
                System.out.println("3. Return Item ");
                customerOption = scanner.nextInt();
                switch(customerOption){
                    case 1:
                        System.out.println("PURCHASE SELECTED");
                        System.out.println("Enter item ID");
                        itemID = scanner.next();
                        scanner.nextLine();
                        System.out.println("Enter the date of purchase in this form: MMMM dd, yyyy ");
                        inputDate = scanner.nextLine();
                        date = format.parse(inputDate);
                        customer.purchaseItem(itemID,date);
                        break;
                    case 2:
                        System.out.println("FIND ITEM SELECTED");
                        System.out.println("Enter the name of item:");
                        itemName = scanner.next();
                        customer.findItem(itemName);
                        break;
                    case 3:
                        System.out.println("RETURN ITEM SELECTED");
                        System.out.println("Enter item ID");
                        itemID = scanner.next();
                        scanner.nextLine();
                        System.out.println("Enter the date of return in this form: MMMM dd, yyyy ");
                        inputDate = scanner.nextLine();
                        date = format.parse(inputDate);
                        customer.returnItem(itemID,date);
                        break;
                }
            }


        }
        else{
            clientID = province.toString() + "M" + IDNumber;
            System.out.println("Your ID is :"+ clientID);
            Manager manager = new Manager(clientID,province);
            int managerOption;
            String itemID;
            String inputDate;
            String itemName;
            int price, quantity;
            DateFormat format = new SimpleDateFormat("MMMM d, yyyy");
            Date date = null;
            while(true){
                System.out.println("Please choose your action ");
                System.out.println("1. Add item ");
                System.out.println("2, Remove item ");
                System.out.println("3. List item availability");
                managerOption = scanner.nextInt();
                switch(managerOption){
                    case 1:
                        System.out.println("ADD ITEM SELECTED");
                        System.out.println("Enter item ID");
                        itemID = scanner.next();
                        System.out.println("Enter item name");
                        itemName = scanner.next();
                        System.out.println("Enter price");
                        price = scanner.nextInt();
                        System.out.println("Enter quantity");
                        quantity = scanner.nextInt();
                        manager.addItem(itemID,itemName,quantity, price);
                        break;
                    case 2:
                        System.out.println("REMOVE ITEM SELECTED");
                        System.out.println("Enter item ID");
                        itemID = scanner.next();
                        System.out.println("Enter quantity");
                        quantity = scanner.nextInt();
                        manager.removeItem(itemID,quantity);
                        break;
                    case 3:
                        System.out.println("LIST ITEM AVAILABILITY SELECTED");
                        manager.listItemAvailability();
                        break;
                }
            }
        }
    }
}
