package dasturchi.uz.kitoblar.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.github.barteksc.pdfviewer.PDFView;

import dasturchi.uz.kitoblar.R;
import dasturchi.uz.kitoblar.objects.Book;

/**
 * Created by Ahmadjon on 10.01.2017.
 */
public class FragmentBook extends MyFragment {

    public static String BOOK = "book";

    Book book;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = "";
        super.onCreate(savedInstanceState);

        book = (Book) bundle.getSerializable(BOOK);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_book, null, false);


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
}
