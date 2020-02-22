package com.example.zpi2019_zaoczni_turbo_bigos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.google.gson.Gson;


public class SesjeActivity extends Fragment {

    ListView listaOkresow;
    ListView listaWalut;

    TextView okres_czasowy_txt;
    TextView waluta_txt;

    String selectedOkres;
    String selectedWaluta;
    String currency;

    Zbior zbior;
    private Button button_obl;
    public String url;

    View rootView;

    Boolean m = false;
    Boolean w = false;
    Boolean s = false;
    int malejace;
    int stale;
    int wzrostowe;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (View) inflater.inflate(R.layout.activity_sesje, container, false);



        listaOkresow = rootView.findViewById(R.id.lista_okresow);
        listaWalut = rootView.findViewById(R.id.lista_walut2);

        okres_czasowy_txt = rootView.findViewById(R.id.okres_czasowy_txt);
        waluta_txt = rootView.findViewById(R.id.waluta_txt);

        List<String> okresy_list = new ArrayList<String>(Arrays.asList(zbior.okresy));
        List<String> waluty_list = new ArrayList<String>(Arrays.asList(zbior.waluty));

        ArrayAdapter<String> arrayAdapterOkresy = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, okresy_list);
        ArrayAdapter<String> arrayAdapterWaluty = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, waluty_list);

        listaOkresow.setAdapter(arrayAdapterOkresy);
        listaWalut.setAdapter(arrayAdapterWaluty);

              button_obl = rootView.findViewById(R.id.liczSesje);
        button_obl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                liczenie_sesji();
            }
        });

        listaOkresow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedOkres = (String) parent.getItemAtPosition(position);
                okres_czasowy_txt.setText(selectedOkres);
            }
        });

        listaWalut.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedWaluta = (String) parent.getItemAtPosition(position);
                waluta_txt.setText(selectedWaluta);
            }
        });

        return rootView;
    }

    public void liczenie_sesji() {

        String dateToday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if(selectedOkres == "Tydzień")
        {
            calendar.add(Calendar.WEEK_OF_MONTH, -1);
        }
        else if(selectedOkres == "2 tygodnie")
        {
            calendar.add(Calendar.WEEK_OF_MONTH, -2);
        }
        else if(selectedOkres == "Miesiąc")
        {
            calendar.add(Calendar.MONTH, -1);
        }
        else if(selectedOkres == "Kwartał")
        {
            calendar.add(Calendar.MONTH, -3);
        }
        else if(selectedOkres == "Półrocze")
        {
            calendar.add(Calendar.MONTH, -6);
        }
        else if(selectedOkres == "Rok")
        {
            calendar.add(Calendar.YEAR, -1);
        }


        for(int i=0; i<zbior.waluty.length ;i++)
        {
            if(selectedWaluta == zbior.waluty[i])
            {
                currency = selectedWaluta.toLowerCase();
            }
        }

        String dateFrom=zbior.dateFormat.format(calendar.getTime());
        url=("https://api.nbp.pl/api/exchangerates/rates/c/"+currency+"/"+dateFrom+"/"+dateToday+"/?format=json");
        Waluta data = new Gson().fromJson(PobieranieDanych.Pobieranie(url), Waluta.class);

        malejace = 0;
        stale = 0;
        wzrostowe = 0;
        List<Float> lista = new ArrayList<>();

        for (Kurs waluty : data.getRates()) {
            System.out.println(waluty.getAsk() + " + ");
            lista.add(waluty.getAsk());
            if (lista.size() > 1) {
                if (lista.get(lista.size() - 1) < lista.get(lista.size() - 2)) {
                    w = false;
                    s = false;
                    if (m != true) {
                        m = true;
                        malejace++;
                    }
                } else if (lista.get(lista.size() - 1) > lista.get(lista.size() - 2)) {
                    s = false;
                    m = false;
                }
                if (w != true) {
                    w = true;
                    wzrostowe++;
                } else if (lista.get(lista.size() - 1) == lista.get(lista.size() - 2)) {
                    w = false;
                    m = false;
                    if (s != true) {
                        s = true;
                        stale++;
                    }
                }
            }
        }


        TextView constant = rootView.findViewById(R.id.stale_text);
        TextView growth = rootView.findViewById(R.id.up_text);
        TextView decreasing = rootView.findViewById(R.id.down_text);
        constant.setText(Integer.toString(stale));
        decreasing.setText(Integer.toString(malejace));
        growth.setText(Integer.toString(wzrostowe));


    }


}



