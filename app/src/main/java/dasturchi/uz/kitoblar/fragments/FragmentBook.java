package dasturchi.uz.kitoblar.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.github.barteksc.pdfviewer.PDFView;

import dasturchi.uz.kitoblar.R;
import dasturchi.uz.kitoblar.objects.Book;

/**
 * Created by Ahmadjon on 10.01.2017.
 */
public class FragmentBook extends MyFragment {

    public static String BOOK = "book";

    Book book;


    ViewPager viewPager;
    CustomAdapter adapter;

    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        book = (Book) getArguments().getSerializable(BOOK);

        title = book.getName();
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_book, null, false);

        adapter = new CustomAdapter(activity().getSupportFragmentManager());

        viewPager = (ViewPager)parentView.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText((position + 1) + "/" + book.getPagesCount() );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        textView = (TextView)parentView.findViewById(R.id.text_view);
        textView.setText(1 + "/" + book.getPagesCount() );

        return parentView;
    }

    @Override
    public boolean back() {
        return false;
    }

    @Override
    public void update() {

    }

    public static FragmentBook newInstance(Bundle args) {

        FragmentBook fragment = new FragmentBook();
        fragment.setArguments(args);
        return fragment;
    }

    class CustomAdapter extends FragmentStatePagerAdapter{

        public CustomAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putSerializable(FragmentPage.BOOK , book);
            args.putInt(FragmentPage.PAGE , position);

            return FragmentPage.newInstance(args);
        }

        @Override
        public int getCount() {
            return book.getPagesCount();
        }
    }
}
