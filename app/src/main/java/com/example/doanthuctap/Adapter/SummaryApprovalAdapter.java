package com.example.doanthuctap.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.doanthuctap.R;
import com.example.doanthuctap.Retrofit.Constant;
import com.example.doanthuctap.entity.SummaryApproval;
import com.example.doanthuctap.entity.User;
import com.example.doanthuctap.restful.UserApi;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SummaryApprovalAdapter extends ArrayAdapter<SummaryApproval> {

    private Context context;
    private List<SummaryApproval> summaryApprovals;

    public SummaryApprovalAdapter(Context context, List<SummaryApproval> summaryApprovals) {
        super(context, R.layout.item_summary_approval, summaryApprovals);
        this.context = context;
        this.summaryApprovals = summaryApprovals;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_detail_summary_approval, parent, false);
        }

        SummaryApproval summaryApproval = summaryApprovals.get(position);

        TextView tvUserId = convertView.findViewById(R.id.tvUserId);
        TextView tvApprovalStatus = convertView.findViewById(R.id.tvApprovalStatus);
        TextView tvComments = convertView.findViewById(R.id.tvComments);

        Log.d("SummaryApprovalAdapter", "User ID: " + summaryApproval.getUserId());
        Log.d("SummaryApprovalAdapter", "Approval Status: " + summaryApproval.getApprovalStatus());
        Log.d("SummaryApprovalAdapter", "Review Suggestion: " + summaryApproval.getReviewSuggestion());

        if (tvUserId != null) {
            // Fetch user email asynchronously
            fetchUserEmail(summaryApproval.getUserId(), email -> {
                if (tvUserId != null) {
                    tvUserId.setText("Người dùng: " + email);
                }
            });
        }

        if (tvApprovalStatus != null) {
            tvApprovalStatus.setText("Trạng thái phê duyệt: " + summaryApproval.getApprovalStatus());
        }

        if (tvComments != null) {
            tvComments.setText("Nhận xét : "+ summaryApproval.getReviewSuggestion());
        }

        return convertView;
    }
    private void fetchUserEmail(int userId, Consumer<String> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL + "/api/user/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi userApi = retrofit.create(UserApi.class);
        Call<User> call = userApi.getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String email = response.body().getEmail();
                    callback.accept(email);
                } else {
                    Log.e("SummaryApprovalAdapter", "Failed to fetch user email: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("SummaryApprovalAdapter", "Error fetching user email", t);
            }
        });
    }

}
