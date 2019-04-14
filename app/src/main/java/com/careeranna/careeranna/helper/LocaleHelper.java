package com.careeranna.careeranna.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static Context onAttach( Context context ) {

        String lang = getPersistedData(context, Locale.getDefault().getLanguage());

        return setLocale(context, lang);

    }

    public static Context setLocale(Context context, String lang) {

        persist(context, lang);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResource(context, lang);
        }
        return updateResourceLegacy(context, lang);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResource(Context context, String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }


    @SuppressWarnings("deprecation")
    private static Context updateResourceLegacy(Context context, String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        } else {
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }

        return context;
    }

    private static void persist(Context context, String language) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    private static String getPersistedData(Context context, String language) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SELECTED_LANGUAGE, language);
    }

    public static Context onAttach( Context context, String defaultLanguage) {

        String lang = getPersistedData(context, Locale.getDefault().getLanguage());

        return setLocale(context, lang);

    }
}
