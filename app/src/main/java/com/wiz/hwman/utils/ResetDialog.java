package com.gabriel.hwman.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.gabriel.hwman.screens.MainActivity;

import java.io.IOException;
import java.sql.SQLException;

public class ResetDialog extends DialogFragment {

    Context context;
    String message;
    String seq;
    String response;

    public ResetDialog() {
        this.message = "";
        this.seq = "0";
    }

    public ResetDialog(Context ctx, String msg, String seq) {
        this.message = msg;
        this.seq = seq;
        this.context = ctx;
        this.response = "Database reset!";
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //props
                        PropertiesManager pm = new PropertiesManager(context);
                        pm.createPropertiesFile();
                        pm.setProps("DATABASE_VERSION", "2");
                        //db
                        Database dbh = new Database(context, pm.getDbVersionNumber());
                        try {
                            dbh.createDataBase(true);
                            dbh.listAllTables();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }
}