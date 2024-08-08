package com.example.doanthuctap.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanthuctap.DetailSummaryApprovalActivity;
import com.example.doanthuctap.R;
import com.example.doanthuctap.entity.Summary;

import java.util.List;

public class AdminSummaryAdapter extends RecyclerView.Adapter<AdminSummaryAdapter.SummaryViewHolder> {

    private List<Summary> summaries;

    public AdminSummaryAdapter(List<Summary> summaries) {
        this.summaries = summaries;
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

        holder.arrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển sang DetailSummaryApprovalActivity
                Intent intent = new Intent(v.getContext(), DetailSummaryApprovalActivity.class);
                // Truyền summary_id qua Intent
                intent.putExtra("summary_id", summary.getSummaryId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return summaries.size();
    }

    static class SummaryViewHolder extends RecyclerView.ViewHolder {

        TextView tvMeetingTitle, tvConclusion, tvCoordinator;
        ImageView arrowImageView;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvConclusion = itemView.findViewById(R.id.tvConclusion);

            arrowImageView = itemView.findViewById(R.id.arrowImageView);
        }
    }
}
