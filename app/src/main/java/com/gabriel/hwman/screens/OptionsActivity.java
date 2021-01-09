package com.gabriel.hwman.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.myfirstapp.R;
import com.gabriel.hwman.utils.UIManager;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        EditText about = findViewById(R.id.fdAbout);
        about.setEnabled(false);
        about.setKeyListener(null);
    }

    public void btnReset(View view){
        UIManager.createResetDialog(getSupportFragmentManager(), getApplicationContext(),
                "This action cannot be undone. Are you sure?", "0");
    }

}
