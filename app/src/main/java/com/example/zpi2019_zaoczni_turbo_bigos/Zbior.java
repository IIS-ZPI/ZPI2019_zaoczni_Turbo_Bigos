package com.example.zpi2019_zaoczni_turbo_bigos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Zbior {

    public static String[] okresy = new String[]{"Tydzień", "2 tygodnie", "Miesiąc", "Kwartał", "Półrocze", "Rok"};
    public static String[] waluty = new String[]{"USD", "GBP", "CHF", "EUR"};

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Waluta {
    private String table;
    private String currency;
    private String code;
    private ArrayList<KursWaluty> rates = new ArrayList<>();

    public ArrayList<KursWaluty> getRates() {
        return rates;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class KursWaluty {
    private String no;
    private String effectiveDate;
    private float ask;
    private float bid;

    public float getAsk() {
        return ask;
    }

    public float getBid() {
        return bid;
    }
}

class PobieranieDanych {

    private final OkHttpClient httpClient = new OkHttpClient();

    public String Pobieranie(String url) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            return response.body().string();
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

    public static String getData(final String url) {
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] outputString = {null};
        Thread thread = new Thread(new Runnable() {
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
}