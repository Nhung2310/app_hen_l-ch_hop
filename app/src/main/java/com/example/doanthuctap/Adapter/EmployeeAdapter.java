package com.example.doanthuctap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.doanthuctap.R;
import com.example.doanthuctap.entity.User;

import java.util.List;

public class EmployeeAdapter extends BaseAdapter {
    private Context context;
    private List<User> employeeList;

    public EmployeeAdapter(Context context, List<User> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    @Override
    public int getCount() {
        return employeeList.size();
    }

    @Override
    public Object getItem(int position) {
        return employeeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.employee_item, parent, false);
        }


        TextView fullNameTextView = convertView.findViewById(R.id.fullNameTextView);
        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        TextView emailTextView = convertView.findViewById(R.id.emailTextView);
        TextView roleTextView = convertView.findViewById(R.id.roleTextView);

        User user = employeeList.get(position);


        fullNameTextView.setText("Full Name: " + user.getFullName());
        usernameTextView.setText("Username: " + user.getUsername());
        emailTextView.setText("Email: " + user.getEmail());
        roleTextView.setText("Role: " + user.getRole());

        return convertView;
    }
}
