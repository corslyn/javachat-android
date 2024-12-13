package com.assembleurnational.javachat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.List;

public class PageAccueil extends AppCompatActivity {
    Button ami1;
    Button ami2;
    Button ami3;
    Button ami4;
    Button ami5;
    Button voiramis;
    Button add;
    Button suppr;                       // Initialisation de quelques™ variables
    EditText textAdd;
    EditText textSuppr;
    ImageButton Settings;
    ImageButton  Delete;
    RecyclerView listeAmis;
    List<String> amis;
    FriendAdapter adapter;
    int amis_id = 0;
    Button Demanderecu;
    String user;
    String[] amisliste =  new String[5]; //5 amis maximum
    TextView currentUser;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loggedin_page);
        user = getIntent().hasExtra("user") ? getIntent().getStringExtra("user") : "";





        add = findViewById(R.id.add);
        suppr = findViewById(R.id.suppr);
        textAdd = findViewById(R.id.textAdd);
        textSuppr = findViewById(R.id.textSuppr);
        Delete = findViewById(R.id.delete);
        Settings = findViewById(R.id.settings);
        Demanderecu = findViewById(R.id.damis);
        currentUser = findViewById(R.id.current);
        ami1 = findViewById(R.id.ami1);
        ami2 = findViewById(R.id.ami2);
        ami3 = findViewById(R.id.ami3);
        ami4 = findViewById(R.id.ami4);
        ami5 = findViewById(R.id.ami5);
        voiramis = findViewById(R.id.voiramis);

        currentUser.setText(user);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> {
                    try {
                        AddFriend();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

            }
        });

        suppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> {
                        DelFriend();
                }).start();
            }
        });
       Delete.setOnClickListener(new View.OnClickListener() {
           @Override
             public void onClick(View view) {
               new Thread(() -> {
                   delete();
               }).start();
           }
         });

        Settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new Thread(() -> {
                    setting();
                }).start();
            }
        });
        Demanderecu.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              try {
                  voirdemande();
              } catch (IOException e) {
                  throw new RuntimeException(e);
              }
          }
          });

        voiramis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> {
                    try {
                        voiramis();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        });

        ami1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotochat(amisliste[0]);
            }
        });
        ami2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotochat(amisliste[1]);
            }
        });
        ami3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotochat(amisliste[2]);
            }
        });
        ami4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotochat(amisliste[3]);
            }
        });
        ami5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    gotochat(amisliste[4]);


            }
        });




        Intent intent = getIntent();
        String user = intent.hasExtra("user") ? intent.getStringExtra("user") : "";




    }


    private void voiramis() throws IOException { // Récuper les amis qu'on a déja sur le serveur
                                                 // Et les stock dans un tableau
             //envoi d'une requete au server
            String request = "recuperer_amis," + user;
            byte[] sentBytes = request.getBytes();
            Server.send(sentBytes);


            String message = Server.received();
            String[] messplit = message.split(",");

             //remplissage de la liste d'amis
            int j = 3;
            while ( j < 8){
                amisliste[j-3] = (messplit[j]);
                j += 1;

            }

            runOnUiThread(
                    () -> { // Puis en remplis les bouton avec nos amis
                        ami1.setText(amisliste[0]);
                        ami2.setText(amisliste[1]);
                        ami3.setText(amisliste[2]);
                        ami4.setText(amisliste[3]);
                        ami5.setText(amisliste[4]);
                    }
            );





    }






    private void voirdemande() throws IOException {
        Intent intent = new Intent(this, PageAmis.class); // Dirige vers la page pour les demande d'amis
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void AddFriend() throws IOException { //Permet d'envoyé une demande d'amis
        // faire commandce piur ajout d'ami
        String ami = textAdd.getText().toString();
        String moi = user;
        String text = "demande_ami,"+moi+","+ami+"\n";
        System.out.println(text);
        byte[] sentBytes = text.getBytes();
        Server.send(sentBytes);

        String message = Server.received();
        System.out.println(message);

        String[] messplit = message.split(",");
        if (messplit[2].equals("erreur")){
            String T = "Erreur dans la demande";
            System.out.println(T);
        }
        else {
            String T = "demande bien envoyé";
            System.out.println(T);
        }


    }

    private void DelFriend(){
        // faire effacement
    }

    private void setting(){
        // faire un truc mais je sais pas encore quoi
    }

    private void delete(){
        // faire le suppression de compte
        new Thread(() -> {
            try {
                String request = "supprimer_utilisateur," + user; // Commande pour supprimer l'utilisateur
                byte[] sentBytes = request.getBytes();

                Server.send(sentBytes); // Envoyer la requête au serveur

                String response = Server.received(); // Lire la réponse du serveur

                runOnUiThread(() -> {
                    if (response.equals("")) {
                        Toast.makeText(this, "Compte supprimé avec succès.", Toast.LENGTH_LONG).show();
                        // Retour à l'écran de connexion
                        Intent loginIntent = new Intent(this, MainActivity.class);
                        startActivity(loginIntent);
                        finish(); // Ferme l'activité actuelle
                    } else {
                        Toast.makeText(this, "Erreur lors de la suppression du compte.", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Erreur réseau.", Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void gotochat(String ami){
        // Permet d'aller sur la page de chat avec l'amis selectionné
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("user", user);
        intent.putExtra("ami", ami);
        startActivity(intent);
    }
}