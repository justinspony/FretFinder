package com.fretfinder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class mode_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_list);
        makeList();
    }

    public void run(){
        System.out.println("Test start");
    }

    private void makeList() {
        //new Thread(new mode_list()).start();

        String[] modeArray = {"ionian", "dorian", "phrygian", "lydian", "mixolydian", "aeolion", "locrian"};
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, modeArray);
        ListView listAdapter = (ListView) findViewById(R.id.myListView);
        listAdapter.setAdapter(myAdapter);

        //set onClick listener to return mode name to previous activity
        listAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedText = (TextView) view;
                Intent returnIntent = new Intent();
                returnIntent.putExtra("CHOSEN_MODE", selectedText.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


    }
}
