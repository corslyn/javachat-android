package com.assembleurnational.javachat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

public class Chat extends AppCompatActivity {
    boolean suite = true;
    int compteur = 0;
    String[] tabmessage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.chat_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String user = intent.hasExtra("user") ? intent.getStringExtra("user") : "";
        String ami = intent.hasExtra("ami") ? intent.getStringExtra("amis") : "";
        //initialisation socket client
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        while (suite == true){
            //Envoie
            String text = "demande_message,"+ user+","+ami+","+compteur;
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
            tabmessage[compteur] = messplit[4];
            compteur += 1;
            if (messplit[5].equals("non")){
                suite = false;
            }


        }

    }
}