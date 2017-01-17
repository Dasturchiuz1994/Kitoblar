package dasturchi.uz.kitoblar.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import dasturchi.uz.kitoblar.activities.MainActivity;

/**
 * Created by Ahmadjon on 08.01.2017.
 */
public abstract class MyFragment extends Fragment {

    String title = "";

    Bundle bundle;

    View parentView;

    public abstract boolean back();

    public abstract void update();

    MainActivity activity() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            activity().setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (activity().fragmentStack.isEmpty()) {
                activity().fragmentActionBar.hideBackImageView();
                activity().drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                activity().fragmentActionBar.showBackImageView();
                activity().drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        bundle = getArguments();
    }



    public void hide() {
        parentView.setVisibility(View.INVISIBLE);
    }

    public void show() {
        parentView.setVisibility(View.VISIBLE);
    }
}
