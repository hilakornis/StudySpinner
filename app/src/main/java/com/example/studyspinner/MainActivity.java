package com.example.studyspinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {
    String TAG = "ServerResponse";

    ArrayList<String> names ;

    Button btnConfirm;

    Spinner spinner;

    Button btnGetReqeust;
    Button btnPostReqeust;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewOfItems();
        initItems();

    }

    private void initItems() {
        names = new ArrayList<>();
        names.add(getString(R.string.choose_a_technician_name_in_spinner));



        initSpinner();
        initBtnAndAlertWindow();
//        setOnClickListernerServerButtons();

    }

    private void findViewOfItems() {
        btnConfirm = findViewById(R.id.btnConfirm);
        spinner = findViewById(R.id.spinner);
        btnGetReqeust = findViewById(R.id.btnGetReqeust);
        btnPostReqeust = findViewById(R.id.btnPostReqeust);
    }


    private void setOnClickListernerServerButtons() {
        btnGetReqeust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTechnicianNamesFromServer();

            }
        });


        btnPostReqeust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                isTechnicianNameLegal();
                sendTechnicianName("");

            }
        });
    }



    //todo remove:
    /*public void getTechnicianNamesFromServer_old(){
        OkHttpClient client = new OkHttpClient();
        String url = Constants.Server.Users.GetRequest.getAllUserNamesList;
        Log.i("ServerResponse","getTechnicianNamesFromServer start");

        Log.i("ServerResponse","url : "+url);
        Request request = new Request.Builder().url(url).get().build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Log.i("ServerResponse","onResponse successful");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.i("ServerResponse","onResponse successful, starting parsing the input.");
                                Gson gson = new Gson();
                                String response_body = response.body().string();
                                Log.i("ServerResponse", response_body);
//                                String[] namesArray = gson.fromJson(response_body, String[].class);




//                                names = new ArrayList<>(Arrays.asList(namesArray));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });
    }*/

    private void initSpinner() {

        getTechnicianNamesFromServer();//todo return this.
//        parseStringArray("usernameList","");//todo remove this
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names));
    }

    public void getTechnicianNamesFromServer(){
        OkHttpClient client = new OkHttpClient();
        String url = Constants.Server.Users.GetRequest.getAllUserNamesList;
        Log.i(TAG,"getTechnicianNamesFromServer start");

        Log.i(TAG,"url : " + url);
//        Request request = new Request.Builder().url(url).get().build();
        Request request = new Request.Builder()
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG, "Response failed");
                e.printStackTrace();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.i(TAG,"onResponse successful, starting parsing the input.");
                                String response_body = response.body().string();
                                String fieldName = "usernameList";

                                parseStringArray(fieldName, response_body);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });
    }

    private void parseStringArray(String fieldName, String response_body) {

//            response_body = "{\"usernameList\":[\"Assi\",\"Yakir\",\"Hila\",\"Boaz\",\"Idan\"]}";

//        Log.i(TAG, response_body);
        try {
            JSONObject jo = new JSONObject(response_body);
            JSONArray ja = jo.getJSONArray(fieldName);
            for (int i = 0; i < ja.length(); i++) {
                names.add(ja.getString(i));
            }
            spinner.setSelection(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initBtnAndAlertWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTechnicianNameLegal()){
                    CallAlertWindow();
                }

            }
        });
    }

    private void CallAlertWindow() {
        String chosenName = spinner.getSelectedItem().toString();

        String alertMessage = String.format(getString(R.string.technician_picked_to_deal_with_event_message), chosenName);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.technician_picked_to_deal_with_event)
                .setMessage(alertMessage)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        sendTechnicianName("");//todo here put eventID

                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isTechnicianNameLegal(){
        String chosenName = spinner.getSelectedItem().toString();
        if(chosenName.equals(getString(R.string.choose_a_technician_name_in_spinner))){
//            Toast.makeText(this, getString(R.string.user_send_empty_technician_name), Toast.LENGTH_SHORT).show();
            showToastMessage(getString(R.string.user_send_empty_technician_name));

            Log.i(TAG,"empty name");
            return false;
        }
        return true;
    }

    public void showToastMessage(final String msg) {

        /*new Handler(Looper.getMainLooper()).post( () ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
*/

        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        /*runOnUiThread(new Runnable() {            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void sendTechnicianName(String ID_Event){
        String technicianName = spinner.getSelectedItem().toString();



        Log.i("Server","sending technician name "+technicianName);
        OkHttpClient client = new OkHttpClient();

        ID_Event = "6277d5e196756934bc8848c1";//todo remove this


        RequestBody requestBody = new FormBody.Builder()
                .add("ID_Event", ID_Event)
                .add("techName",technicianName)
                .build();

        String url = Constants.Server.Events.PostRequest.updateTechnicianNameToHandleEvent;

        Request request = new Request.Builder().url(url).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG,"Connection with server failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(response.body().string().matches("success")){
                                    Toast.makeText(MainActivity.this, getString(R.string.update_info_in_server), Toast.LENGTH_LONG).show();
                                    finish();//todo
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });
    }


//    private void sendTechnicianName() {
//        String technicianName = spinner.getSelectedItem().toString();
//        Log.i("Server","sending technician name "+technicianName);
//
//
//
//        // todo
//    }


    private void getTechnicianNamesFromServer2() {
        names = new ArrayList<>();
        names.add("Hilak");
        names.add("DanielK");
        names.add("PeterK");
        names.add("SybilK");
        //todo




    }



}