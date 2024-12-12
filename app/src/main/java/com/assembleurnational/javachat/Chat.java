package com.assembleurnational.javachat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    String[] tabmessage = new String[5];;
    ImageButton envoie;
    String User ;
    String Ami;
    EditText message;
    TextView correspondant;
    TextView mess1;
    TextView mess2;
    TextView mess3;
    TextView mess4;
    TextView mess5;
    Button recup;

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

        correspondant = findViewById(R.id.correspondant);
        mess1 = findViewById(R.id.mess1);
        mess2 = findViewById(R.id.mess2);
        mess3 = findViewById(R.id.mess3);
        mess4 = findViewById(R.id.mess4);
        mess5 = findViewById(R.id.mess5);
        recup = findViewById(R.id.recup);
        envoie = findViewById(R.id.sendButton);
        message = findViewById(R.id.messageInput);
        Intent intent = getIntent();
        //this.User = intent.hasExtra("user") ? intent.getStringExtra("user") : "";
        User = getIntent().hasExtra("user") ? getIntent().getStringExtra("user") : "";
        Ami = getIntent().hasExtra("ami") ? getIntent().getStringExtra("ami") : "";
        //this.Ami = intent.hasExtra("ami") ? intent.getStringExtra("amis") : "";
        //Ami = "castex";
       // if (Ami == null){
          //  Ami = "null";
       // }
       // if (Ami.equals("null")){
          //  Intent goback = new Intent(this, PageAccueil.class);
           // intent.putExtra("user", User);
            //intent.putExtra("ami", Ami);
           // startActivity(goback);
       // }
        correspondant.setText(Ami);




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
        recup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    recup();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }



    private void envoie() throws IOException {

        // Envoie
        String text = "envoi_message,"+User+"," + Ami +","+ message.getText().toString() ;
        byte[] sentBytes = text.getBytes();

        Server.send(sentBytes);
        String message = Server.received();

        String[] messplit = message.split(",");
        if (messplit[4].equals("ok")){
            tabmessage[compteur] = message;
            compteur += 1;

        }
    }

    private void recup() throws IOException {
        boolean suite = true;
        compteur = 0;
        while (suite || compteur < 5){
            String text = "demande_message,"+User+","+Ami+","+compteur;
            byte[] sentBytes = text.getBytes();

            Server.send(sentBytes);
            String message = Server.received();

            String[] messplit = message.split(",");
            if(messplit[6].equals("erreur")){
                suite = false;
            }
            else {
                tabmessage[compteur] = messplit[4]+" : "+messplit[5];
                compteur +=1;
                if (messplit[6].equals("non")){
                    suite = false;
                }
            }
        }

        mess1.setText(tabmessage[0]);
        mess2.setText(tabmessage[1]);
        mess3.setText(tabmessage[2]);
        mess4.setText(tabmessage[3]);
        mess5.setText(tabmessage[4]);

    }
}