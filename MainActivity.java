package com.fretfinder;

import android.content.Context;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TableLayout FretBoard;
    public static ArrayList<Integer> modePattern = new ArrayList<Integer>();
    final Integer scale[] = {0,1,2,3,4,5,6,7,8,9,10,11,0,1,2,3,4,5,6,7,8,9,10,11};

    int rootINDX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FretBoard = (TableLayout) findViewById(R.id.guitarNeck);
        //loadFretBoard(scale, modePattern);

        createFB(scale);
        Button myButton = (Button) findViewById(R.id.choodeModeBtn);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListView(); //find the mode pattern
            }
        });

    }

    public void createFB(Integer[] testScale){
        //FretBoard.removeAllViews();

        CharSequence cs;

        int h, j, b = 0;
        int scaleIndex= 0;

        /*
        the scale array needs to be re-made and accepted based on the mode and the root note
        accepted mode and root note in order to re-arrange scale then recall creatFretBoard with new pattern
        */

        LinkedHashMap<String, ArrayList<Integer>> scaleGroup = new LinkedHashMap<>();

        String[] stringNames = {"E", "A", "D", "G", "B"};
        String id = null;

        for(int c = 0; c <= stringNames.length - 1;c++){

            if(c < 5) {
                id = stringNames[c];
            }

            if(id.equals("G")){
                scaleGroup.put(id, new ArrayList<Integer>());
                b = 0;
                while(b <=3){
                    scaleGroup.get(id).add(testScale[scaleIndex]);
                    scaleIndex++;
                    b++;
                }
            }else if(c != 5){
                scaleGroup.put(id, new ArrayList<Integer>());
                b = 0;
                while(b <= 4){
                    scaleGroup.get(stringNames[c]).add(testScale[scaleIndex]);
                    scaleIndex++;
                    b++;
                }
            }/*else if(c == 5){
                scaleGroup.put("eH", new ArrayList<Integer>());
                scaleGroup.get("eH").addAll(scaleGroup.get("E"));
            }*/
        }

        for (Map.Entry<String, ArrayList<Integer>> e : scaleGroup.entrySet()){
            System.out.println(e.getKey() + " " + e.getValue());
        }

        for(int i = stringNames.length; i >= 0; i--) {
            final TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setId(i);
            row.setLayoutParams(lp);
            j = i;

            for(h = 0; h < stringNames.length; h++){
                String key = stringNames[j % 5];
                for(int note: scaleGroup.get(key)){

                    final TextView cell = new TextView(this);
                    cell.setTextColor(Color.BLACK);

                    cs = Integer.toString(note);
                    cell.setBackgroundColor(Color.rgb(102,51,0));
                    cell.setId(note);

                    cell.setText(cs);
                    cell.setPadding(32,64,32,64);
                    row.addView(cell);

                    cell.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            rootINDX = cell.getId();
                            System.out.println("The rootINDX is: " + rootINDX);
                            highlightRoots(rootINDX);
                            /**/
                            if(!modePattern.isEmpty()){
                                reArrangeFB();
                            }
                        }
                    });
                }
                j++;
            }
            FretBoard.addView(row);
        }

    }

    public void reArrangeFB(){
        //function requires the rootINDX (which fret selected) and the mode pattern
        if(!modePattern.isEmpty()) {
            System.out.println("The mode pattern is: " + modePattern);
            System.out.println("The rootINDX is " + rootINDX);
            int i = 0;
            while(i < rootINDX){
                modePattern.add(0, modePattern.get(11));
                modePattern.remove(modePattern.size() - 1);
                i++;
            }

            System.out.println("The re-arranged mode pattern is: " + modePattern);
            modePattern.addAll(modePattern);
            Integer[] intArray = modePattern.toArray(new Integer[modePattern.size()]);
//            createFB(intArray);
        }

    }

    private void highlightRoots(int fretID){

        TableLayout table = (TableLayout) findViewById(R.id.guitarNeck);

        for(int i = 0; i < table.getChildCount(); i++){
            TableRow row1 = (TableRow) table.getChildAt(i);
            for(int a = 0; a < row1.getChildCount(); a++) {
                int checkedCell = row1.getChildAt(a).getId();
                if(checkedCell == fretID){
                    row1.getChildAt(a).setBackgroundColor(Color.rgb(181,94,8));

                }else{
                    row1.getChildAt(a).setBackgroundColor(Color.rgb(102,51,0));
                }

            }
        }
    }

    public void createListView() {
        Intent intent = new Intent(this, mode_list.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == mode_list.RESULT_OK){
            //returnFromActivity = true;
            final String mode = data.getStringExtra("CHOSEN_MODE");

            //TextView mode_text = (TextView) findViewById(R.id.mode_name);
            //mode_text.setText(mode);
            System.out.println("The chosen mode is: " + mode);

            createNodeArray(mode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void createNodeArray(String modeName){
        int[] modeSpaces = {};
        switch(modeName){
            case "ionian":
                modeSpaces =  new int[]{2,2,1,2,2,2,1};
                reOrderNodes(0, modeSpaces);
                break;
            case "dorian":
                modeSpaces =  new int[]{2,1,2,2,2,1,2};
                reOrderNodes(1, modeSpaces);
                break;
            case "phrygian":
                modeSpaces =  new int[]{1,2,2,2,1,2,2};
                reOrderNodes(2, modeSpaces);
                break;
            case "lydian":
                modeSpaces =  new int[]{2,2,2,1,2,2,1};
                reOrderNodes(3, modeSpaces);
                break;
            case "mixolydian":
                modeSpaces = new int[]{2,2,1,2,2,1,2};
                reOrderNodes(4, modeSpaces);
                break;
            case "aeolion":
                modeSpaces = new int[]{2,1,2,2,1,2,2};
                reOrderNodes(5, modeSpaces);
                break;
            case "locrian":
                modeSpaces = new int[]{1,2,2,1,2,2,2};
                reOrderNodes(6, modeSpaces);
                break;
        }
    }

    public void reOrderNodes(int chosenRoot,int[] modeArrSpaces){

        if(modePattern.size() != 0){
            modePattern.clear();
        }

        int[] nodeNums = {1, 2, 3, 4, 5, 6, 7};
        int scalePos = chosenRoot;
        int i;

        for(i = 0; i < nodeNums.length;i++){ //re-order scale based on chosen mode

            modePattern.add(nodeNums[scalePos]);
            scalePos++;

            if(modeArrSpaces[i] == 2){
                modePattern.add(0);
            }
            if(scalePos == 7)
                scalePos = 0;
        }

        //modePattern.addAll(modePattern);
        System.out.println("The mode pattern is: " + modePattern);

    }

}
