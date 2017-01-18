package dasturchi.uz.kitoblar.functions;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import dasturchi.uz.kitoblar.objects.Book;

/**
 * Created by Ahmadjon on 10.01.2017.
 */
public class MyFile {

    public static void addBook(Context context, Book book, ArrayList<byte[]> pages) {
        try {

            File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + book.getId());

            if (file.exists())
                file.delete();

            file.mkdirs();

            for (int i = 0; i < pages.size(); i++) {
                File f = new File(file.getAbsolutePath() + File.separator + i + ".html");

                FileOutputStream writer = new FileOutputStream(f);
                writer.write(pages.get(i));
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isHaveBook(Context context, Book book) {
        try {
            boolean ans = true;

            File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + book.getId());

            if (!file.exists())
                file.mkdirs();

            for (int i = 0; i < book.getPagesCount(); i++) {
                File f = new File(file.getAbsolutePath() + File.separator + i + ".html");

                ans = ans && f.exists();
            }

            return ans;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public static String getPage(Context context, Book book, int page) {
        try {

            File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + book.getId() + File.separator + page + ".html");

            if (file.exists()) {
                file.mkdirs();

                BufferedReader reader = new BufferedReader(new FileReader(file));

                StringBuffer sb = new StringBuffer();
                String s;

                while ((s = reader.readLine()) != null) {
                    sb.append(s);
                }

                return sb.toString();
            } else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
