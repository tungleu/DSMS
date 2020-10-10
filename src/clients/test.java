package clients;

import DSMS.managerClient;
import common.Province;

import java.util.Date;

public class test {
    public static void main(String[] args) throws Exception {
//        Manager manager = new Manager("BCM1111", Province.BC);
//        manager.addItem("BC1234","TUNGLEU",2,20);
//        manager.addItem("BC3456","LMAO",3,200);
//
        Manager manager = new Manager("BCM1111", Province.BC);
        manager.addItem("BC1234","TUNGLEU",0,20);
        manager.listItemAvailability();
        Customer customer = new Customer("ONU1111",Province.ON);
        customer.purchaseItem("BC1234", new Date());
        manager.listItemAvailability();
    }
}
