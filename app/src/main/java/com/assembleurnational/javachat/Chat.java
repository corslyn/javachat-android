package com.assembleurnational.javachat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

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
    ImageButton envoie;
    String User ;
    String Ami;
    EditText message;
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

        this.action();

        envoie = findViewById(R.id.sendButton);
        message = findViewById(R.id.messageInput);
        Intent intent = getIntent();
        this.User = intent.hasExtra("user") ? intent.getStringExtra("user") : "";
        this.Ami = intent.hasExtra("ami") ? intent.getStringExtra("amis") : "";

        //initialisation socket client
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }


        while (suite){
            //Envoie
            String text = "demande_message,"+ this.User+","+this.Ami+","+compteur;
            byte[] sentBytes = text.getBytes();

            InetAddress serverAddress = null;
            try {
                serverAddress = InetAddress.getByName(getString(R.string.ip_addr));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            DatagramPacket sendPacket = new DatagramPacket(sentBytes, sentBytes.length, serverAddress, R.string.port);
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
            String[] splitted_message = message.split(",");
            tabmessage[compteur] = splitted_message[4];
            compteur += 1;
            if (splitted_message[5].equals("non")){
                suite = false;
            }
        }
    }

    private void action() {
        envoie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    envoie();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void envoie() throws IOException {
        //initialisation socket client
        DatagramSocket clientSocket = new DatagramSocket();

        // Envoie
        String text = "envoi_message,"+User+"," + Ami + message.getText().toString() ;
        byte[] sentBytes = text.getBytes();

        InetAddress serverAddress = InetAddress.getByName(getString(R.string.ip_addr));

        DatagramPacket sendPacket = new DatagramPacket(sentBytes, sentBytes.length, serverAddress, 1337);
        clientSocket.send(sendPacket);

        //reception
        byte[] receiveBytes = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);
        clientSocket.receive(receivePacket);

        String Message = new String (receivePacket.getData(), 0, receivePacket.getLength());
        String[] messplit = Message.split(",");
        if (messplit[4].equals("ok")){
            compteur += 1;
            tabmessage[compteur] = message.getText().toString();
        }
    }
}