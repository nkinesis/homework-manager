package com.gabriel.hwman.screens;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.myfirstapp.R;
import com.gabriel.hwman.utils.Database;

import java.sql.SQLException;

public class FormHwActivity extends AppCompatActivity {

    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_hw);
        Intent intent = getIntent();
        id = intent.getIntExtra("HWID", 0);
        initScreen();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), "Your changes were not saved", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ViewHwActivity.class);
            startActivity(intent);
        }
        return false;
    }

    public void initScreen() {
        Spinner spinner = findViewById(R.id.books_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.books_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinner2 = findViewById(R.id.eval_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.evals_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        EditText lesson = findViewById(R.id.fdLesson);
        lesson.setText("");

        EditText obs = findViewById(R.id.fdNotes);
        CheckBox review = findViewById(R.id.cbReview);

        if (id != -1) {
            try {
                Cursor dt = Database.select("select * from hw where seq = ?", new String[]{String.valueOf(id)});
                spinner.setSelection(getComboNumber("book", Database.getData(dt, "book")));
                spinner2.setSelection(getComboNumber("eval", Database.getData(dt, "evaluation")));
                lesson.setText(Database.getData(dt, "lesson"));
                obs.setText(Database.getData(dt, "obs"));
                if ("1".equals(Database.getData(dt, "review"))){
                    review.setChecked(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearScreen() {
        EditText lesson = findViewById(R.id.fdLesson);
        EditText obs = findViewById(R.id.fdNotes);
        CheckBox review = findViewById(R.id.cbReview);
        review.setChecked(false);
        lesson.setText("");
        obs.setText("");
    }

    public void saveHw(View view) {
        Spinner spinner = findViewById(R.id.books_spinner);
        Spinner spinner2 = findViewById(R.id.eval_spinner);
        EditText lesson = findViewById(R.id.fdLesson);
        EditText obs = findViewById(R.id.fdNotes);
        CheckBox review = findViewById(R.id.cbReview);

        int lessonNum = Integer.parseInt(lesson.getText().toString());
        String obsTx = obs.getText().toString();
        if (review.isChecked() && !(lessonNum >= 1 && lessonNum <= 10)) {
            Toast.makeText(this, "The review number must be between 1 and 10!", Toast.LENGTH_SHORT).show();
        } else {
            if (id == -1) { //insert record
                if (lesson != null && !lesson.getText().toString().isEmpty()) {
                    ContentValues columns = new ContentValues();
                    columns.put("book", spinner.getSelectedItem().toString());
                    columns.put("lesson", lesson.getText().toString());
                    columns.put("evaluation", spinner2.getSelectedItem().toString());
                    columns.put("review", review.isChecked() ? 1 : 0);
                    columns.put("obs", obsTx.length() > 30 ? obsTx.substring(0, 30) : obsTx);
                    Database.insert("hw", columns);
                    clearScreen();
                    Toast.makeText(this, "Homework saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Inform a lesson!", Toast.LENGTH_SHORT).show();
                }
            } else { //update record
                ContentValues columns = new ContentValues();
                columns.put("book", spinner.getSelectedItem().toString());
                columns.put("lesson", lesson.getText().toString());
                columns.put("evaluation", spinner2.getSelectedItem().toString());
                columns.put("review", review.isChecked() ? 1 : 0);
                columns.put("obs",  obsTx.length() > 30 ? obsTx.substring(0, 30) : obsTx);
                Database.update("hw", columns, "seq = ?", new String[]{String.valueOf(id)});
                Toast.makeText(this, "Homework saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ViewHwActivity.class);
                startActivity(intent);
            }
        }

    }


    public int getComboNumber(String type, String value) {

        if ("eval".equals(type)) {
            switch (value) {
                case "R":
                    return 0;
                case "G":
                    return 1;
                case "VG":
                    return 2;
                case "GR":
                    return 3;
                default:
                    return -1;
            }
        } else if ("book".equals(type)) {
            switch (value) {
                case "HW2":
                    return 0;
                case "HW4":
                    return 1;
                case "HW6":
                    return 2;
                case "HW8":
                    return 3;
                case "HW10":
                    return 4;
                case "HW12":
                    return 5;
                default:
                    return -1;
            }
        } else {
            return -1;
        }
    }


}
