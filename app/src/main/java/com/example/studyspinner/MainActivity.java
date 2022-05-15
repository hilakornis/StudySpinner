package com.example.studyspinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

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

        names = new ArrayList<>();

        findViewOfItems();
        initSpinner();
        initBtnAndAlertWindow();
        setOnClickListernerServerButtons();


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

                sendTechnicianName("");

            }
        });
    }

    private void findViewOfItems() {
        btnConfirm = findViewById(R.id.btnConfirm);
        spinner = findViewById(R.id.spinner);
        btnGetReqeust = findViewById(R.id.btnGetReqeust);
        btnPostReqeust = findViewById(R.id.btnPostReqeust);
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
                                Gson gson = new Gson();
                                String response_body = response.body().string();
                                //String response_body = "usernameList:[\"Assi\",\"Yakir\",\"Hila\",\"Boaz\"]";

                                String fieldName = "usernameList";
                                Log.i(TAG, response_body);

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
        //todo: remove this:
            response_body = "{\"usernameList\":[\"Assi\",\"Yakir\",\"Hila\",\"Boaz\",\"Idan\"]}";

//        Log.i(TAG, response_body);
        try {
            JSONObject jo = new JSONObject(response_body);
            JSONArray ja = jo.getJSONArray(fieldName);
            for (int i = 0; i < ja.length(); i++) {
                names.add(ja.getString(i));
            }
//            Log.i(TAG,"Output String array will be : ");
//            for (String s : names) {
//                Log.i(TAG, s);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

//        getTechnicianNamesFromServer();//todo return this.
        parseStringArray("usernameList","");//todo remove this
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names));
    }

    private void initBtnAndAlertWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallAlertWindow();
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

                        sendTechnicianName("");

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


    public void sendTechnicianName(String ID_Event){
        String technicianName = spinner.getSelectedItem().toString();
        Log.i("Server","sending technician name "+technicianName);
        OkHttpClient client = new OkHttpClient();

        ID_Event = "6277d5e196756934bc8848c1";//todo remove this


        RequestBody requestBody = new FormBody.Builder()
                .add("_id", ID_Event)
                .add("techName",technicianName)
                .build();

        String url = Constants.Server.Events.PostRequest.updateTechnicianNameToHandleEvent;

        Request request = new Request.Builder().url(url).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG,"Connection with server failed");
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
                                    finish();
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