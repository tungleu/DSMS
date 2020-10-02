package servers;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ONServer {
    public static void main(String[] args) throws IOException, AlreadyBoundException {
        DSMSImplement object = new DSMSImplement(Province.BC);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.bind("BC_Server", object);
        System.out.println("BC_Server is started");
    }
}
