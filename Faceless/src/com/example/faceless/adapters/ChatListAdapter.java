package com.example.faceless.adapters;

import java.util.List;

import com.example.faceless.R;
import com.example.faceless.objects.ZChatObject;
import com.example.faceless.objects.ZChatObject.ChatItem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ChatListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	Context context;
	List<ZChatObject.ChatItem> chats;
	boolean isMoreAllowed;

	public ChatListAdapter(Context context, List<ChatItem> chats,
			boolean isMoreAllowed) {
		super();
		this.context = context;
		this.chats = chats;
		this.isMoreAllowed = isMoreAllowed;
	}

	@Override
	public int getItemCount() {
		return chats.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holdercom, int pos) {
		pos = holdercom.getAdapterPosition();
		ChatHolder holder = (ChatHolder) holdercom;
		holder.chatText.setText(chats.get(pos).getText());
	}

	public void addItemToListAtBeggining(ZChatObject.ChatItem item) {
		chats.add(0, item);
		notifyItemInserted(0);
	}

	public void addItemsToListAtEnd(List<ChatItem> items) {
		chats.addAll(items);
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.chat_list_item_layout, parent, false);
		ChatHolder holder = new ChatHolder(v);
		return holder;
	}

	class ChatHolder extends RecyclerView.ViewHolder {

		TextView chatText;

		public ChatHolder(View v) {
			super(v);
			chatText = (TextView) v.findViewById(R.id.chattext);
		}
	}
}
