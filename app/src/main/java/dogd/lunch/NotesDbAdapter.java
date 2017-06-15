package dogd.lunch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 2017-05-22.
 */

public class NotesDbAdapter {

    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY1 = "body1";
    public static final String KEY_BODY2 = "body2";
    public static final String KEY_BODY3 = "body3";
    public static final String KEY_ROWID = "_id";
    private static final String TAG = "NotesDbAdapter";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /*
    * Database creation sql statement
     */

    private static final String DATABASE_CREATE = "create table notes "
            + "(_id integer primary key autoincrement, "
            + "title text not null, body1 text, body2 text, body3 text);";

    private static final String DATABASE_NAME = "date";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 2;
    private final Context mCtx;


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which dwill destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "Downgrade database from version ");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    public NotesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void createNote(String title, String body1, String body2, String body3) {
        mDb.execSQL("INSERT INTO " + DATABASE_TABLE + " (" + KEY_TITLE + ", "
            + KEY_BODY1 + ", " + KEY_BODY2 + ", " + KEY_BODY3 + ") VALUES ('"
            + title + "', '" + body1 +  "', '" + body2 + "', '" + body3 + "');");
    }

    public boolean deleteNote(long rowId) {
        Log.i("Delete called", "value__" + rowId);
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public void allDelNote() {
        Log.d("delete called", "성공!");
        mDb.execSQL("Delete from " + DATABASE_TABLE);
    }

    public Cursor fetchAllNotes() {
        return mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
    }

    public Cursor fetchNote(long rowId) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_BODY1, KEY_BODY2, KEY_BODY3 },
                KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateNote(long rowId, String title, String body1, String body2, String body3) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY1, body1);
        args.put(KEY_BODY2, body2);
        args.put(KEY_BODY3, body3);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
