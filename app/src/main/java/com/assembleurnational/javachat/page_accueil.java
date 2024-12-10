package com.assembleurnational.javachat;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_accueil);
        Intent intent = getIntent();
        String user = "";
        if (intent.hasExtra("user")){ // vérifie qu'une valeur est associée à la clé “edittext”
             user = intent.getStringExtra("user"); // on récupère la valeur associée à la clé
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        for (int i = 0; i<10; i++){
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
            if (messplit[4].equals("true")){
                amis[amis_id] = messplit[2];
                amis_id += 1;
            }
        }


    }
}