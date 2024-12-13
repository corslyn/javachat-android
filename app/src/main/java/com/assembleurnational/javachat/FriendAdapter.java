package com.assembleurnational.javachat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
// CE FICHIER NO SERT STRICTEMENT À RIEN
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private final List<String> amis;
    private final OnFriendClickListener listener;

    public FriendAdapter(List<String> amis, OnFriendClickListener listener) {
        this.amis = amis;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ami, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        String friend = amis.get(position);
        holder.tvFriendName.setText(friend);
        holder.itemView.setOnClickListener(v -> listener.onFriendClick(friend));
    }

    @Override
    public int getItemCount() {
        return amis.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView tvFriendName;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tvNomAmi);
        }
    }

    public interface OnFriendClickListener {
        void onFriendClick(String friendName);
    }
}
