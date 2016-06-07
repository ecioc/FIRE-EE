package com.pthzkj.fire_ee.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pthzkj.fire_ee.R;
import com.pthzkj.fire_ee.customView.CircleButton;

public class MainActivity extends AppCompatActivity {

    private CircleButton main_btn1;

    private CircleButton main_btn2;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        buttonClicks();
    }


    public void buttonClicks(){
        main_btn1 = (CircleButton) findViewById(R.id.main_btn1);
        main_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "指挥救援调度中心", Toast.LENGTH_SHORT).show();
            }
        });
        main_btn2 = (CircleButton) findViewById(R.id.main_btn2);
        main_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InformationActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "消防信息资源库", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
