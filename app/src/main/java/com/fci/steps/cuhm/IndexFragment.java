package com.fci.steps.cuhm;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public IndexFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_index, container, false);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.view_pager);

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.getTabAt(0).setIcon(R.mipmap.ic_home_icon);
                tabLayout.getTabAt(1).setIcon(R.mipmap.ic_notif);
                tabLayout.getTabAt(2).setIcon(R.mipmap.ic_account_icon);
                tabLayout.getTabAt(3).setIcon(R.mipmap.ic_contact_icon);
            }
        });
        return view;
    }
}
