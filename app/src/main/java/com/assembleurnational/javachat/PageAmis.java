package com.assembleurnational.javachat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class PageAmis extends AppCompatActivity {
    String user;
    TextView demande1;
    Button accepte1;
    Button refus1;
    int compteur = 0;
    String[] tabdemande = new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_amis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = getIntent().hasExtra("user") ? getIntent().getStringExtra("user") : "";
        demande1 = findViewById(R.id.demande1);
        accepte1 = findViewById(R.id.accepte1);
        refus1 = findViewById(R.id.refus1);
    }

    private void oui() throws IOException {
        String request = "accepter_demande,"+user+"," +demande1.getText().toString()+","+"oui";
        byte[] sentBytes = request.getBytes();
        Server.send(sentBytes);


        String message = Server.received();
        String[] messplit = message.split(",");
        if(messplit[messplit.length -1].equals("ok")){
            System.out.println("demande bien accepté");
            demande1.setText("");
        }
    }

    private void non() throws IOException {
        String request = "accepter_demande,"+user+"," +demande1.getText().toString()+","+"non";
        byte[] sentBytes = request.getBytes();
        Server.send(sentBytes);


        String message = Server.received();
        String[] messplit = message.split(",");
        if(messplit[messplit.length -1].equals("ok")){
            System.out.println("demande bien refusé");
            demande1.setText("");
        }
    }

    public void voir_demande() throws IOException {
        for (int i = 0; i<5; i++){
            String request = "recuperer_demande," + user+","+ i;
            byte[] sentBytes = request.getBytes();
            Server.send(sentBytes);


            String message = Server.received();
            String[] messplit = message.split(",");
            if(messplit[messplit.length -1].equals("erreur")){
                break;
            }else {
                tabdemande[i] = messplit[messplit.length -2];
                if (messplit[messplit.length-1].equals("non")){
                    break;
                }
            }
        }

        demande1.setText(tabdemande[0]);
        demande2.setText(tabdemande[1]);
        demande3.setText(tabdemande[2]);
        demande4.setText(tabdemande[3]);
        demande5.setText(tabdemande[4]);
    }
}