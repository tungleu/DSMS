package DSMS;

import DSMS.DSMSInteface;
import common.Province;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class managerClient {
    private String managerID;
    private Province province;
    public managerClient(Province province, String managerID) throws Exception {
        this.province = province;
        this.managerID = managerID;
    }

    public String getID(){
        return this.managerID;
    }
}
