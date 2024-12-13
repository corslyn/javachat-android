package com.assembleurnational.javachat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    // initialise le port et l'@ IP du serveur
    public static int PORT = 1337;
    public static String IP_ADDR = "10.0.2.2"; // localhost
    static DatagramSocket clientSocket;

    static {
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    Server() throws SocketException {
    }

    public static void send(byte[] sentBytes) throws IOException {
    // envoie le tableau de Bytes en argument au serveur
        DatagramPacket sendPacket = new DatagramPacket(sentBytes, sentBytes.length, InetAddress.getByName(IP_ADDR), PORT);
        clientSocket.send(sendPacket);
    }

    public static String received() throws IOException {
        // récupere un message du serveur et le convertie en string
        byte[] receiveBytes = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);
        clientSocket.receive(receivePacket);
        String message = new String (receivePacket.getData(), 0, receivePacket.getLength());
        return message.trim();
    }
}
