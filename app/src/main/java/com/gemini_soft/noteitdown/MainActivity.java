package com.gemini_soft.noteitdown;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

   private static final String DIR_NAME = "NoteItDown";
   private static final String LOG_TAG = "NIDL";

   private EditText mEditText;
   private String mFileName;

   private boolean isWorking;

   private SettingsHelper mSettingsHelper;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // эта переменная нужна как флаг для чтения настроек
      isWorking = true;

      mSettingsHelper = new SettingsHelper(this);

      mFileName = mSettingsHelper.getFileName();

      // поле ввода текста. окно редактора
      mEditText = findViewById(R.id.etEditor);
      // устанавливаем размер шрифта из настроек
      mEditText.setTextSize(mSettingsHelper.getFontSize());

      // при открытии проги, сразу загружаем данные из файла
      OpenFile();

   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main_menu, menu);

      return super.onCreateOptionsMenu(menu);
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {

      // получаем int из нажатого пункта меню. принимаем решение в зависимости от id
      switch (item.getItemId()) {
         case R.id.action_open:
            OpenFile();
            break;
         case R.id.action_save:
            SaveFile();
            break;
         case R.id.action_settings:
            OpenSettings();
      }

      return super.onOptionsItemSelected(item);
   }

   @Override
   protected void onStop() {
      super.onStop();

      // работа активности была приостановлена
      isWorking = false;

      // сохраняем файл, при приостановке работы активности
      SaveFile();
   }

   @Override
   protected void onStart() {
      super.onStart();

      if (!isWorking) {
         // работа активности была восстановлена, после вызова другой активности
         // надо перечитать настройки
         mEditText.setTextSize(mSettingsHelper.getFontSize());
         mFileName = mSettingsHelper.getFileName();
      }

      isWorking = true;
   }

   private void OpenSettings() {
      // получаем объект активности
      Intent intent = new Intent(this, SettingsActivity.class);

      //intent.putExtra(SettingsHelper.class.getSimpleName(), mSettingsHelper);
      // запускаем окно активности
      startActivity(intent);

      Log.d(LOG_TAG, "Start settings activity; " + isWorking);
      isWorking = false;
   }

   private void SaveFile() {
      try {

         // для преобразования байтового потока в поток символов используем объект OutputStream
         // открываем (создаём и открываем, если не существует) частный файл для записи
         OutputStream outputStream = openFileOutput(mFileName, 0);
         // для преобразования потока символов в поток байтов создаём этот объект
         // и передаём ему поток файла
         OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
         // преобразуем поток данных в строку и записываем в файл
         outputStreamWriter.write(mEditText.getText().toString());
         // освобождаем ресурсы
         outputStreamWriter.close();
         outputStream.close();

         Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

      } catch (Throwable t) {
         // обработка исключения. выводим что пошло не так
         Toast.makeText(this, "Xcptn: " + t.toString(), Toast.LENGTH_SHORT).show();
      }
   }

   private void OpenFile() {
      try {
         // создаём байтовый поток и передаём ему файл
         InputStream inputStream = openFileInput(mFileName);
         if (inputStream != null) {

            // для преобразования байтового потока в поток символов
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            // класс "обёртка" для существующего класса чтения. передаём ему поток из файла
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder builder = new StringBuilder();
            // пока строка читается, формируем выходную строку
            while ((line = reader.readLine()) != null) {
               builder.append(line + "\n");
            }

            inputStream.close();
            // отображаем сформированную строку в пользовательском виджете
            mEditText.setText(builder.toString());
            // после чтения файла, помещаем курсор в конец текста
            mEditText.setSelection(mEditText.getText().length());

            Toast.makeText(this, "Read", Toast.LENGTH_SHORT).show();
         }

      } catch (Throwable t) {
         Toast.makeText(this, "Xcptn: " + t.toString(), Toast.LENGTH_SHORT).show();
      }
   }
}
