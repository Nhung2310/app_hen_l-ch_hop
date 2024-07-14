package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminActivity extends AppCompatActivity {

    private TextView tvTaoCuocHop;
    private TextView tvThanhVien;
    private TextView tvDanhSachCuocHop;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        fab=findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, CreateMeetingActivity.class);
                startActivity(intent);
            }
        });

        tvTaoCuocHop=(TextView)findViewById(R.id.tvTaoCuocHop);
        tvTaoCuocHop.setOnClickListener(v -> {
            Intent intent=new Intent(AdminActivity.this, CreateMeetingActivity.class);
            startActivity(intent);
        });

        tvThanhVien=(TextView)findViewById(R.id.tvThanhVien);
        tvThanhVien.setOnClickListener(v -> {
            Intent intent=new Intent(AdminActivity.this,EmployeeListActivity.class);
            startActivity(intent);
        });

        tvDanhSachCuocHop=(TextView)findViewById(R.id.tvDanhSachCuocHop);
        tvDanhSachCuocHop.setOnClickListener(v -> {
            Intent intent=new Intent(AdminActivity.this,MeetingListActivity.class);
            startActivity(intent);
        });


    }



    }
