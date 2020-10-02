package clients;

import servers.Province;

import java.rmi.registry.Registry;

public class managerClient {
    private String customerID;
    private Province province;
    public managerClient(Province province){
        this.province = province;

    }
}
