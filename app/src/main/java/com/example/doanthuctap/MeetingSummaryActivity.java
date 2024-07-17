package com.example.doanthuctap;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MeetingSummaryActivity extends AppCompatActivity {

    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_summary);

        // Lấy các thông tin từ Intent hoặc từ dữ liệu khác
        String meetingTitle = "Tên cuộc họp mẫu";
        String meetingTime = "10:00 - 11:00, 16/07/2024";
        String meetingSummary = "Nội dung cuộc họp mẫu";
        boolean isAdmin = false;  // Set this based on user role

        // Thiết lập các TextView
        TextView tvMeetingTitle = findViewById(R.id.tvMeetingTitle);
        TextView tvMeetingTime = findViewById(R.id.tvMeetingTime);
        TextView tvMeetingSummary = findViewById(R.id.tvMeetingSummary);

        tvMeetingTitle.setText(meetingTitle);
        tvMeetingTime.setText(meetingTime);
        tvMeetingSummary.setText(meetingSummary);

        // Thêm các kết luận và checkbox
        LinearLayout llConclusions = findViewById(R.id.llConclusions);
        btnSave = findViewById(R.id.btnSave);

        // Dữ liệu mẫu về kết luận và thành viên
        String[] conclusions = {"Kết luận 1", "Kết luận 2"};
        String[] members = {"Thành viên A", "Thành viên B", "Thành viên C"};
        String currentUser = "Thành viên A";  // Thay đổi thành user hiện tại

        for (String conclusion : conclusions) {
            LinearLayout conclusionLayout = new LinearLayout(this);
            conclusionLayout.setOrientation(LinearLayout.VERTICAL);
            conclusionLayout.setPadding(0, 0, 0, 16);

            TextView tvConclusion = new TextView(this);
            tvConclusion.setText(conclusion);
            tvConclusion.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tvConclusion.setTextSize(16);

            conclusionLayout.addView(tvConclusion);

            for (String member : members) {
                LinearLayout memberLayout = new LinearLayout(this);
                memberLayout.setOrientation(LinearLayout.HORIZONTAL);
                memberLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                memberLayout.setPadding(0, 8, 0, 8);

                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(member);
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                EditText etSuggestReview = new EditText(this);
                etSuggestReview.setHint("Đề nghị xem lại (nếu có)");
                etSuggestReview.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));
                etSuggestReview.setVisibility(View.GONE); // Ẩn trường nhập liệu khi không cần thiết

                if (!isAdmin && member.equals(currentUser)) {
                    // Chỉ hiện checkbox và trường nhập liệu cho người dùng hiện tại
                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            etSuggestReview.setVisibility(View.GONE);
                        } else {
                            etSuggestReview.setVisibility(View.VISIBLE);
                        }
                    });
                } else if (isAdmin) {
                    // Admin có thể xem tất cả các thành viên
                    checkBox.setEnabled(false); // Không cho admin tick
                    etSuggestReview.setEnabled(false); // Không cho admin nhập
                } else {
                    checkBox.setVisibility(View.GONE);
                    etSuggestReview.setVisibility(View.GONE);
                }

                memberLayout.addView(checkBox);
                memberLayout.addView(etSuggestReview);

                conclusionLayout.addView(memberLayout);
            }

            llConclusions.addView(conclusionLayout);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClick();
            }
        });
    }

    public void onSaveClick() {
        // Logic để lưu trạng thái của checkbox và các đề nghị xem lại vào cơ sở dữ liệu
        LinearLayout llConclusions = findViewById(R.id.llConclusions);
        boolean allValid = true;

        for (int i = 0; i < llConclusions.getChildCount(); i++) {
            LinearLayout conclusionLayout = (LinearLayout) llConclusions.getChildAt(i);
            for (int j = 1; j < conclusionLayout.getChildCount(); j++) {
                LinearLayout memberLayout = (LinearLayout) conclusionLayout.getChildAt(j);
                CheckBox checkBox = (CheckBox) memberLayout.getChildAt(0);
                EditText etSuggestReview = (EditText) memberLayout.getChildAt(1);

                if (checkBox.getVisibility() == View.VISIBLE && !checkBox.isChecked() && etSuggestReview.getText().toString().trim().isEmpty()) {
                    allValid = false;
                    etSuggestReview.setError("Bạn cần đề nghị xem lại nếu không tick đồng ý");
                }
            }
        }

        if (allValid) {
            // Thực hiện lưu dữ liệu nếu tất cả các checkbox đã được tick hoặc có lý do xem lại
            Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show();
        } else {
            // Thông báo cho người dùng nếu có checkbox chưa được tick hoặc không có lý do xem lại
            Toast.makeText(this, "Có kết luận chưa được tick đồng ý hoặc chưa có lý do xem lại", Toast.LENGTH_SHORT).show();
        }
    }
}
