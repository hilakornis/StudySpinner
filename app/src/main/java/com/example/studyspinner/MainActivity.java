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
import com.google.gson.Gson;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {

    Button btnConfirm;
    Spinner spinner;
//    ArrayList<String> names ;
    ArrayList<String> names = new ArrayList<>(); // todo remove



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewOfItems();
        initSpinner();
        initBtnAndAlertWindow();

    }

    private void findViewOfItems() {
        btnConfirm = findViewById(R.id.btnConfirm);
        spinner = findViewById(R.id.spinner);

    }


    public void getTechnicianNamesFromServer(){
        OkHttpClient client = new OkHttpClient();
        String url = Constants.Server.Users.GetRequest.getAllUserNamesList;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Gson gson = new Gson();

                                String[] namesArray = gson.fromJson(response.body().string(),String[].class);
//                                names = new ArrayList<>(Arrays.asList(namesArray));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });
    }

    private void initSpinner() {

        getTechnicianNamesFromServer();
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
                        sendTechnicianName();
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

    private void sendTechnicianName() {
                Toast.makeText(MainActivity.this, "sendTechnicianName was called" , Toast.LENGTH_SHORT).show();
    }


    private void getTechnicianNamesFromServer2() {
        names = new ArrayList<>();
        names.add("Hilak");
        names.add("DanielK");
        names.add("PeterK");
        names.add("SybilK");
        //todo




    }



}