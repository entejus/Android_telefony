package com.example.student.telefony;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class EdycjaActivity extends AppCompatActivity {

    private EditText producent;
    private EditText model ;
    private EditText wersja;
    private EditText www ;
    private Button zapisz;
    private Button strona;
    private Button anuluj;

    private boolean isEmpty(EditText string) {
        if (!string.getText().toString().equals(""))
            return false;
        return true;
    }


    private  static  boolean isValidVersion(String tekst)
    {
        return Pattern.compile("^(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)$").matcher(tekst).matches();

    }

    private  static  boolean isValidAdress(String tekst)
    {
        return Pattern.compile("^(http\\:\\/\\/[a-zA-Z0-9_\\-]+(?:\\.[a-zA-Z0-9_\\-]+)*" +
                "\\.[a-zA-Z]{2,4}(?:\\/[a-zA-Z0-9_]+)*(?:\\/[a-zA-Z0-9_]+\\.[a-zA-Z]{2,4}" +
                "(?:\\?[a-zA-Z0-9_]+\\=[a-zA-Z0-9_]+)?)?(?:\\&[a-zA-Z0-9_]+\\=[a-zA-Z0-9_]+)*)$").matcher(tekst).matches();
    }

    private boolean producentIsCorrect=false;
    private boolean modelIsCorrect=false;
    private boolean wersjaIsCorrect=false;
    private boolean wwwIsCorrect=false;
    private long    nrWiersza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edycja);

        zapisz = (Button) findViewById(R.id.zapisz);
        strona = (Button) findViewById(R.id.strona);
        anuluj = (Button) findViewById(R.id.anuluj);
        producent = (EditText) findViewById(R.id.producent);
        model = (EditText) findViewById(R.id.model);
        wersja = (EditText) findViewById(R.id.wersja);
        www = (EditText) findViewById(R.id.www);
        producent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(isEmpty(producent))
                {
                    producent.setError("Wypełnij!");
                }
            }
        });
        model.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(isEmpty(model))
                    model.setError("Wypełnij");
            }
        });
        wersja.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!isValidVersion(wersja.getText().toString()))
                    wersja.setError("Podaj poprawną wersję");
            }
        });
        www.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!isValidAdress(www.getText().toString()))
                    www.setError("Podaj poprawny adres");
            }
        });

        producent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isEmpty(producent))
                {
                    producentIsCorrect=false;
                }
                else {
                    producentIsCorrect = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        model.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isEmpty(model))
                {
                    modelIsCorrect=false;
                }
                else {
                    modelIsCorrect = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        wersja.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isValidVersion(wersja.getText().toString()))
                {
                    wersjaIsCorrect=true;
                }
                else {
                    wersjaIsCorrect = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        www.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isValidAdress(www.getText().toString()))
                {
                    wwwIsCorrect=true;
                }
                else {
                    wwwIsCorrect = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nrWiersza = -1;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            nrWiersza = bundle.getLong(PomocnikBD.ID);
        }
        if(nrWiersza != -1)
        {
            wypelnijPola();
        }

        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(producentIsCorrect && modelIsCorrect && wersjaIsCorrect && wwwIsCorrect) {
                    if(nrWiersza != -1)
                        edytujWartosc();
                    else
                        dodajWartosc();
                    zapisz.setVisibility(View.GONE);
                }
                else
                    Toast.makeText(EdycjaActivity.this, "Wypełnij poprawnie wszystkie pola", Toast.LENGTH_SHORT).show();
            }
        });

        anuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zamiar = new Intent();
                setResult(RESULT_CANCELED,zamiar);
                finish();
            }
        });

        strona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText www = (EditText) findViewById(R.id.www);
                String adres = www.getText().toString();
                if(adres.startsWith("http://")) {
                    Intent zamiarPrzegladarki = new Intent("android.intent.action.VIEW", Uri.parse(adres));
                    startActivity(zamiarPrzegladarki);
                }
                else {
                    adres = "http://"+adres;
                    Intent zamiarPrzegladarki = new Intent("android.intent.action.VIEW", Uri.parse(adres));
                    startActivity(zamiarPrzegladarki);
                }
            }
        });

    }

    private void dodajWartosc()
    {
        ContentValues wartosci = new ContentValues();

        producent = (EditText) findViewById(R.id.producent);
        model = (EditText) findViewById(R.id.model);
        wersja = (EditText) findViewById(R.id.wersja);
        www = (EditText) findViewById(R.id.www);

        wartosci.put(PomocnikBD.PRODUCENT,producent.getText().toString());
        wartosci.put(PomocnikBD.MODEL,model.getText().toString());
        wartosci.put(PomocnikBD.WERSJA_ANDROID,wersja.getText().toString());
        wartosci.put(PomocnikBD.WWW,www.getText().toString());

        Uri uriNowego = getContentResolver().insert(TelefonyProvider.URI_ZAWARTOSCI,wartosci);
        Toast.makeText(this, "Zapisano", Toast.LENGTH_SHORT).show();
    }

    private void edytujWartosc()
    {
        ContentValues wartosci = new ContentValues();
        String selekcja = PomocnikBD.ID+"="+nrWiersza;

        producent = (EditText) findViewById(R.id.producent);
        model = (EditText) findViewById(R.id.model);
        wersja = (EditText) findViewById(R.id.wersja);
        www = (EditText) findViewById(R.id.www);

        wartosci.put(PomocnikBD.PRODUCENT,producent.getText().toString());
        wartosci.put(PomocnikBD.MODEL,model.getText().toString());
        wartosci.put(PomocnikBD.WERSJA_ANDROID,wersja.getText().toString());
        wartosci.put(PomocnikBD.WWW,www.getText().toString());

        getContentResolver().update(TelefonyProvider.URI_ZAWARTOSCI,wartosci,selekcja,null);
        Toast.makeText(this, "Zapisano", Toast.LENGTH_SHORT).show();

    }

    private void wypelnijPola()
    {
        String projekcja[] = {PomocnikBD.PRODUCENT,PomocnikBD.MODEL,PomocnikBD.WERSJA_ANDROID,PomocnikBD.WWW};
        String selekcja = PomocnikBD.ID+"="+nrWiersza;
        Cursor kursor = getContentResolver().query(TelefonyProvider.URI_ZAWARTOSCI,projekcja,selekcja,null,null);
        kursor.moveToFirst();
        int indeksKolumny = kursor .getColumnIndexOrThrow(PomocnikBD.PRODUCENT);
        String wartosc = kursor.getString(indeksKolumny);
        producent.setText(wartosc);
        model.setText(kursor.getString(kursor.getColumnIndexOrThrow(PomocnikBD.MODEL)));
        wersja.setText(kursor.getString(kursor .getColumnIndexOrThrow(PomocnikBD.WERSJA_ANDROID)));
        www.setText(kursor.getString(kursor .getColumnIndexOrThrow(PomocnikBD.WWW)));
        kursor.close();
    }
}
