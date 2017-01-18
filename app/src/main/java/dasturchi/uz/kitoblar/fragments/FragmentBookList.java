package dasturchi.uz.kitoblar.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import dasturchi.uz.kitoblar.R;
import dasturchi.uz.kitoblar.database.MyDatabase;
import dasturchi.uz.kitoblar.functions.Internet;
import dasturchi.uz.kitoblar.functions.MyFile;
import dasturchi.uz.kitoblar.objects.Book;
import dasturchi.uz.kitoblar.objects.Constants;
import okhttp3.Response;


/**
 * Created by Ahmadjon on 08.01.2017.
 */
public class FragmentBookList extends MyFragment {

    RecyclerView recyclerView;
    ArrayList<Book> books;
    CustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = "Kitoblar";
        super.onCreate(savedInstanceState);

        books = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_book_list, null, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity());

        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);


        new GetBooks().execute();
        return parentView;
    }

    @Override
    public boolean back() {
        return false;
    }

    @Override
    public void update() {
        books = new ArrayList<>();
        new GetBooks().execute();
    }

    public static FragmentBookList newInstance() {

        Bundle args = new Bundle();

        FragmentBookList fragment = new FragmentBookList();
        fragment.setArguments(args);
        return fragment;
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomHolder> implements View.OnClickListener {


        @Override
        public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(activity()).inflate(R.layout.item_recycler_view_fragment_book_list, parent, false);

            v.setOnClickListener(this);

            return new CustomHolder(v);
        }

        @Override
        public void onBindViewHolder(CustomHolder holder, int position) {

            holder.nameTextView.setText(books.get(position).getName());
            Picasso.with(activity()).load(books.get(position).getImage()).resize(dpToPx(100), dpToPx(100)).into(holder.imageView);
            holder.downloadImageView.setVisibility(View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return books.size();
        }

        @Override
        public void onClick(View view) {
            int position = recyclerView.getChildLayoutPosition(view);

            Bundle args = new Bundle();
            args.putSerializable(FragmentBook.BOOK , books.get(position));

            activity().setFragment(FragmentBook.newInstance(args) , false);
        }

        class CustomHolder extends RecyclerView.ViewHolder {

            ImageView downloadImageView;
            ImageView imageView;
            TextView nameTextView;

            public CustomHolder(View itemView) {
                super(itemView);

                imageView = (ImageView) itemView.findViewById(R.id.image_view);
                nameTextView = (TextView) itemView.findViewById(R.id.text_view);
                downloadImageView = (ImageView) itemView.findViewById(R.id.image_view_download);
            }
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    class GetBooks extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                MyDatabase database = new MyDatabase(activity());

                books.addAll(database.getBooks());

                for(int i = 0;i < books.size();i ++){
                    if(!MyFile.isHaveBook(activity() , books.get(i))){

//                        new MyDatabase(activity()).removeBook(books.get(i));

                        books.remove(i);

                        i --;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(o);
        }
    }
}
