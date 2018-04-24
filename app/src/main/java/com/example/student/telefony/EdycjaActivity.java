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

public class EdycjaActivity extends AppCompatActivity {

    private EditText producent;
    private EditText model ;
    private EditText wersja;
    private EditText www ;
    Button zapisz;
    Button strona;
    Button anuluj;

    private boolean isEmpty(EditText string) {
        if (!string.getText().toString().equals(""))
            return false;

        return true;
    }


    private boolean producentIsEmpty=true;
    private boolean modelIsEmpty=true;
    private boolean wersjaIsEmpty=true;
    private boolean wwwIsEmpty=true;
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
                    producent.setError("Wypełnij");
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
                if(isEmpty(wersja))
                    wersja.setError("Wypełnij");
            }
        });
        www.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(isEmpty(www))
                    www.setError("Wypełnij");
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
                    producentIsEmpty=true;
                }
                else {
                    producentIsEmpty = false;
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
                    modelIsEmpty=true;
                }
                else {
                    modelIsEmpty = false;
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
                if(isEmpty(wersja))
                {
                    wersjaIsEmpty=true;
                }
                else {
                    wersjaIsEmpty = false;
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
                if(isEmpty(www))
                {
                    wwwIsEmpty=true;
                }
                else {
                    wwwIsEmpty = false;
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
       // czyWypelnione();

        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!producentIsEmpty && !modelIsEmpty && !wersjaIsEmpty && !wwwIsEmpty) {
                    if(nrWiersza != -1)
                        edytujWartosc();
                    else
                        dodajWartosc();
                        zapisz.setVisibility(View.GONE);
                }
                else
                    Toast.makeText(EdycjaActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
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
