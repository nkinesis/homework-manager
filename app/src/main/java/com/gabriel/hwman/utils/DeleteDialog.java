package com.gabriel.hwman.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.gabriel.hwman.screens.ViewHwActivity;

public class DeleteDialog extends DialogFragment {

    Context context;
    String message;
    String seq;
    String response;

    public DeleteDialog() {
        this.message = "";
        this.seq = "0";
    }

    public DeleteDialog(Context ctx, String msg, String seq) {
        this.message = msg;
        this.seq = seq;
        this.context = ctx;
        this.response = "Deleted!";
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Database.delete("HW", "seq = ?", new String[]{String.valueOf(seq)});
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ViewHwActivity.class);
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