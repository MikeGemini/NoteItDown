package com.gemini_soft.noteitdown;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

class SettingsHelper implements Serializable {
   private static final String PREFERENCES_NAME = "NoteItDownPreferences";
   private static final String PREF_FONT_SIZE_NAME = "FontSize";
   private static final String PREF_FILE_NAME = "FileName";
   private SharedPreferences mPreferences;
   private SharedPreferences.Editor mEditor;

   private int mFontSize;
   private String mFileName;


   SettingsHelper(Context context) {
      mPreferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
   }

   int getFontSize() {
      // получаем значение настройки по имени
      mFontSize = mPreferences.getInt(PREF_FONT_SIZE_NAME, 24);

      return mFontSize;
   }

   void setFontSize(int fontSize) {
      mFontSize = fontSize;
      mEditor = mPreferences.edit();
      // для редактирования настроек получаем объект редактора
      // присвоить новое значение настройке
      mEditor.putInt(PREF_FONT_SIZE_NAME, mFontSize);
      // применить изменения для всех настроек
      mEditor.apply();
   }

   String getFileName() {
      mFileName = mPreferences.getString(PREF_FILE_NAME, "current.txt");

      return mFileName;
   }

   void setFileName(String fileName) {
      mFileName = fileName;

      mEditor = mPreferences.edit();
      mEditor.putString(PREF_FILE_NAME, mFileName);
      mEditor.apply();
   }
}
