package com.example.doanthuctap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.doanthuctap.R;
import com.example.doanthuctap.entity.User;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSelectionAdapter extends BaseAdapter {
    private final Context context;
    private List<User> employeeList;  // Khởi tạo là null
    private final List<User> selectedUsers;

    public EmployeeSelectionAdapter(Context context, List<User> employeeList, List<User> selectedUsers) {
        this.context = context;
        this.employeeList = employeeList != null ? employeeList : new ArrayList<>();  // Đảm bảo không phải là null
        this.selectedUsers = selectedUsers != null ? selectedUsers : new ArrayList<>();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_employee_selection, parent, false);
        }

        TextView fullNameTextView = convertView.findViewById(R.id.employeeName);
        CheckBox checkBox = convertView.findViewById(R.id.employeeCheckBox);

        User user = employeeList.get(position);

        fullNameTextView.setText(user.getFullName());
        checkBox.setChecked(selectedUsers.contains(user));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedUsers.contains(user)) {
                    selectedUsers.add(user);
                }
            } else {
                selectedUsers.remove(user);
            }
        });

        return convertView;
    }
}
