package com.gabriel.hwman.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.myfirstapp.R;
import com.gabriel.hwman.utils.Database;
import com.gabriel.hwman.utils.PropertiesManager;

import java.io.IOException;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_BOOK = "com.myfirstapp.EXTRA_BOOK";
    public static String EXTRA_LESSON = "com.myfirstapp.EXTRA_LESSON";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //props
        PropertiesManager pm = new PropertiesManager(getApplicationContext());
        pm.createPropertiesFile();
        pm.setProps("DATABASE_VERSION", "2");

        //db
        Database dbh = new Database(getApplicationContext(), pm.getDbVersionNumber());
        try {
            dbh.createDataBase(false);
            dbh.listAllTables();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.books_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.books_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void openPDF(View view) {
        Intent intent = new Intent(this, PDFActivity.class);
        Spinner book = findViewById(R.id.books_spinner);
        EditText page = findViewById(R.id.fdLesson);
        String bookName = book.getSelectedItem().toString();
        String pageNum = page.getText().toString();

        if (pageNum == null || pageNum.isEmpty()) {
            Toast.makeText(this, "Invalid page number!", Toast.LENGTH_SHORT).show();
        } else {
            intent.putExtra("EXTRA_BOOK", bookName);
            intent.putExtra("EXTRA_PAGE", pageNum);
            startActivity(intent);
        }
    }

    public void openManager(View view) {
        Intent intent = new Intent(this, ViewHwActivity.class);
        startActivity(intent);
    }

    public void openOptions(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }
}
