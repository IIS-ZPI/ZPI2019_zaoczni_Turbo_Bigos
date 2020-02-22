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
    private ArrayList<Kurs> rates = new ArrayList<>();

    public ArrayList<Kurs> getRates() {
        return rates;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Kurs {
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

    public String Laczenie(String url) throws Exception {
        OkHttpClient ohc = new OkHttpClient();
        Request zapytanie = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = ohc.newCall(zapytanie).execute();
            return response.body().string();
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

    public static String Pobieranie(final String url) {
        final CountDownLatch cdl = new CountDownLatch(1);
        final String[] dane = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String responseString = null;
                try {
                    PobieranieDanych pd = new PobieranieDanych();
                    responseString = pd.Laczenie(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dane[0] = responseString;
                cdl.countDown();
            }
        });
        thread.start();
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dane[0];
    }
}