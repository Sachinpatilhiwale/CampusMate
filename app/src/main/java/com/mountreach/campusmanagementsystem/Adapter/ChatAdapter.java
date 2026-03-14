package com.mountreach.campusmanagementsystem.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Model.ChatMessage;
import com.mountreach.campusmanagementsystem.R;

import java.util.List;



public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> messageList;
    private static final int USER = 1;
    private static final int BOT = 2;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).isUser()) {
            return USER;
        } else {
            return BOT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == USER) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_message, parent, false);

            return new UserViewHolder(view);

        } else {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_bot_message, parent, false);

            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ChatMessage message = messageList.get(position);

        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).userMessage.setText(message.getMessage());
        } else {
            ((BotViewHolder) holder).botMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userMessage;

        public UserViewHolder(View itemView) {
            super(itemView);
            userMessage = itemView.findViewById(R.id.userMessage);
        }
    }

    class BotViewHolder extends RecyclerView.ViewHolder {

        TextView botMessage;

        public BotViewHolder(View itemView) {
            super(itemView);
            botMessage = itemView.findViewById(R.id.botMessage);
        }
    }
}