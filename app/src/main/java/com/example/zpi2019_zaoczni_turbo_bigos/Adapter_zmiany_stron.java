package com.example.zpi2019_zaoczni_turbo_bigos;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Adapter_zmiany_stron extends FragmentStatePagerAdapter {

    private List<Fragment> lista_podstron;

    public Adapter_zmiany_stron(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.lista_podstron = fragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        return lista_podstron.get(position);
    }

    @Override
    public int getCount() {
        return lista_podstron.size();
    }
}
