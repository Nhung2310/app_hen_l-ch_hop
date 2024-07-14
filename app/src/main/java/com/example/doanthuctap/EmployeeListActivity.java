package com.example.doanthuctap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListActivity extends AppCompatActivity {
    private ListView employeeListView;
    private EmployeeAdapter employeeAdapter;
    private List<Employee> employeeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        employeeListView = findViewById(R.id.employee_list);

        // Tạo dữ liệu giả
        employeeList = new ArrayList<>();
        employeeList.add(new Employee("Nguyễn Văn A", "a@example.com"));
        employeeList.add(new Employee("Trần Thị B", "b@example.com"));
        employeeList.add(new Employee("Lê Văn C", "c@example.com"));

        // Tạo adapter và gán cho ListView
        employeeAdapter = new EmployeeAdapter(this, employeeList);
        employeeListView.setAdapter(employeeAdapter);

    }

}