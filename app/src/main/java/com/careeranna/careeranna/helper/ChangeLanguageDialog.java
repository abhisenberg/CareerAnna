package com.careeranna.careeranna.helper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Constants;

import io.paperdb.Paper;

public class ChangeLanguageDialog implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    /*
    1 - English
    2 - Hindi
    3 - Tamil
    4 - Telugu
     */

    private Dialog dialog;
    private int language;
    private Context context;

    public ChangeLanguageDialog(final Context context){
        this.context = context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_content_language);

        Paper.init(context);

        language = getPreviousLanguage();

        RadioGroup radioGroup = dialog.findViewById(R.id.rg_language_group);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(getRadioButtonID());

        TextView tv_cancel, tv_ok;
        tv_cancel = dialog.findViewById(R.id.tv_cancel);
        tv_ok = dialog.findViewById(R.id.tv_ok);
        tv_cancel.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
    }

    public void showDialog(){
        dialog.show();
    }

    private int getPreviousLanguage(){
        int lang;

        try {
            lang = Paper.book().read(Constants.LANGUAGE);
        } catch (NullPointerException e){
            lang = 1;
            Paper.book().write(Constants.LANGUAGE, lang);
        }

        return lang;
    }

    private void setLanguage(){
        Paper.book().write(Constants.LANGUAGE, language);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_english:
                language = 1;
                break;
            case R.id.rb_hindi:
                language= 2;
                break;
            case R.id.rb_tamil:
                language = 3;
                break;
            case R.id.rb_telugu:
                language = 4;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dialog.cancel();
                break;

            case R.id.tv_ok:
                setLanguage();
                Toast.makeText(context, "You can now watch videos in "+getLanguageName(), Toast.LENGTH_SHORT).show();
                dialog.cancel();
                break;
        }
    }

    private int getRadioButtonID(){
        switch (language){
            case 1:
                return R.id.rb_english;

            case 2:
                return R.id.rb_hindi;

            case 3:
                return R.id.rb_tamil;

            case 4:
                return R.id.rb_telugu;
        }

        return 1;
    }

    private String getLanguageName(){
        switch (language){
            case 1:
                return "English";

            case 2:
                return "Hindi";

            case 3:
                return "Tamil";

            case 4:
                return "Telugu";
        }
        return "English";
    }
}
