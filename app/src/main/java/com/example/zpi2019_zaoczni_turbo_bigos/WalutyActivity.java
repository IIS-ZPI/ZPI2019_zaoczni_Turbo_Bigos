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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class WalutyActivity extends Fragment {

    ListView listaWalutZ;
    ListView listaWalutDO;

    TextView waluta_z_txt;
    TextView waluta_do_txt;
    EditText ilosc_waluty;

    String selectedWalutaZ;
    String selectedWalutaDO;
    String currencyZ;
    String currencyDO;

    float wynik;

    Zbior zbior;

    private Button button_przelicz;
    public String urlZ;
    public String urlDO;

    View rootView;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_waluty, container, false);

        listaWalutZ = rootView.findViewById(R.id.waluta_z);
        listaWalutDO = rootView.findViewById(R.id.waluta_do);

        waluta_z_txt = rootView.findViewById(R.id.waluta_z_txt);
        waluta_do_txt = rootView.findViewById(R.id.waluta_zdo_txt);
        ilosc_waluty = rootView.findViewById(R.id.editText2);


        List<String> waluty_list = new ArrayList<String>(Arrays.asList(zbior.waluty));
        ArrayAdapter<String> arrayAdapterWaluty = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, waluty_list);

        listaWalutZ.setAdapter(arrayAdapterWaluty);
        listaWalutDO.setAdapter(arrayAdapterWaluty);

        button_przelicz = rootView.findViewById(R.id.button_przelicz);
        button_przelicz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                przeliczenie_walut();
            }
        });

        listaWalutZ.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedWalutaZ = (String) parent.getItemAtPosition(position);
                waluta_z_txt.setText(selectedWalutaZ);
            }
        });

        listaWalutDO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedWalutaDO = (String) parent.getItemAtPosition(position);
                waluta_do_txt.setText(selectedWalutaDO);
            }
        });


        return rootView;
    }


    public void przeliczenie_walut() {

        String dateToday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        currencyZ = selectedWalutaZ.toLowerCase();
        currencyDO = selectedWalutaDO.toLowerCase();

        urlZ=("https://api.nbp.pl/api/exchangerates/rates/c/"+currencyZ+"/"+dateToday+"/"+dateToday+"/?format=json");
        urlDO=("https://api.nbp.pl/api/exchangerates/rates/c/"+currencyDO+"/"+dateToday+"/"+dateToday+"/?format=json");

        Waluta dataZ = new Gson().fromJson(PobieranieDanych.getData(urlZ), Waluta.class);
        Waluta dataDO = new Gson().fromJson(PobieranieDanych.getData(urlDO), Waluta.class);
//-----------------------------------------------------------------------------------

        float z = 0;
        for (KursWaluty waluty : dataZ.getRates()) {
            z = waluty.getAsk();
        }

        float d = 0;
        for (KursWaluty waluty : dataDO.getRates()) {
            d = waluty.getAsk();
        }

        String war = String.valueOf(ilosc_waluty.getText());

       wynik = z * Float.parseFloat(war) /d;

//-----------------------------------------------------------------------------------

        TextView w = rootView.findViewById(R.id.wynik);
        //w.setText(Float.toString(wynik));
        w.setText(war+" "+selectedWalutaZ+" jest równe "+wynik+" "+selectedWalutaDO);

    }



















}
