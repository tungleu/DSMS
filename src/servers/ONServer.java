package servers;

import DSMS.DSMSImplement;
import common.Province;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ONServer {
    public static void main(String[] args) throws IOException, AlreadyBoundException {
        DSMSImplement server = new DSMSImplement(Province.ON);
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(1111);
            registry.list( );

        }
        catch (RemoteException e){
            registry = LocateRegistry.createRegistry(1111);
        }
        registry.bind("ON", server);
        System.out.println("ON_Server has started");
        server.receive();
    }
}
