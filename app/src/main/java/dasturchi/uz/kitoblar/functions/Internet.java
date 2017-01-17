package dasturchi.uz.kitoblar.functions;

import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ahmadjon on 04.01.2017.
 */
public class Internet {

    public static OkHttpClient client = new OkHttpClient();

    public static Response get(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            return client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Response post(String url, Map<String, String> map) {
        try {
            Set<String> keys = map.keySet();

            FormBody.Builder builder = new FormBody.Builder();

            for (String key : keys)
                builder.add(key, map.get(key));

            RequestBody formBody = builder.build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            return client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
