package com.crimsonlabs.orlovcs.reaction;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class dice_activity extends MainActivity implements OnItemClickListener, AdapterView.OnItemSelectedListener {

    TextView debug;
    TextView textOutput;
    String currOutput;
    ArrayList<Integer> nums;
    Integer rollOptionSelected = 0;
    Integer dieOptionSelected = 0;
    Boolean api = true;
    Boolean ANU = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.dice_layout);

        currOutput = "";

        Button generateButon = (Button) findViewById(R.id.generate_die);

        nums = new ArrayList<>();
        debug = (TextView) findViewById(R.id.textView5);
        textOutput = findViewById(R.id.dice_output);
        textOutput.setTextIsSelectable(true);

        Spinner roll_spinner = findViewById(R.id.roll_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.rolls,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roll_spinner.setAdapter(adapter);
        roll_spinner.setOnItemSelectedListener(this);


        Spinner die_spinner = findViewById(R.id.die_spinner);
        ArrayAdapter<CharSequence> adapter_2 = ArrayAdapter.createFromResource(this,R.array.die,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        die_spinner.setAdapter(adapter_2);
        die_spinner.setOnItemSelectedListener(this);

        final GenerationHelper generationHelper = new GenerationHelper();


        final Context context = this;
        final RetrieveAPIHelper helper = new RetrieveAPIHelper() {
            @Override
            public void onSuccess(JSONArray data_array) {
                Toast.makeText(getApplicationContext(),
                        "API Call Success\nNumbers Generated",
                        Toast.LENGTH_SHORT).show();
                nums = generationHelper.processData(data_array);
                setString();
            }

            @Override
            public void onFail(String Response) {
                Toast.makeText(getApplicationContext(),
                        "API Call Failed\nManually Generated",
                        Toast.LENGTH_SHORT).show();
                nums = generationHelper.manualGeneration();
                setString();
            }
        };

        generateButon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (api == false){
                    Toast.makeText(getApplicationContext(),
                            "API Disabled\nManually Generated",
                            Toast.LENGTH_SHORT).show();
                    nums = generationHelper.manualGeneration();
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

            if (api){
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

            if (currOutput != null && currOutput != "") {

                final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Source Text", currOutput);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getApplicationContext(),
                        "Copied!",
                        Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.action_share){


            if (currOutput != null && currOutput != "") {

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

        if(adapterView.getId() == R.id.roll_spinner)
        {
            rollOptionSelected = i;
        //    Toast.makeText(getApplicationContext(),   "rolls: " + i,   Toast.LENGTH_SHORT).show();
        }  else  if(adapterView.getId() == R.id.die_spinner)
        {
            dieOptionSelected = i;
        //    Toast.makeText(getApplicationContext(), "die: " + i,    Toast.LENGTH_SHORT).show();
        }







    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    void setString(){

        if (!nums.isEmpty() && nums!=null) {

            Integer sides = 0;
            Integer max = 0;


            switch (dieOptionSelected){
                case 0: //649
                    max = 3;
                    break;
                case 1:
                    max = 4;
                    break;
                case 2:
                    max = 5;
                    break;
                case 3:
                    max = 6;
                    break;
                case 4:
                    max = 7;
                    break;
                case 5:
                    max = 8;
                    break;
                case 6:
                    max = 9;
                    break;
                case 7:
                    max = 10;
                    break;
                case 8:
                    max = 12;
                    break;
                case 9:
                    max = 14;
                    break;
                case 10:
                    max = 20;
                    break;
                case 11:
                    max = 24;
                    break;
                case 12:
                    max = 30;
                    break;
                case 13:
                    max = 48;
                    break;
                case 14:
                    max = 50;
                    break;
                case 15:
                    max = 100;
                    break;
                case 16:
                    max = 1000;
                    break;
                default:
                    max = 3;
                    break;
            }

            String output = "D"+max + "x"+rollOptionSelected;

            List<Integer> digitNums = nums.subList(0,21);


            Integer sum = 0;





            if (dieOptionSelected == -1){ //d1k

                Integer el = digitNums.get(0);
                debug.setText(String.valueOf(el));
                if (el < 99999){
                    el = el * 2;
                }
                output = String.valueOf(el%(99999+1));

            }else{



                for(int i = 0; i < rollOptionSelected+1;i++){

                    Integer v = digitNums.get(i)%(max) + 1;

                    if (i == 0){
                        output = String.valueOf(v);
                    }else{
                        output = output + "+" + v;
                        if (i%3==0){
                            output += "\n";
                        }
                    }
                    sum+=v;
                }

            }

            if (rollOptionSelected == 0){

                textOutput.setText(output);
                currOutput = output;

            }else{
                String tmp = output;
                output = "D"+ max + "x" + (rollOptionSelected+1) + ":\n" ;
              //  tmp = tmp.replaceAll(".....", "$0\n");
                output += tmp;
                output +="\n= "+sum;
                textOutput.setText(output);
                currOutput = output;
            }



        }}





}