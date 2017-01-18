package dasturchi.uz.kitoblar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import dasturchi.uz.kitoblar.R;

/**
 * Created by Ahmadjon on 30.12.2016.
 */
public class FragmentDrawer extends MyFragment implements View.OnClickListener {

    RelativeLayout favouritesRelativeLayout;
    RelativeLayout settingsRelativeLayout;
    RelativeLayout aboutRelativeLayout;
    RelativeLayout advertiseRelativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_drawer, null, false);

//        favouritesRelativeLayout = (RelativeLayout) parentView.findViewById(R.id.relative_layout_favourites);
//        favouritesRelativeLayout.setOnClickListener(this);

        settingsRelativeLayout = (RelativeLayout) parentView.findViewById(R.id.relative_layout_settings);
        settingsRelativeLayout.setOnClickListener(this);

//        aboutRelativeLayout = (RelativeLayout) parentView.findViewById(R.id.relative_layout_about);
//        aboutRelativeLayout.setOnClickListener(this);
//
//        advertiseRelativeLayout = (RelativeLayout) parentView.findViewById(R.id.relative_layout_advertise);
//        advertiseRelativeLayout.setOnClickListener(this);

        return parentView;
    }

    @Override
    public boolean back() {
        return false;
    }

    @Override
    public void update() {

    }

    public static FragmentDrawer newInstance() {

        Bundle args = new Bundle();

        FragmentDrawer fragment = new FragmentDrawer();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if (view == settingsRelativeLayout) {
            activity().setFragment(FragmentSettings.newInstance(), false);

        }
    }
}
