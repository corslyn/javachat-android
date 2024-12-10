package com.assembleurnational.javachat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Button envoie;
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
        envoie = findViewById(R.id.envoie);
        message = findViewById(R.id.message);
        Intent intent = getIntent();
        String user = intent.hasExtra("user") ? intent.getStringExtra("user") : "";
        String ami = intent.hasExtra("ami") ? intent.getStringExtra("amis") : "";
        User = user;
        Ami= ami;
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
    private void action(){
        envoie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Envoie();
            }
        });
    }

    private void Envoie() throws IOException {
        //initialisation socket client
        DatagramSocket clientSocket = new DatagramSocket();



        // Envoie
        String text = "envoie_message,"+User+"," + Ami + message.getText().toString() ;
        byte[] sentBytes = text.getBytes();

        InetAddress serverAddress = InetAddress.getByName("localhost");

        DatagramPacket sendPacket = new DatagramPacket(sentBytes, sentBytes.length, serverAddress, 1337);
        clientSocket.send(sendPacket);

        //reception
        byte[] receiveBytes = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);
        clientSocket.receive(receivePacket);

        String Message = new String (receivePacket.getData(), 0, receivePacket.getLength());
        String[] messplit = Message.split(",");
        if (messplit[4].equals("OK")){
            compteur +=1;
            tabmessage[compteur] = message.getText().toString();
        }


    }
}