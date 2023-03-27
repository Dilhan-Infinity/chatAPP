package com.chatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 5000;

    private final String username;

    private PrintWriter out;
    private BufferedReader in;
    private final Scanner scanner;

    public Client() {
        scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        username = scanner.nextLine();

        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        Thread sender = new Thread(() -> {
            String message;
            while (true) {
                message = scanner.nextLine();
                out.println(username + ": " + message);
            }
        });
        sender.start();

        Thread receiver = new Thread(() -> {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiver.start();
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}