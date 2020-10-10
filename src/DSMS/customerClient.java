package DSMS;

import DSMS.DSMSInteface;
import common.Province;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Scanner;


public class customerClient{
    private String customerID;
    private int budget = 1000;
    private Province province;
    private PrintWriter pw ;
    public customerClient(Province province, String customerID) throws Exception {
        this.province = province;
        this.customerID = customerID;
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



}
