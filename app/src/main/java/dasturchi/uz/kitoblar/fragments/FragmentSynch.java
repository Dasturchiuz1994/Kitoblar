package dasturchi.uz.kitoblar.fragments;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dasturchi.uz.kitoblar.R;
import dasturchi.uz.kitoblar.database.MyDatabase;
import dasturchi.uz.kitoblar.functions.Internet;
import dasturchi.uz.kitoblar.functions.MyFile;
import dasturchi.uz.kitoblar.objects.Book;
import dasturchi.uz.kitoblar.objects.Constants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by Ahmadjon on 15.01.2017.
 */
public class FragmentSynch extends MyFragment {

    RecyclerView recyclerView;
    ArrayList<Book> books;
    CustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = "Sizda yo`q kitoblar";
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

    }

    public static FragmentSynch newInstance() {

        Bundle args = new Bundle();

        FragmentSynch fragment = new FragmentSynch();
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

            holder.downloadImageView.setTag(position);
            holder.downloadImageView.setOnClickListener(this);
        }

        @Override
        public int getItemCount() {
            return books.size();
        }

        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();

            new DownloadBook(books.get(position)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

        Map<String, String> map;

        @Override
        protected void onPreExecute() {
            try {
                map = new HashMap<>();
                MyDatabase database = new MyDatabase(activity());
                ArrayList<Book> helper = database.getBooks();
                String s = "";

                for (int i = 0; i < helper.size(); i++)
                    s += helper.get(i).getId() + (i != helper.size() - 1 ? "_" : "");

                map.put("book_ids", s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {

                Response response = Internet.post(Constants.HOST + "/api/get_books.php", map);

                String body = response.body().string();

                JSONObject json = new JSONObject(body);

                if (json.getString("action").equals("ok")) {
                    JSONArray array = json.getJSONArray("data");

                    for (int i = 0; i < array.length(); i++)
                        books.add(new Book(array.getJSONObject(i)));
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


    class DownloadBook extends AsyncTask {
        int last = -1;
        int current = 0;
        Book book;
        Dialog dialog;
        TextView progressTextView;

        ArrayList <byte[]> arrayList = new ArrayList<>();

        boolean mustCancel = false;


        public DownloadBook(Book b) {
            book = b;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                dialog = new Dialog(activity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);
                dialog.setContentView(R.layout.dialog_download_book);
//                dialog.setCancelable(false);

                TextView nameTextView = (TextView) dialog.findViewById(R.id.text_view_name);
                nameTextView.setText(book.getName());

                progressTextView = (TextView) dialog.findViewById(R.id.text_view_progress);

                Button cancelButton = (Button) dialog.findViewById(R.id.button_cancel);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        DownloadBook.this.cancel(true);
                        mustCancel = true;
                    }
                });

                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {

                for (int i = 1; i <= book.getPagesCount(); i++) {
                    try {
                        if(mustCancel)
                            return null;

                        Response response = Internet.get(book.getContent() + "/" + i + ".html");
                        System.out.println("URL : " + book.getContent() + "/" + i + ".html");

                        byte[] bytes = response.body().bytes();

                        arrayList.add(bytes);

                        current++;
                        publishProgress();
                    } catch (Exception e) {
                        i--;
                        e.printStackTrace();
                    }
                }

                //if success
                if(arrayList.size() == book.getPagesCount()) {
                    MyFile.addBook(activity(), book, arrayList);

                    new MyDatabase(activity()).addBook(book);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            try {
                progressTextView.setText((int) (100. * current / book.getPagesCount()) + " %");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}