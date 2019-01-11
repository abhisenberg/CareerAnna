package com.careeranna.careeranna.helper;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
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
    5 - Bengali
    6 - Marathi
    7 - Kannada
    8 - Gujarati
    9 - Malayalam
    10 - Punjabi
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

//        int width = (int) (context.getResources().getDisplayMetrics().widthPixels);
//        int height = (int) (context.getResources().getDisplayMetrics().heightPixels);
//        dialog.getWindow().setLayout(width, height);

        Paper.init(context);

        language = getPreviousLanguage();

        RadioGroup radioGroup = dialog.findViewById(R.id.rg_language_group);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(getRadioButtonID());

        /*
        Enable this radio group when languages are available
         */
        RadioGroup radioGroupComingSoon = dialog.findViewById(R.id.rg_language_group_coming_soon);

        View tv_cancel, tv_ok;
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
        Log.d("ChangeLanguage", language+"");
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
                Toast.makeText(context, context.getResources().getText(R.string.you_can_view_content_in_language)+" "+getLanguageName(), Toast.LENGTH_SHORT).show();
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
