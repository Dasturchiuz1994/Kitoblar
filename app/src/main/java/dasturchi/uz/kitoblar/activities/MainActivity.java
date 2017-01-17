package dasturchi.uz.kitoblar.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import java.util.Stack;

import dasturchi.uz.kitoblar.R;
import dasturchi.uz.kitoblar.fragments.FragmentActionBar;
import dasturchi.uz.kitoblar.fragments.FragmentBookList;
import dasturchi.uz.kitoblar.fragments.FragmentDrawer;
import dasturchi.uz.kitoblar.fragments.MyFragment;

public class MainActivity extends Activity {

    public Stack<MyFragment> fragmentStack;

    FragmentManager fragmentManager;
    public FragmentActionBar fragmentActionBar;
    public MyFragment fragment;
    FragmentDrawer fragmentDrawer;

    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentStack = new Stack();

        fragmentManager = getFragmentManager();

        initFragmentActionBar();
        initFragmentDrawer();
        setFragment(FragmentBookList.newInstance(), false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void initFragmentDrawer() {
        fragmentDrawer = FragmentDrawer.newInstance();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_drawer_container, fragmentDrawer);
        transaction.commit();
    }

    private void initFragmentActionBar() {
        fragmentActionBar = FragmentActionBar.newInstance();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_action_bar_container, fragmentActionBar);

        transaction.commit();
    }

    public void setFragment(MyFragment fr, boolean withAnim) {
        if (this.fragment != null) {
            fragmentStack.push(this.fragment);
            this.fragment.hide();
        }

        this.fragment = fr;

        FragmentTransaction transaction = fragmentManager.beginTransaction();

//        if (withAnim)
//            transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        transaction.add(R.id.fragment_container, fragment);

        transaction.commit();
    }

    public void backFragment() {
        if (fragmentStack.isEmpty())
            finish();
        else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.remove(fragment);

            transaction.commit();

            fragment = fragmentStack.pop();
            fragment.update();
            fragment.show();

            if (fragmentStack.isEmpty()) {
                fragmentActionBar.hideBackImageView();
            }
        }
    }

    public void openDrawer(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    public void closeDrawer(){
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void isOpenDrawer(){
        drawerLayout.isDrawerOpen(Gravity.LEFT);
    }
}
