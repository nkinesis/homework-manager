package com.gabriel.hwman.screens;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.myfirstapp.R;
import com.gabriel.hwman.utils.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewHwActivity extends AppCompatActivity {

    Cursor table;
    ArrayList<String> contents;
    HashMap<Long, Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_hw);
        contents = new ArrayList<>();
        ids = new HashMap<>();
        try {
            table = Database.select("SELECT * FROM HW WHERE ?");
            table.moveToFirst();
            if (table.getCount() > 0) {
                String[] retArray = populateArrayList();
                createListItems(retArray);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addHW(View view) {
        Intent intent = new Intent(this, FormHwActivity.class);
        intent.putExtra("HWID", -1);
        startActivity(intent);
    }

    public String[] populateArrayList() {
        String hwType;
        String obs;
        String line;
        long j = 0;

        do {
            obs = ", " + Database.getData(table, "obs");
            if ("1".equals(Database.getData(table, "review"))){
                hwType = "Review";
            } else {
                hwType = "Lesson";
            }
            line = Database.getData(table, "book") +
                    " - " + hwType + " " + Database.getData(table, "lesson") +
                    " (" + evalAcronymToString(Database.getData(table, "evaluation"));
            if (obs.trim().length() > 1){
                line += obs + ")";
            } else {
                line += ")";
            }
            contents.add(line);
            ids.put(j++, Integer.parseInt(Database.getData(table, "seq")));
        } while (table.moveToNext());

        int sz = contents.size();
        String[] retArray = new String[sz];

        for (int i = 0; i < sz; i++) {
            retArray[i] = contents.get(i);
        }
        return retArray;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return true;
    }


    public void createListItems(String[] arr) {
        ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arr);

        ListView listView = findViewById(R.id.hwListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditOptionsActivity.class);
                String row = String.valueOf(parent.getItemAtPosition(position));
                System.out.println(row);
                intent.putExtra("HWID", ids.get(id));
                startActivity(intent);
            }
        });

    }

    public String evalNumToString(String eval) {
        switch (eval) {
            case "0":
                return "Regular";
            case "1":
                return "Good";
            case "2":
                return "Very Good";
            case "3":
                return "Great";
            default:
                return "";
        }
    }

    public String evalAcronymToString(String eval) {
        switch (eval) {
            case "R":
                return "Regular";
            case "G":
                return "Good";
            case "VG":
                return "Very Good";
            case "GR":
                return "Great";
            default:
                return "";
        }
    }
}
