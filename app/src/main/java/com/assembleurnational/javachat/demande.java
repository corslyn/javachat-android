package com.assembleurnational.javachat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.io.IOException;

public class demande extends DialogFragment {

    public Dialog onCreateDialog( String demandeur, String receveur ) {
        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Demande d'amis de"+demandeur)
                .setPositiveButton("Accepter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // accrpter demande
                        String text = "accepter_demande,"+receveur+","+demandeur+","+"oui";
                        byte[] sentBytes = text.getBytes();

                        try {
                            Server.send(sentBytes);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .setNegativeButton("Refuser", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancels the dialog.
                        String text = "accepter_demande,"+receveur+","+demandeur+","+"non";
                        byte[] sentBytes = text.getBytes();
                        try {
                            Server.send(sentBytes);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        // Create the AlertDialog object and return it.
        return builder.create();
    }
}

