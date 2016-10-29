package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;

/**
 * Created by liuyoung on 15/9/24.
 */
public class Motto extends AppCompatActivity {

    private EditText motto;
    private Button save;
    private SharedPreferences sharedPreferences;
    private TextView emoji;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmotto);
        Activitymanager.addActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emoji= (TextView) findViewById(R.id.tv_emoji);

        sharedPreferences = getSharedPreferences("studentinfo", MODE_PRIVATE);
        motto= (EditText) findViewById(R.id.et_editmotto);
        motto.setText(sharedPreferences.getString("pickupWords", ""));


        save= (Button) findViewById(R.id.bt_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("pickupWords",motto.getText().toString());

                editor.apply();

                finish();

            }
        });







    }
}
