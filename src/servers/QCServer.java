package servers;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class QCServer {
    public static void main(String[] args) throws IOException, AlreadyBoundException {
        DSMSImplement server = new DSMSImplement(Province.QC,1111);
        Registry registry = LocateRegistry.createRegistry(1111);
        registry.bind("QC_Server", server);
        System.out.println("QC_Server is started");
        server.log("QC_Server is started");
    }
}
