package com.example.zpi2019_zaoczni_turbo_bigos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager strona;
    private PagerAdapter stronaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<Fragment> lista_podstron = new ArrayList<>();
        lista_podstron.add(new SesjeActivity());
        lista_podstron.add(new WalutyActivity());
        lista_podstron.add(new StatisticsActivity());

        strona = findViewById(R.id.MainVievPager);
        stronaAdapter = new Adapter_zmiany_stron(getSupportFragmentManager(), lista_podstron);
        strona.setAdapter(stronaAdapter);
        strona.setCurrentItem(1);
    }
}
