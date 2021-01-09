package com.gabriel.hwman.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.myfirstapp.R;
import com.gabriel.hwman.utils.UIManager;

public class EditOptionsActivity extends AppCompatActivity {
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_options);
        Intent intent = getIntent();
        id = intent.getIntExtra("HWID", 0);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(getApplicationContext(), ViewHwActivity.class);
            startActivity(intent);
        }
        return false;
    }

    public void btnRemove(View view) {
        UIManager.createDeleteDialog(getSupportFragmentManager(), getApplicationContext(),
                "This action cannot be undone. Are you sure?", String.valueOf(id));
    }

    public void btnEdit(View view) {
        Intent intent = new Intent(getApplicationContext(), FormHwActivity.class);
        intent.putExtra("HWID", id);
        startActivity(intent);
    }
}
