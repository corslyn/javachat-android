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
import com.assembleurnational.javachat.Server;

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

        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    try {
                        log_in();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        });

        inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page_inscription();
            }
        });
    }


    private void page_inscription() {
        Intent intent = new Intent(this, RegisterPage.class); //Dirige vers la page pour s'inscrire
        startActivity(intent);
    }


    private void log_in() throws IOException {
        String log = user.getText().toString();         //On récupere les info pour la connexion
        String mdp = password.getText().toString();

        // Envoie
        String text = "connexion,"+log+"," + mdp ; // On créer le message pour le serveur
        byte[] sentBytes = text.getBytes();

        Server.send(sentBytes);     // et on l'envoie

        String message = Server.received(); // On attend la réponse du serveur
        String[] messplit = message.split(","); // On split le message pour pouvoir l'analyser
        if (messplit[3].equals("ok")){      // selon la reponse on fait les action voulu ici aller sur la page
            System.out.println(messplit[3]);// de l'utilisateur
            Intent intent = new Intent(this, PageAccueil.class);
            intent.putExtra("user", user.getText().toString());
            startActivity(intent);
        }
        else {
            String T = "Erreur login mdp";
            System.out.println(T);

        }
    }

}