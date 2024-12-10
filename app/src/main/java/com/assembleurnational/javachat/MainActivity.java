package com.assembleurnational.javachat;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
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
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    EditText user;
    EditText password;
    Button log_button;
    Button inscrire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        log_button = findViewById(R.id.log_button);
        inscrire = findViewById(R.id.inscrire);

    }

    private void action(){
        inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_inscription();
            }
        });

        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    log_in();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void page_inscription() {
        Intent intent = new Intent(this, register_page.class);
        startActivity(intent);
    }

    private void log_in() throws IOException {

        //initialisation socket client
        DatagramSocket clientSocket = new DatagramSocket();

        String log = user.getText().toString();
        String mdp = password.getText().toString();

        // Envoie
        String text = "connexion,"+log+"," + mdp ;
        byte[] sentBytes = text.getBytes();

        InetAddress serverAddress = InetAddress.getByName("localhost");

        DatagramPacket sendPacket = new DatagramPacket(sentBytes, sentBytes.length, serverAddress, 1337);
        clientSocket.send(sendPacket);

        //reception
        byte[] receiveBytes = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);
        clientSocket.receive(receivePacket);

        String message = new String (receivePacket.getData(), 0, receivePacket.getLength());
        String[] messplit = message.split(",");
        if (messplit[3].equals("OK")){
            Intent intent = new Intent(this, page_accueil.class);
            intent.putExtra("user", user.getText().toString());
            startActivity(intent);
        }
        else {
            String toast = "Erreur login mdp";
            Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
        }
    }

}