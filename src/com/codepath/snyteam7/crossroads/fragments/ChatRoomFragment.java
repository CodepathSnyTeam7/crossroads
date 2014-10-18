package com.codepath.snyteam7.crossroads.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.adapters.ChatListAdapter;
import com.codepath.snyteam7.crossroads.model.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


public class ChatRoomFragment extends DialogFragment {
	private ListView lvChat;
	private EditText etMessage;
	private Button btSend;
	private Button btClose;
	private ArrayList<Message> mMessages;
	private ChatListAdapter mAdapter;

	private String loggedInUserId;
	private String loggedInUsername;
	private String itemId;
	
	private Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loggedInUserId = getArguments().getString("loggedInUserId");
		loggedInUsername = getArguments().getString("loggedInUsername");
		itemId = getArguments().getString("itemId");
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat_room, container, false);
    	etMessage = (EditText) view.findViewById(R.id.etMessage);
    	btSend = (Button) view.findViewById(R.id.btSend);
    	btClose = (Button) view.findViewById(R.id.btClose);
    	lvChat = (ListView) view.findViewById(R.id.lvChat);	
    	btClose.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});   	
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		setupMessagePosting();
		receiveMessage();
	}
	
	@Override
	public void onResume() {
		super.onStart();
		handler.postDelayed(runnable, 5000);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		handler.removeCallbacksAndMessages(null);
	}
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			refreshMessages();
			handler.postDelayed(this, 5000);			
		}
	};
	
	
	public static ChatRoomFragment getInstance(String loggedInUserId, String itemId, String loggedInUsername) {
		ChatRoomFragment frag = new ChatRoomFragment();
		Bundle bundle = new Bundle();
		bundle.putString("loggedInUserId", loggedInUserId);
		bundle.putString("itemId", itemId);
		bundle.putString("loggedInUsername", loggedInUsername);
		frag.setArguments(bundle);
		return frag;
	}
	
	private void setupMessagePosting() {
    	mMessages = new ArrayList<Message>();
    	mAdapter = new ChatListAdapter(getActivity(), loggedInUserId, mMessages);
    	lvChat.setAdapter(mAdapter);
    	btSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String body = etMessage.getText().toString();
				// Use Message model to create new messages now      
				Message message = new Message();
				message.setUserId(loggedInUserId);
				message.put("username", loggedInUsername);
				message.setItemId(itemId);
				message.setBody(body);
				message.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if(e == null) {
							receiveMessage();
						}else{
							e.printStackTrace();
							Toast.makeText(getActivity(), "failed to save new message!", Toast.LENGTH_LONG).show();
						}
					}
				});
				etMessage.setText("");
			}
    	});
	}
    	
    private static int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private void receiveMessage() {
        // Construct query to execute
    	ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
    	query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
    	query.whereEqualTo("itemId", itemId);
    	query.orderByAscending("createdAt");
        // Execute query for messages asynchronously
		query.findInBackground(new FindCallback<Message>() {
			public void done(List<Message> messages, ParseException e) {
				if (e == null) {					
					mMessages.clear();
					mMessages.addAll(messages);
					mAdapter.notifyDataSetChanged();
					lvChat.invalidate();
				} else {
					e.printStackTrace();
					Log.d("message", "Error: " + e.getMessage());
				}
			}
		});
    }
    
	private void refreshMessages() {
		receiveMessage();		
	}
	
}
