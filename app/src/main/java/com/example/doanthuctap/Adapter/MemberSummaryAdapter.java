package com.example.doanthuctap.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanthuctap.R;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.entity.Summary;
import com.example.doanthuctap.entity.SummaryApproval;
import com.example.doanthuctap.restful.SummaryApprovalApi;
import com.example.doanthuctap.util.GsonProvider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MemberSummaryAdapter extends RecyclerView.Adapter<MemberSummaryAdapter .SummaryViewHolder> {
    private List<Summary> summaries;
    private SummaryApprovalApi summaryApprovalApi; // API interface

    public MemberSummaryAdapter(List<Summary> summaries) {
        this.summaries = summaries;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/summaryapproval/summaryapproval/")
                .addConverterFactory(GsonConverterFactory.create(GsonProvider.getGson()))
                .build();

        summaryApprovalApi = retrofit.create(SummaryApprovalApi.class);
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_summary, parent, false);
        return new SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        Summary summary = summaries.get(position);
        holder.tvConclusion.setText(summary.getConclusion());

        holder.arrowImageView.setOnClickListener(v -> showDialog(holder.itemView.getContext(), summary));
    }

    @Override
    public int getItemCount() {
        return summaries.size();
    }

    private void showDialog(Context context, Summary summary) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_summary_action, null);
        builder.setView(dialogView);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
        RadioButton radioApprove = dialogView.findViewById(R.id.radioApprove);
        RadioButton radioRequestReview = dialogView.findViewById(R.id.radioRequestReview);
        EditText etSuggestion = dialogView.findViewById(R.id.etSuggestion);
      //  etSuggestion.setVisibility(View.VISIBLE);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioRequestReview) {
                etSuggestion.setVisibility(View.VISIBLE);
            } else {
                etSuggestion.setVisibility(View.GONE);
            }
        });

        builder.setTitle("Nêu ý kiến");
        builder.setPositiveButton("Gửi", (dialog, which) -> {
                    String suggestion = etSuggestion.getText().toString();
                    boolean isApproved = radioApprove.isChecked();
                    boolean isReviewRequested = radioRequestReview.isChecked();
                    String approvalStatus = isApproved ? "approved" : (isReviewRequested ? "request_review" : "none");

                    SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                    int userId = sharedPreferences.getInt("user_id", -1);


// Ghi log dữ liệu trước khi gửi
                    Log.d("SummaryApproval", "Summary ID: " + summary.getSummaryId());
                    Log.d("SummaryApproval", "User ID: " + userId);
                    Log.d("SummaryApproval", "Approval Status: " + approvalStatus);
                    Log.d("SummaryApproval", "Review Suggestion: " + suggestion);

                    SummaryApproval summaryApproval = new SummaryApproval(
                            summary.getSummaryId(),
                            userId,
                            approvalStatus,
                            isReviewRequested ? suggestion : null
                    );

                    summaryApprovalApi.createSummaryApproval(summaryApproval).enqueue(new Callback<SummaryApproval>() {
                        @Override
                        public void onResponse(@NonNull Call<SummaryApproval> call, @NonNull Response<SummaryApproval> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Đã gửi thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("API Error", "Code: " + response.code() + ", Message: " + response.message());
                                Toast.makeText(context, "Gửi thất bại: " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<SummaryApproval> call, @NonNull Throwable t) {
                            Log.e("API Error", "Failure: " + t.getMessage());
                            Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

        builder.setNegativeButton("Thoát", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    static class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView tvConclusion;
        ImageView arrowImageView;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvConclusion = itemView.findViewById(R.id.tvConclusion);
            arrowImageView = itemView.findViewById(R.id.arrowImageView);
        }
    }
}

