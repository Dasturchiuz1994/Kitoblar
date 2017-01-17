package dasturchi.uz.kitoblar.objects;

import android.content.ContentValues;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ahmadjon on 10.01.2017.
 */
public class Book implements Serializable {

    public static String ID = "id";
    public static String NAME = "name";
    public static String CONTENT = "content";
    public static String IMAGE = "image";
    public static String PAGES_COUNT = "pages_count";


    int id;
    String image;
    String content;
    String name;
    int pagesCount;

    public Book(JSONObject json) {
        try {
            if (json.has(ID))
                id = json.getInt(ID);
            if (json.has(NAME))
                name = json.getString(NAME);
            if (json.has(IMAGE))
                image = json.getString(IMAGE);
            if (json.has(CONTENT))
                content = json.getString(CONTENT);
            if (json.has(PAGES_COUNT))
                pagesCount = json.getInt(PAGES_COUNT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public ContentValues getContentValues(){
        ContentValues c = new ContentValues();

        c.put(ID , id);
        c.put(NAME , name);
        c.put(CONTENT , content);
        c.put(IMAGE , image);
        c.put(PAGES_COUNT , pagesCount);

        return c;
    }
}
