package com.gemini_soft.noteitdown;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

   private SeekBar mSeekBar;
   private TextView mTextSizeSample;
   private String mSampleString;
   private SettingsHelper mSettingsHelper;
   private EditText mFileName;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_settings);

      mTextSizeSample = findViewById(R.id.tv_text_size_sample);
      mSeekBar = findViewById(R.id.sb_font_size);
      mFileName = findViewById(R.id.et_file_name);

//      Bundle arguments = getIntent().getExtras();
//      if (arguments != null) {
//         // получаем объект настроек, переданный из главной активности
//         mSettingsHelper = (SettingsHelper) arguments.getSerializable(SettingsHelper.class.getSimpleName());
//
//         if (mSettingsHelper != null) {
//            mTextSizeSample.setTextSize(mSettingsHelper.getFontSize());
//            mFileName.setText(mSettingsHelper.getFileName());
//         }
//
//      }

      mSettingsHelper = new SettingsHelper(this);
      mTextSizeSample.setTextSize(mSettingsHelper.getFontSize());
      mFileName.setText(mSettingsHelper.getFileName());

      // подключаем слушателя к ползунку
      mSeekBar.setOnSeekBarChangeListener(this);
      // получаем начальную строку из ресурсов. она будет использоваться для формирования
      // окончательной строки для отображения пользователю
      mSampleString = getResources().getString(R.string.text_size_sample);

      mSeekBar.setProgress(mSettingsHelper.getFontSize());

      // устанавливаем значение текста
      SetSampleTextValue();

      // при открытии помещаем курсор в конец поля имени файла
      mFileName.setSelection(mFileName.getText().length());
   }

   @Override
   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      if (progress >= 18 && progress <= 60) {
         // в пределах от 18 до 60 устанавливаем размер текста
         mTextSizeSample.setTextSize(progress);
      }
      else {
         // вне предела устанавливаем ползунок в значение по умолчанию
         seekBar.setProgress(18);
      }
      SetSampleTextValue();
   }

   private void SetSampleTextValue() {
      // получаем текущий размер шрифта из значения прогресса ползунка
      int fontSize = mSeekBar.getProgress();
      String tempString = mSampleString + " (" + fontSize + ")";
      // устанавливаем текст
      mTextSizeSample.setText(tempString);
   }

   @Override
   public void onStartTrackingTouch(SeekBar seekBar) {

   }

   @Override
   public void onStopTrackingTouch(SeekBar seekBar) {

   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.settings_menu, menu);

      return super.onCreateOptionsMenu(menu);
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {

      if (item.getItemId() == R.id.action_back) {
         // перед закрытием, сохраняем настроку
         if (mSettingsHelper != null) {
            mSettingsHelper.setFontSize(mSeekBar.getProgress());
            mSettingsHelper.setFileName(mFileName.getText().toString());
         }
         // закрыть текущую активность
         finish();
      }

      return super.onOptionsItemSelected(item);
   }
}
