package dasturchi.uz.kitoblar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dasturchi.uz.kitoblar.R;
import dasturchi.uz.kitoblar.activities.MainActivity;

/**
 * Created by Ahmadjon on 28.12.2016.
 */
public class FragmentActionBar extends MyFragment implements View.OnClickListener {

    ImageView backImageView;
    ImageView drawerImageView;
    ImageView synchImageView;


    TextView titleTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_action_bar, null, false);

        drawerImageView = (ImageView) parentView.findViewById(R.id.drawer_image_view);
        drawerImageView.setOnClickListener(this);

        backImageView = (ImageView) parentView.findViewById(R.id.image_view_back);
        backImageView.setOnClickListener(this);

        synchImageView = (ImageView) parentView.findViewById(R.id.image_view_synch);
        synchImageView.setOnClickListener(this);

        titleTextView = (TextView) parentView.findViewById(R.id.text_view_title);

        return parentView;
    }

    @Override
    public boolean back() {
        return false;
    }

    public static FragmentActionBar newInstance() {

        Bundle args = new Bundle();

        FragmentActionBar fragment = new FragmentActionBar();
        fragment.setArguments(args);
        return fragment;
    }

    public void showBackImageView() {
        backImageView.setVisibility(View.VISIBLE);
        drawerImageView.setVisibility(View.INVISIBLE);
    }

    public void hideBackImageView() {
        backImageView.setVisibility(View.INVISIBLE);
        drawerImageView.setVisibility(View.VISIBLE);
    }

    public void setTitle(String s) {
        titleTextView.setText(s);
    }

    @Override
    public void update() {
        activity().setTitle(title);
    }

    @Override
    public void onClick(View view) {
        if (view == drawerImageView) {
            if (((MainActivity) getActivity()).drawerLayout.isDrawerOpen(Gravity.LEFT))
                ((MainActivity) getActivity()).drawerLayout.closeDrawer(Gravity.LEFT);
            else
                ((MainActivity) getActivity()).drawerLayout.openDrawer(Gravity.LEFT);
        } else if (view == backImageView) {
            activity().backFragment();
        } else if (view == synchImageView) {
            activity().setFragment(FragmentSynch.newInstance(), false);
        }
    }

}