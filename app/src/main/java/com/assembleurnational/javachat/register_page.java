package com.assembleurnational.javachat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class register_page extends AppCompatActivity {
    EditText registerName;
    EditText registerPassword;
    Button registerButton;
    Button retour;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.register_page);
        super.onCreate(savedInstanceState);
        registerName = findViewById(R.id.registerName);
        registerPassword = findViewById(R.id.registerPassword);
        registerButton = findViewById(R.id.registerButton);
        retour = findViewById(R.id.retour);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    register();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retours();
            }
        });
    }



    private void retours(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void register() throws IOException {
        String name = registerName.getText().toString();
        String mdp = registerPassword.getText().toString();

        //initialisation

        DatagramSocket clientSocket = new DatagramSocket();

        //envoie
        String text = "Inscription," + name + "," + mdp;
        byte[] sentBytes = text.getBytes();

        InetAddress serverAddress = InetAddress.getByName("localhost");

        DatagramPacket sendPacket = new DatagramPacket(sentBytes, sentBytes.length, serverAddress, 1337);
        clientSocket.send(sendPacket);

        //reception
        byte[] receivedBytes = new byte[256];
        DatagramPacket receivedPacket = new DatagramPacket(receivedBytes, receivedBytes.length);
        clientSocket.receive(receivedPacket);

        String message = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
        String[] messplit = message.split(",");
        if (messplit[3].equals("OK")) {
            String T = "compte créer";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }



}