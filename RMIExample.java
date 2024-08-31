package com.mycompany.rmiexample;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Java RMI example that combines server and client functionality in one program.
 */
public class RMIExample {

    // Define the remote interface
    public interface SharedResource extends Remote {
        String getResource() throws RemoteException;
        void setResource(String value) throws RemoteException;
    }

    // Implement the remote interface
    public static class SharedResourceImpl extends UnicastRemoteObject implements SharedResource {
        private String resource;

        protected SharedResourceImpl() throws RemoteException {
            super();
            this.resource = "Initial Resource";
        }

        @Override
        public String getResource() throws RemoteException {
            return resource;
        }

        @Override
        public void setResource(String value) throws RemoteException {
            this.resource = value;
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            String mode = args[0];

            if (mode.equalsIgnoreCase("server")) {
                // Server code
                try {
                    // Create and export a SharedResourceImpl object
                    SharedResourceImpl obj = new SharedResourceImpl();

                    // Create and bind the RMI registry
                    LocateRegistry.createRegistry(1099);
                    Naming.rebind("rmi://localhost/SharedResource", obj);

                    System.out.println("SharedResource bound in registry");

                    // Keep server running
                    System.out.println("Press Enter to stop the server...");
                    System.in.read();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (mode.equalsIgnoreCase("client")) {
                // Client code
                try {
                    // Lookup the remote object
                    SharedResource resource = (SharedResource) Naming.lookup("rmi://localhost/SharedResource");

                    // Interact with the remote object
                    System.out.println("Initial Resource: " + resource.getResource());
                    resource.setResource("Updated Resource");
                    System.out.println("Updated Resource: " + resource.getResource());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Usage: java RMIExample <server|client>");
            }
        } else {
            System.out.println("Usage: java RMIExample <server|client>");
        }
    }
}