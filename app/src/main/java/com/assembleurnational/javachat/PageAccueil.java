package com.assembleurnational.javachat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class PageAccueil extends AppCompatActivity {
    Button add;
    Button suppr;
    EditText textAdd;
    EditText textSuppr;
    ImageButton Settings;
    ImageButton  Delete;
    RecyclerView listeAmis;
    List<String> amis;
    FriendAdapter adapter;
    int amis_id = 0;
    Button Demanderecu;
    Intent intent = getIntent();
    String user = intent.hasExtra("user") ? intent.getStringExtra("user") : "";
    String[] amipotentiel;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loggedin_page);
        action();
        adapter = new FriendAdapter(amis, friendName -> {
            Intent chatIntent = new Intent(this, Chat.class);
            chatIntent.putExtra("nomAmi", friendName);
            startActivity(chatIntent);
        });
        listeAmis = findViewById(R.id.listeAmis);
        listeAmis.setAdapter(adapter);
        add = findViewById(R.id.add);
        suppr = findViewById(R.id.suppr);
        textAdd = findViewById(R.id.textAdd);
        textSuppr = findViewById(R.id.textSuppr);
        Delete = findViewById(R.id.delete);
        Settings = findViewById(R.id.settings);
        Demanderecu = findViewById(R.id.damis);



        Intent intent = getIntent();
        String user = intent.hasExtra("user") ? intent.getStringExtra("user") : "";

        new Thread(() -> fetchFriends(user)).start();
        new Thread(() -> {
            try {
                AddFriend();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                voirdemande();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> delete()).start();

    }


    private void fetchFriends(String user) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            // envoi d'une requete au server
            String request = "recuperer_amis," + user;
            byte[] sentBytes = request.getBytes();
            Server.send(sentBytes);


            String message = Server.received();
            String[] messplit = message.split(",");

            // remplissage de la liste d'amis
            int j = 4;
            while (!messplit[j].equals("null") && j < 14){
                amis.set(amis_id, messplit[j]);
                amis_id += 1;
                j += 1;
            }

            // mise a jour de l'ui
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void action(){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AddFriend();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        suppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DelFriend();
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting();
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
    }

    private void voirdemande() throws IOException {
        boolean suite = true;
        int compteur = 0;
        while (suite) {
            String text = "recuperer_demande," + user + "," + compteur;
            byte[] sentBytes = text.getBytes();

            Server.send(sentBytes);

            String message = Server.received();

            String[] messplit = message.split(",");
            if (messplit[5].equals("erreur")){
                String T = "pas de demande recu";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                suite = false;
            }
            else{
                 demande choix = new demande();
                 choix.onCreateDialog(messplit[3], messplit[2]);
            }
            if (messplit[5].equals("non")){
                suite = false;
            }
            compteur+=1;
        }
    }

    private void AddFriend() throws IOException {
        // faire commandce piur ajout d'ami
        String ami = textAdd.getText().toString();
        String text = "demande_ami,"+user+","+ami;
        byte[] sentBytes = text.getBytes();
        Server.send(sentBytes);

        String message = Server.received();

        String[] messplit = message.split(",");
        if (messplit[4].equals("ok")){
            String toast = "Demande bien envoyé";
            Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
        }
        else {
            String toast = "Erreur dans la demande";
            Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
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
                    if (response.equals("succès")) {
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

}