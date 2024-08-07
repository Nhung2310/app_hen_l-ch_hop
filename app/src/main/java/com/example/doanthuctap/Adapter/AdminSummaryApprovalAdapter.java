package com.example.doanthuctap.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanthuctap.R;
import com.example.doanthuctap.entity.SummaryApproval;

import java.util.List;

public class AdminSummaryApprovalAdapter {
    private List<SummaryApproval> approvalList;

    public AdminSummaryApprovalAdapter(List<SummaryApproval> approvalList) {
        this.approvalList = approvalList;
    }

    @NonNull

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_approval, parent, false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SummaryApproval approval = approvalList.get(position);
        holder.tvSummaryId.setText(String.valueOf(approval.getSummaryId()));
        holder.tvUserId.setText(String.valueOf(approval.getUserId()));
        holder.tvApprovalStatus.setText(approval.getApprovalStatus());
        holder.tvReviewSuggestion.setText(approval.getReviewSuggestion());
    }


    public int getItemCount() {
        return approvalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSummaryId, tvUserId, tvApprovalStatus, tvReviewSuggestion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSummaryId = itemView.findViewById(R.id.tvSummaryId);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvApprovalStatus = itemView.findViewById(R.id.tvApprovalStatus);
            tvReviewSuggestion = itemView.findViewById(R.id.tvReviewSuggestion);
        }
    }
}
