package com.careeranna.careeranna.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.careeranna.careeranna.R;

public class ForgetDialog extends AppCompatDialogFragment {

    private EditText email;

    private ForgetPasswordClickListener forgetPasswordClickListener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.forgot_layout, null);

        builder.setView(view)
                .setTitle("Forget Password Enter Regestered Email")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String useremail = email.getText().toString();

                        forgetPasswordClickListener.sendMail(useremail);
                    }
                });

         email = view.findViewById(R.id.forget_email);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            forgetPasswordClickListener = (ForgetPasswordClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement forgetPasswordListener");
        }
    }

    public interface ForgetPasswordClickListener {
        void sendMail(String useremail);
    }
}
