package com.example.madison.mapexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This is the Databasre Adapter Class. Provides the SQLite interface for creating, deleting, and retrieving
 * entries in tables.
 */

public class ClassDbAdapter {

    // Creating Name of DB and name of Table
    private static final String DATABASE_NAME = "CLASS_DATABASE.db";
    private static final String CLASS_TABLE = "CLASS_TABLE";
    private static final int DATABASE_VERSION = 200;
    private final Context Ctx;

    public static String TAG = ClassDbAdapter.class.getSimpleName();

    private DatabaseHelper DbHelper;
    SQLiteDatabase Db;

    // Need to define the static variables to save within the SQLite Table
    public static final String KEY_ROWID="_id";
    public static final String CLASSNAME = "classname";
    public static final String FREQ = "freq";

    public static final String COUNT = "count";
    public static final String CURRENTCOUNT = "currentcount";
    public static final String INTERVAL = "interval";
    public static final String CURRENTINTERVAL = "currentinterval";

    public static final String UNTIL = "until";
    public static final String DAYS = "days";
    public static final String LOCATION = "location";

    public static final String SU = "su";
    public static final String MO = "mo";
    public static final String TU = "tu";
    public static final String WE = "we";
    public static final String TH = "th";
    public static final String FR = "fr";
    public static final String SA = "sa";

    public static final String MISSED = "missed";
    public static final String ATTENDED = "attended";
    public static final String UNSURE = "unsure";

    public static final String STARTH = "starth";
    public static final String STARTM = "startm";
    public static final String ENDH = "endh";
    public static final String ENDM = "endm";

    public static final String NEXTDATE = "nextdate";


    // Specifying the Class Fields that makup the Class Table
    public static final String[] CONTACT_FIELDS = new String[]{
            KEY_ROWID,
            CLASSNAME,
            FREQ,
            COUNT,
            CURRENTCOUNT,
            INTERVAL,
            CURRENTINTERVAL,
            UNTIL,
            DAYS,
            LOCATION,
            SU,
            MO,
            TU,
            WE,
            TH,
            FR,
            SA,
            MISSED,
            ATTENDED,
            UNSURE,
            STARTH,
            STARTM,
            ENDH,
            ENDM,
            NEXTDATE
    };


    // Creating the Class Table entries (strings, integers, keys)
    private static final String CREATE_TABLE_CLASS =
            "create table " + CLASS_TABLE +"("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CLASSNAME + " text,"
                    + FREQ + " text,"
                    + COUNT + " text,"
                    + CURRENTCOUNT + " text,"
                    + INTERVAL + " text,"
                    + CURRENTINTERVAL + " text,"
                    + UNTIL + " text,"
                    + DAYS + " text,"
                    + LOCATION + " text,"
                    + SU + " INTEGER,"
                    + MO + " INTEGER,"
                    + TU + " INTEGER,"
                    + WE + " INTEGER,"
                    + TH + " INTEGER,"
                    + FR + " INTEGER,"
                    + SA + " INTEGER,"
                    + MISSED + " INTEGER,"
                    + ATTENDED + " INTEGER,"
                    + UNSURE + " INTEGER,"
                    + STARTH + " INTEGER,"
                    + STARTM + " INTEGER,"
                    + ENDH + " INTEGER,"
                    + ENDM + " INTEGER,"
                    + NEXTDATE + " INTEGER"
                    +");";


    // Provides the basic Database Helper functions that create and upgrade tables
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_CLASS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy old data");
            db.execSQL("DROP TABLE IF EXISTS" + CLASS_TABLE);
            onCreate(db);
        }
    }

    public ClassDbAdapter(Context ctx){
        this.Ctx = ctx;
    }

    // Ope functionaility for the Database
    public ClassDbAdapter open() throws SQLException {
        DbHelper = new DatabaseHelper(Ctx);
        Db = DbHelper.getWritableDatabase();
        return this;
    }

    // Close functionaility for the database
    public void close(){
        if(DbHelper!=null){
            DbHelper.close();
        }
    }

    // Upgrade functionality for the database
    public void upgrade() throws SQLException{
        DbHelper = new DatabaseHelper(Ctx);
        Db = DbHelper.getWritableDatabase();
        DbHelper.onUpgrade(Db, 1, 0);
    }

    // Adding a new class to the Table
    public long insertClass(ContentValues initialValues){
        return Db.insertWithOnConflict(CLASS_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    // Update a class in the Table
    public boolean updateClass(int id, ContentValues newValues){
        Log.d("ID", "" + id);
        String[] selectionArgs = {String.valueOf(id)};
        Log.d("ValueOfID", "" + selectionArgs);
        return Db.update(CLASS_TABLE, newValues, KEY_ROWID + "=?", selectionArgs) >0;
    }

    // Delete a class in the Table
    public boolean deleteClass(int id){
        String[] selectionArgs = {String.valueOf(id)};
        return Db.delete(CLASS_TABLE, KEY_ROWID + "=?", selectionArgs)>0;
    }

    // Get the cursor location to the Table sorted by next date of occurance, star hour, and start minute
    public Cursor getClassType(){
        // Table -- Columns -- Seletction -- String Selection Args -- Group By -- Having -- Order By
        return Db.query(CLASS_TABLE, CONTACT_FIELDS,  null, null, null, null,NEXTDATE+" ASC, " + STARTH + " ASC, " + STARTM + " ASC");
    }


    // Functionailty to search in the database, returns a corsor location to a Table
    public Cursor searchClass(String search){
        return Db.query(CLASS_TABLE, CONTACT_FIELDS,
                CLASSNAME + " like " + "'%" + search + "%'", null, null, null, null);
    }

    // Takes a cursor location and extracts the classType fields from the row
    public static classType getClassFromCursor(Cursor cursor){
        classType ct = new classType();
        ct.id = cursor.getInt(cursor.getColumnIndex(KEY_ROWID));
        ct.classname = cursor.getString(cursor.getColumnIndex(CLASSNAME));
        ct.freq = cursor.getString(cursor.getColumnIndex(FREQ));
        ct.count = cursor.getString(cursor.getColumnIndex(COUNT));
        ct.currentcount = cursor.getString(cursor.getColumnIndex(CURRENTCOUNT));
        ct.interval = (cursor.getString(cursor.getColumnIndex(INTERVAL)));
        ct.currentinterval = (cursor.getString(cursor.getColumnIndex(CURRENTINTERVAL)));
        ct.until = (cursor.getString(cursor.getColumnIndex(UNTIL)));
        ct.days = (cursor.getString(cursor.getColumnIndex(DAYS)));
        ct.location = (cursor.getString(cursor.getColumnIndex(LOCATION)));

        ct.su = (cursor.getInt(cursor.getColumnIndex(SU)));
        ct.mo = (cursor.getInt(cursor.getColumnIndex(MO)));
        ct.tu = (cursor.getInt(cursor.getColumnIndex(TU)));
        ct.we = (cursor.getInt(cursor.getColumnIndex(WE)));
        ct.th = (cursor.getInt(cursor.getColumnIndex(TH)));
        ct.fr = (cursor.getInt(cursor.getColumnIndex(FR)));
        ct.sa = (cursor.getInt(cursor.getColumnIndex(SA)));

        ct.missed = (cursor.getInt(cursor.getColumnIndex(MISSED)));
        ct.attended = (cursor.getInt(cursor.getColumnIndex(ATTENDED)));
        ct.unsure = (cursor.getInt(cursor.getColumnIndex(UNSURE)));

        ct.starth = (cursor.getInt(cursor.getColumnIndex(STARTH)));
        ct.startm = (cursor.getInt(cursor.getColumnIndex(STARTM)));
        ct.endh = (cursor.getInt(cursor.getColumnIndex(ENDH)));
        ct.endm = (cursor.getInt(cursor.getColumnIndex(ENDM)));

        ct.nextdate = (cursor.getInt(cursor.getColumnIndex(NEXTDATE)));

        return(ct);
    }
}
