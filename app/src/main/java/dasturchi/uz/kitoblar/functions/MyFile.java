package dasturchi.uz.kitoblar.functions;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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


}
