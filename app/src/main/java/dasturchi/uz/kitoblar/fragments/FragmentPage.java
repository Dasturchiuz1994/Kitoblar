package dasturchi.uz.kitoblar.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import dasturchi.uz.kitoblar.R;
import dasturchi.uz.kitoblar.database.MyDatabase;
import dasturchi.uz.kitoblar.functions.MyFile;
import dasturchi.uz.kitoblar.objects.Book;

/**
 * Created by Ahmadjon on 17.01.17.
 */

public class FragmentPage extends Fragment{

    public static String BOOK = "book";
    public static String PAGE = "page";

    Book book;
    int page = 0;
    int shrift;

    WebView webView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        book = (Book)getArguments().getSerializable(BOOK);
        page = getArguments().getInt(PAGE);

        shrift = new MyDatabase(getActivity()).getShrift();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page , container , false);

        String content = MyFile.getPage(getActivity() , book , page);

        WebView webView = (WebView)v.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

//        new GetContent().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        webView.loadData(beautify(content), "text/html; charset=utf-8", "utf-8");

        return v;
    }


    private String beautify(String content) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html>");
        buffer.append("<head>");
        buffer.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        buffer.append("<style>");
        buffer.append("* { margin: 0; padding: 0; }");
        buffer.append("img { width: 100%; height: auto; }");
        buffer.append("p { font-size: " + shrift + "pt; text-indent:20px; padding-left: 10px; padding-right: 10px; padding-bottom: 10px; padding-top: 10px;}");
        buffer.append("</style>");
        buffer.append("</head>");
        buffer.append("<body>");
        buffer.append(content);
        buffer.append("</body>");
        buffer.append("</html>");
        return buffer.toString();
    }


    public static FragmentPage newInstance(Bundle args) {

        FragmentPage fragment = new FragmentPage();
        fragment.setArguments(args);
        return fragment;
    }

    class GetContent extends AsyncTask{

        String content ;

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                content = MyFile.getPage(getActivity() , book , page);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try{
                webView.loadData(beautify(content), "text/html; charset=utf-8", "utf-8");
            }catch (Exception e){
                e.printStackTrace();
            }
            super.onPostExecute(o);
        }
    }
}
