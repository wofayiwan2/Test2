package Test2;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<ContactServer> contactServers;

    public Server() {
        contactServers = new ArrayList<>();
    }

    public void addContact(ContactServer contactServer) {
        contactServers.add(contactServer);
    }

    public void updateContact(int index, ContactServer contactServer) {
        if (index >= 0 && index < contactServers.size()) {
            contactServers.set(index, contactServer);
        }
    }

    public void deleteContact(int index) {
        if (index >= 0 && index < contactServers.size()) {
            contactServers.remove(index);
        }
    }

    public List<ContactServer> getContacts() {
        return contactServers;
    }

    public static void main(String[] args) {
        Server server = new Server();
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started. Listening on port 12345...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler handler = new ClientHandler(clientSocket, server);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
            while (true) {
                String request = (String) in.readObject();
                if (request.equals("ADD")) {
                    ContactServer contactServer = (ContactServer) in.readObject();
                    server.addContact(contactServer);
                    out.writeObject("Contact added successfully.");
                } else if (request.equals("UPDATE")) {
                    int index = in.readInt();
                    ContactServer contactServer = (ContactServer) in.readObject();
                    server.updateContact(index, contactServer);
                    out.writeObject("Contact updated successfully.");
                } else if (request.equals("DELETE")) {
                    int index = in.readInt();
                    server.deleteContact(index);
                    out.writeObject("Contact deleted successfully.");
                } else if (request.equals("GET")) {
                    List<ContactServer> contactServers = server.getContacts();
                    out.writeObject(contactServers);
                } else if (request.equals("EXIT")) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class ContactServer implements Serializable {
    private String name;
    private String address;
    private String phoneNumber;

    public ContactServer(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    // Getters and setters for name, address, and phoneNumber
}

