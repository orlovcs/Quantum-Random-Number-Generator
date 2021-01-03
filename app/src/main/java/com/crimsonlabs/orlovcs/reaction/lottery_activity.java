package com.crimsonlabs.orlovcs.reaction;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class lottery_activity extends AppCompatActivity implements OnItemClickListener, AdapterView.OnItemSelectedListener {

    TextView debug;
    EditText firstLottoNum;
    TextView textOutput;
    String currOutput;
    ArrayList<Integer> nums;
    Integer lotteryOptionSelected = 0;
    Boolean api = true;
    Boolean ANU = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.lottery_layout);


        currOutput = "";
        Button generateButon  = findViewById(R.id.generate);

        nums = new ArrayList<>();
        debug = findViewById(R.id.textView2);
        textOutput = findViewById(R.id.textOutput);
        textOutput.setTextIsSelectable(true);
        Spinner lottery_spinner = findViewById(R.id.lottery_spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.lottos,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lottery_spinner.setAdapter(adapter);
        lottery_spinner.setOnItemSelectedListener(this);

        final Context context = this;
        final RetrieveAPIHelper helper = new RetrieveAPIHelper() {
            @Override
            public void onSuccess(JSONArray data_array) {
                Toast.makeText(getApplicationContext(),
                        "API Call Success\nNumbers Generated",
                        Toast.LENGTH_SHORT).show();
                processData(data_array);
                setString();
            }

            @Override
            public void onFail(String Response) {
                Toast.makeText(getApplicationContext(),
                        "API Call Failed\nManually Generated",
                        Toast.LENGTH_SHORT).show();
                manualGeneration();
                setString();
            }
        };

        generateButon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!api){
                    Toast.makeText(getApplicationContext(),
                            "API Disabled\nManually Generated",
                            Toast.LENGTH_SHORT).show();

                    manualGeneration();
                    setString();
                }else{
                    textOutput.setText("");
                    currOutput = "";
                    new RetrieveAPI(context, helper).execute();

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_api) {

            if (!api){
                api = false;
                Toast.makeText(getApplicationContext(),
                        "API Disabled",
                        Toast.LENGTH_SHORT).show();
                item.setChecked(false);
            }else{
                api = true;
                Toast.makeText(getApplicationContext(),
                        "API Enabled",
                        Toast.LENGTH_SHORT).show();
                item.setChecked(true);
            }

        }else if (id == R.id.action_copy){

            if (currOutput != null && !currOutput.equals("")) {

                final android.content.ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Source Text", currOutput);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getApplicationContext(),
                        "Copied!",
                        Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.action_share){


            if (currOutput != null  && !currOutput.equals("")) {

                Intent newi  = new sharefunc(currOutput).i;
                startActivity(Intent.createChooser(newi, "Share via"));

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        lotteryOptionSelected = i;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    void setString(){

        if (!nums.isEmpty()) {

            Integer max = 0;
            Integer digits = 0;
            Integer bonuses = 0;
            Integer bonusMax = 0;
            boolean bonus = false;

            switch (lotteryOptionSelected){
                case 0: //649
                    max = 49;
                    digits = 6;
                    bonuses = 1;
                    bonusMax = 49;
                    bonus = true;

                    break;
                case 1: //superenaloto
                    max = 90;
                    digits = 6;
                    bonuses = 2;
                    bonusMax = 90;
                    bonus = true;
                    break;
                case 2: //superlallo plus
                    max = 47;
                    digits = 5;
                    bonuses = 1;
                    bonusMax = 27;
                    bonus = true;
                    break;
                case 3://uk lotto
                    max = 59;
                    digits = 6;
                    bonuses = 1;
                    bonusMax = 59;
                    bonus = true;
                    break;
                case 4: //max
                    max = 50;
                    digits = 7;
                    bonuses = 1;
                    bonusMax = 50;
                    bonus = true;
                    break;
                case 5: //euro jackpot
                    max = 50;
                    digits = 5;
                    bonuses = 2;
                    bonusMax = 10;
                    bonus = true;
                    break;
                case 6: //powerball
                    max = 69;
                    digits = 5;
                    bonuses = 1;
                    bonusMax = 26;
                    bonus = true;
                    break;
                case 7: //euro millions
                    max = 50;
                    digits = 5;
                    bonuses = 2;
                    bonusMax = 12;
                    bonus = true;
                    break;
                case 8://mega-sena
                    max = 60;
                    digits = 6;
                    bonuses = 2;
                    bonusMax = 0;
                    bonus = false;
                    break;
                case 9://oz lotto
                    max = 45;
                    digits = 7;
                    bonuses = 0;
                    bonusMax = 0;
                    bonus = false;
                    break;
                case 10://oz powerball
                    max = 35;
                    digits = 7;
                    bonuses = 1;
                    bonusMax = 20;
                    bonus = true;
                    break;
                case 11://french lotto
                    max = 49;
                    digits = 5;
                    bonuses = 1;
                    bonusMax = 10;
                    bonus = true;
                    break;
                case 12://kenno
                    max = 70;
                    digits = 20;
                    bonuses = 0;
                    bonusMax = 0;
                    bonus = false;
                    break;
                case 13://lotto america
                    max = 52;
                    digits = 5;
                    bonuses = 1;
                    bonusMax = 10;
                    bonus = true;
                    break;
                case 14: //mega millions
                    max = 70;
                    digits = 5;
                    bonuses = 1;
                    bonusMax = 25;
                    bonus = true;
                    break;
                case 15://el gordo
                    max = 99999;
                    digits = 1;
                    bonuses = 0;
                    bonusMax = 0;
                    bonus = false;
                    break;

                default:
                    max = 49;
                    digits = 6;
                    bonuses = 1;
                    bonusMax = 49;
                    bonus = true;
                    break;
            }

            String output = "";

            List<Integer> digitNums = nums.subList(0,20);
            List<Integer> bonusNums = nums.subList(21,39);





            if (lotteryOptionSelected == 15){

                Integer el = digitNums.get(0);
                debug.setText(String.valueOf(el));
                if (el < 99999){
                    el = el * 2;
                }

                output = String.valueOf(el%(99999+1));

            }else{

                ArrayList<Integer> noClone = new ArrayList<Integer>(); //removes clones


                for(int i = 0; i < digits;i++){

                    Integer v =  digitNums.get(i)%(max);
                    v++;

                    while (noClone.contains(v)){
                        if (v < max){
                            v++;
                        }else {
                            v--;
                        }

                    }
                    noClone.add(v);

                    if (i == 0){
                        output = String.valueOf(v);
                    }else{
                        output = output + "-" + v;
                    }
                }
                if (bonus){
                for(int i = 0; i < bonuses;i++){

                    Integer v = bonusNums.get(i)%(bonusMax+1);

                    while (noClone.contains(v)){
                        if (v < bonusMax){
                            v++;
                        }else {
                            v--;
                        }

                    }
                    noClone.add(v);

                    output = output + "-" + "(" + v + ")";
                }}
            }

            textOutput.setText(output);
            currOutput = output;

        }}

    void manualGeneration(){
        nums = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 40; i++){
            int n = rand.nextInt(65535); //ANU Bound
            nums.add(n);
        }
    }


    void processData(JSONArray data_array){
        nums = new ArrayList<>();
        try {
            if (data_array != null){
            for(int i=0;i<data_array.length();i++){
                int data = data_array.getInt(i);
                //debug.setText( ter debug.getText() + "\n" + i + " is " + String.valueOf(data) + ". num mod 49 is " + data%49  );
                // firstLottoNum.setText(String.valueOf(data));
                nums.add(data);
            }}


        } catch (JSONException e) {
            e.printStackTrace();
        }



        //  Toast.makeText(this, "Generated", Toast.LENGTH_SHORT).show();

    }




}