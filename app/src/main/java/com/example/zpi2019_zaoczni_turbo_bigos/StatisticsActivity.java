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

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class StatisticsActivity extends Fragment {

    ListView listaOkresow;
    ListView listaWalut;

    TextView okres_czasowy_txt;
    TextView waluta_txt;

    String selectedOkres;
    String selectedWaluta;
    String currency;

    Zbior zbior;

    private Button btnMiary;
    public String url;

    View rootView;

    double odch_standardowe;
    float dominanta;
    float mediana;

    public static String getData(final String url) {
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] outputString = {null};
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                String responseString = null;
                PobieranieDanych pobieranieDanych = new PobieranieDanych();
                try {
                    responseString = pobieranieDanych.Pobieranie(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                outputString[0] = responseString;
                latch.countDown();
            }
        });
        thread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return outputString[0];
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (View)inflater.inflate(R.layout.activity_statistics,container,false);


        listaOkresow = rootView.findViewById(R.id.lista_okresow);
        listaWalut = rootView.findViewById(R.id.lista_walut2);

        okres_czasowy_txt = rootView.findViewById(R.id.okres_czasowy_txt3);
        waluta_txt = rootView.findViewById(R.id.waluta_txt3);

        List<String> okresy_list = new ArrayList<String>(Arrays.asList(zbior.okresy));
        List<String> waluty_list = new ArrayList<String>(Arrays.asList(zbior.waluty));

        ArrayAdapter<String> arrayAdapterOkresy = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, okresy_list);
        ArrayAdapter<String> arrayAdapterWaluty = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, waluty_list);

        listaOkresow.setAdapter(arrayAdapterOkresy);
        listaWalut.setAdapter(arrayAdapterWaluty);

        btnMiary = rootView.findViewById(R.id.btnMiary);
        btnMiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liczenie_miar();
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


    public void liczenie_miar() {

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
        Waluta data = new Gson().fromJson(getData(url), Waluta.class);
//-----------------------------------------------------------------------------------
        List<Float> lista = new ArrayList<>();

        int n;

        float suma = 0;
        float srednia ;


        for (KursWaluty waluty : data.getRates()) {
            lista.add(waluty.getAsk());
            suma = suma + waluty.getAsk();
        }

        n = lista.size();




        // odchylenie standardowe
        srednia = suma/n;
        float wariancja = 0;
        for( int i=0 ; i<n ; i++)
        {
           wariancja = wariancja +( (lista.get(i)-srednia) * (lista.get(i)-srednia) );
        }
        wariancja = wariancja/n;
        odch_standardowe = Math.sqrt(Double.parseDouble(String.valueOf(wariancja)));

        //mediana

        Collections.sort(lista);
        int middle = lista.size() / 2;
        middle = middle > 0 && middle % 2 == 0 ? middle - 1 : middle;
        mediana = lista.get(middle);

        //dominanta
        List<Float> wartosci = new ArrayList<>();
        List<Integer> ilosci = new ArrayList<>();

        int liczba = 0;
        wartosci.add(lista.get(0));

        int ilosc = 1;

        for(int i=1 ; i<lista.size() ; i++)
        {
            if(lista.get(i) == wartosci.get(liczba))
            {
                ilosc++;
            }
            else
            {
                ilosci.add(ilosc);

                wartosci.add(lista.get(i));
                ilosc = 1;
                liczba++;
            }
        }
        ilosci.add(ilosc);


        int max = ilosci.get(0);
        int maxindex = 0;
        for(int i=1 ; i<wartosci.size() ; i++)
        {
            if(max<ilosci.get(i))
            {
                maxindex=i;
                max=ilosci.get(i);
            }
        }
        dominanta=wartosci.get(maxindex);

//-----------------------------------------------------------------------------------

        TextView mediana_txt = rootView.findViewById(R.id.mediana_txt);
        TextView dominanta_txt = rootView.findViewById(R.id.dominanta_txt);
        TextView odch_standardowe_txt = rootView.findViewById(R.id.odch_standardowe_txt);
        mediana_txt.setText(Float.toString(mediana));
        dominanta_txt.setText(Float.toString(dominanta));
        odch_standardowe_txt.setText(Double.toString(odch_standardowe));


    }

}
