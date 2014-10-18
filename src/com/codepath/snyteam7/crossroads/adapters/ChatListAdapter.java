package com.codepath.snyteam7.crossroads.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.model.Message;

public class ChatListAdapter extends ArrayAdapter<Message> {
	private String mUserId;
	
	public ChatListAdapter(Context context, String userId, List<Message> messages) {
	        super(context, 0, messages);
	        this.mUserId = userId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).
			    inflate(R.layout.chat_item, parent, false);
			final ViewHolder holder = new ViewHolder();
			holder.tvProfileLeftUserName = (TextView)convertView.findViewById(R.id.tvProfileLeftUserName);
			holder.tvMessageCratedAt = (TextView)convertView.findViewById(R.id.tvMessageCratedAt);
			holder.imageRight = (ImageView)convertView.findViewById(R.id.ivProfileRight);
			holder.body = (TextView)convertView.findViewById(R.id.tvBody);
			holder.llOtherUser = (LinearLayout)convertView.findViewById(R.id.llOtherUser);
			convertView.setTag(holder);
		}
		final Message message = (Message)getItem(position);
		final ViewHolder holder = (ViewHolder)convertView.getTag();
		final boolean isMe = message.getUserId().equals(mUserId);
		// Show-hide image based on the logged-in user.
		if (isMe) {
			holder.imageRight.setVisibility(View.VISIBLE);
			holder.llOtherUser.setVisibility(View.GONE);
			holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		} else {
			holder.llOtherUser.setVisibility(View.VISIBLE);
			holder.tvProfileLeftUserName.setText(message.getString("username"));
			Date createDate = message.getCreatedAt();			
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy h:mm a");
		    String donateDateStr = formatter.format(createDate);
		    holder.tvMessageCratedAt.setText(donateDateStr);
			holder.imageRight.setVisibility(View.GONE);
			holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		}

		holder.body.setText(message.getBody());
		return convertView;
	}
	
	final class ViewHolder {
		public TextView tvProfileLeftUserName;
		public TextView tvMessageCratedAt;
		public ImageView imageRight;
		public TextView body;
		public LinearLayout llOtherUser;
	}
}
