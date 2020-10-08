package servers;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BCServer {
    public static void main(String[] args) throws IOException, AlreadyBoundException {
        DSMSImplement server = new DSMSImplement(Province.BC,3333);
        Registry registry = LocateRegistry.createRegistry(1111);
        registry.bind("BC_Server", server);
        System.out.println("BC_Server is started");
        server.log("QC_Server is started");


    }

}
