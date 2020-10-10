package servers;

import DSMS.DSMSImplement;
import common.Province;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BCServer {
    public static void main(String[] args) throws IOException, AlreadyBoundException {
        DSMSImplement server = new DSMSImplement(Province.BC);
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(1111);
            registry.list( );

        }
        catch (RemoteException e){
            registry = LocateRegistry.createRegistry(1111);
        }
        registry.bind("BC", server);
        System.out.println("BC_Server has started");
        server.receive();


    }

}
