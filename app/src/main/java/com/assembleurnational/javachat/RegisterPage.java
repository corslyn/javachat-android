package com.assembleurnational.javachat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RegisterPage extends AppCompatActivity {
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
                new Thread(() -> {
                    try {
                        register();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
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
        String text = "inscription," + name + "," + mdp;
        byte[] sentBytes = text.getBytes();

        Server.send(sentBytes);

        String message = Server.received();

        String[] messplit = message.split(",");
        if (messplit[3].equals("ok")) {
            String T = "compte créé";
            System.out.println(T);
            //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }



}