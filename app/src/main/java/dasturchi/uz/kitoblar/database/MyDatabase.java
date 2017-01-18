package dasturchi.uz.kitoblar.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONObject;

import java.util.ArrayList;

import dasturchi.uz.kitoblar.objects.Book;

/**
 * Created by Ahmadjon on 15.01.2017.
 */
public class MyDatabase extends SQLiteOpenHelper {

    static SQLiteDatabase database;

    static String TABLE_BOOKS = "books";
    static String TABLE_SHRIFT = "shrift";
    static String COLUMN_SHRIFT = "column_shrift";

    public MyDatabase(Context context) {
        super(context, "kitoblar", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            String sql = "create table " + TABLE_BOOKS + " (" +
                    Book.ID + " integer primary key ," +
                    Book.CONTENT + " text ," +
                    Book.IMAGE + " text ," +
                    Book.NAME + " text ," +
                    Book.PAGES_COUNT + " integer " +
                    ");";

            database.execSQL(sql);

            sql = "create table " + TABLE_SHRIFT + " (" +
                    COLUMN_SHRIFT + " integer " +

                    ");";

            database.execSQL(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public ArrayList<Book> getBooks() {
        ArrayList<Book> books = new ArrayList<>();

        try {
            database = getWritableDatabase();
            synchronized (database) {

                Cursor cursor = database.query(TABLE_BOOKS, null, null, null, null, null, null);

                if (cursor.moveToFirst())
                    do {
                        books.add(new Book(cursorToJson(cursor)));
                    } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }

        return books;
    }


    public void addBook(Book book) {
        try {
            database = getWritableDatabase();
            synchronized (database) {

                Cursor cursor = database.rawQuery("select * from " + TABLE_BOOKS + " where " + Book.ID + " = " + book.getId(), null);

                if (cursor.moveToFirst())
                    database.execSQL("delete from " + TABLE_BOOKS + " where " + Book.ID + " = " + book.getId());

                database.insert(TABLE_BOOKS, null, book.getContentValues());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }
    }

    public int getShrift() {

        try {
            database = getWritableDatabase();
            synchronized (database) {

                Cursor cursor = database.query(TABLE_SHRIFT, null, null, null, null, null, null);

                if (cursor.moveToFirst())
                    return cursor.getInt(cursor.getColumnIndex(COLUMN_SHRIFT));

            }
        } catch (Exception e) {
            setShrift(20);
            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }

        return 20;
    }

    public void setShrift(int shrift) {

        try {
            database = getWritableDatabase();
            synchronized (database) {

                database.execSQL("delete from " + TABLE_SHRIFT);
                database.execSQL("insert into " + TABLE_SHRIFT + " values (" + shrift + ")");

            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }
    }


    JSONObject cursorToJson(Cursor cursor) {
        try {
            String names[] = cursor.getColumnNames();
            int indexes[] = new int[names.length];

            for (int i = 0; i < names.length; i++)
                indexes[i] = cursor.getColumnIndex(names[i]);

            JSONObject jsonObject = new JSONObject();

            for (int i = 0; i < names.length; i++) {
                if (cursor.getType(indexes[i]) == Cursor.FIELD_TYPE_INTEGER)
                    jsonObject.put(names[i], cursor.getInt(indexes[i]));
                else if (cursor.getType(indexes[i]) == Cursor.FIELD_TYPE_STRING)
                    jsonObject.put(names[i], cursor.getString(indexes[i]));
            }

            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
