package Test2;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Add Contact");
                System.out.println("2. Update Contact");
                System.out.println("3. Delete Contact");
                System.out.println("4. View Contacts");
                System.out.println("5. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (choice == 1) {
                    System.out.println("Enter name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter address:");
                    String address = scanner.nextLine();
                    System.out.println("Enter phone number:");
                    String phoneNumber = scanner.nextLine();
                    ContactServer contactServer = new ContactServer(name, address, phoneNumber);

                    out.writeObject("ADD");
                    out.writeObject(contactServer);
                    String response = (String) in.readObject();
                    System.out.println(response);
                } else if (choice == 2) {
                    System.out.println("Enter the index of the contact to update:");
                    int index = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    System.out.println("Enter name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter address:");
                    String address = scanner.nextLine();
                    System.out.println("Enter phone number:");
                    String phoneNumber = scanner.nextLine();
                    ContactServer contactServer = new ContactServer(name, address, phoneNumber);

                    out.writeObject("UPDATE");
                    out.writeInt(index);
                    out.writeObject(contactServer);
                    String response = (String) in.readObject();
                    System.out.println(response);
                } else if (choice == 3) {
                    System.out.println("Enter the index of the contact to delete:");
                    int index = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    out.writeObject("DELETE");
                    out.writeInt(index);
                    String response = (String) in.readObject();
                    System.out.println(response);
                } else if (choice == 4) {
                    out.writeObject("GET");
                    List<ContactServer> contactServers = (List<ContactServer>) in.readObject();
                    for (int i = 0; i < contactServers.size(); i++) {
                        System.out.println("Contact #" + i);
                        System.out.println("Name: " + contactServers.get(i).getName());
                        System.out.println("Address: " + contactServers.get(i).getAddress());
                        System.out.println("Phone Number: " + contactServers.get(i).getPhoneNumber());
                    }
                } else if (choice == 5) {
                    out.writeObject("EXIT");
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class Contact implements Serializable {
    private String name;
    private String address;
    private String phoneNumber;

    public Contact(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters for name, address, and phoneNumber
}
