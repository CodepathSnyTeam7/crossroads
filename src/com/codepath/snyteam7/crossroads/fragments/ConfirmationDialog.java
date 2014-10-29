package com.codepath.snyteam7.crossroads.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.codepath.snyteam7.crossroads.R;

public class ConfirmationDialog extends DialogFragment {

	public static ConfirmationDialog newInstance(String title) {
		ConfirmationDialog frag = new ConfirmationDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}
	
	private ConfirmationDialogListener listener;
	
	public interface ConfirmationDialogListener {
		void onOk();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");

		return new AlertDialog.Builder(getActivity())
				.setIcon(R.drawable.ic_action_reject)
				.setTitle(title)
				.setPositiveButton(R.string.alert_dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
										listener.onOk();
							}
						})
				.setNegativeButton(R.string.alert_dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dismiss();
							}
						}).create();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (ConfirmationDialogListener)activity;
	}
}
