package com.asa.texttotab.tablet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.asa.texttotab.R;

public class MyAlertDialogFragment extends DialogFragment {
	public static MyAlertDialogFragment newInstance(int title, int message) {
		MyAlertDialogFragment frag = new MyAlertDialogFragment();
		Bundle args = new Bundle();
		args.putInt("title", title);
		args.putInt("message", message);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");
		int message = getArguments().getInt("message");

		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(R.string.button_okay,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						})
				.setNegativeButton(R.string.button_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).create();
	}
}
