package br.com.stemcell.android.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by spassu on 04/08/15.
 */
public class NotificationDialogFragment extends DialogFragment {
    private static final String TAG = "NotificationDialogFragment";
    private static final String DIALOG_TYPE = "dialogType";
    private static final String DIALOG_MESSAGE = "dialogMessage";
    public static final int ALERT_DIALOG = 0;
    public static final int ERROR_DIALOG = 1;

    public static NotificationDialogFragment newInstance(int dialogType, String message) {
        NotificationDialogFragment frag = new NotificationDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_TYPE, dialogType);
        args.putString(DIALOG_MESSAGE, message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*int dialogType = getArguments().getInt(DIALOG_TYPE);
        String dialogMessage = getArguments().getString(DIALOG_MESSAGE);

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((FragmentAlertDialog)getActivity()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((FragmentAlertDialog)getActivity()).doNegativeClick();
                            }
                        }
                )
                .create();*/

        /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(mMessageToDisplay);
        alertDialog.setNeutralButton(R.string.label_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        return alertDialog.create();*/

        return null;
    }
}
