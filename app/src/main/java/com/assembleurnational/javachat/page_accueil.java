package com.assembleurnational.javachat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class page_accueil extends AppCompatActivity {
    String[] amis;
    int amis_id = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loggedin_page);
        Intent intent = getIntent();
        String user = "";
        if (intent.hasExtra("user")){ // vérifie qu'une valeur est associée à la clé “edittext”
             user = intent.getStringExtra("user"); // on récupère la valeur associée à la clé
        }

        //initialisation socket client
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        String log = user;

        // Envoie
        String text = "";
        byte[] sentBytes = text.getBytes();

        InetAddress serverAddress = null;
        try {
            serverAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        DatagramPacket sendPacket = new DatagramPacket(sentBytes, sentBytes.length, serverAddress, 1337);
        try {
            clientSocket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //reception
        byte[] receiveBytes = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);
        try {
            clientSocket.receive(receivePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String message = new String (receivePacket.getData(), 0, receivePacket.getLength());
        String[] messplit = message.split(",");
        int j = 3;
        while (messplit[j].equals("true") || j < 13){
            amis[amis_id] = messplit[j];
            amis_id += 1;
            j += 1;
        }



    }
}