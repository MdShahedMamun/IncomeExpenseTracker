package app.com.example.shahed.incomeexpensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import app.com.example.shahed.incomeexpensetracker.IncomeExpenseContract.DateEntry;
import app.com.example.shahed.incomeexpensetracker.IncomeExpenseContract.ItemDateEntry;
import app.com.example.shahed.incomeexpensetracker.IncomeExpenseContract.ItemEntry;
import app.com.example.shahed.incomeexpensetracker.IncomeExpenseContract.MainEntry;
import app.com.example.shahed.incomeexpensetracker.IncomeExpenseContract.PersonEntry;


/**
 * Created by shahed on 27-Jun-16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "M4.db";
    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;
    private final String LOG_TAG = DatabaseHelper.class.getSimpleName();
    ArrayList<String> dateArrayList;
    ArrayList<Float> amountArrayList;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dateArrayList = new ArrayList<>();
        amountArrayList = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations. A location consists of the string supplied in
        // the location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_TABLE_ITEM = "CREATE TABLE " + ItemEntry.TABLE_ITEM + " (" +
                ItemEntry.COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemEntry.COLUMN_ITEM_NAME + " TEXT);";         //column name er pore space diye then type dite hobe (marksTEXT)

        final String SQL_CREATE_TABLE_DATE = "CREATE TABLE " + DateEntry.TABLE_DATE + " (" +
                DateEntry.COLUMN_DATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DateEntry.COLUMN_DATE_NAME + " TEXT);";

        final String SQL_CREATE_TABLE_ITEM_DATE = "CREATE TABLE " + ItemDateEntry.TABLE_ITEM_DATE + " (" +
                ItemDateEntry.COLUMN_ITEM_DATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemDateEntry.COLUMN_ITEM_ID + " INTEGER NOT NULL, " +
                ItemDateEntry.COLUMN_DATE_ID + " INTEGER NOT NULL, " +

                "FOREIGN KEY (" + ItemDateEntry.COLUMN_ITEM_ID + ") REFERENCES " +
                ItemEntry.TABLE_ITEM + " (" + ItemEntry.COLUMN_ITEM_ID + "), " +

                "FOREIGN KEY (" + ItemDateEntry.COLUMN_DATE_ID + ") REFERENCES " +
                DateEntry.TABLE_DATE + " (" + DateEntry.COLUMN_DATE_ID + "), " +

                "UNIQUE (" + ItemDateEntry.COLUMN_ITEM_ID + ", " +
                ItemDateEntry.COLUMN_DATE_ID + ") ON CONFLICT IGNORE);";

        final String SQL_CREATE_TABLE_PERSON = "CREATE TABLE " + PersonEntry.TABLE_PERSON + " (" +
                PersonEntry.COLUMN_PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PersonEntry.COLUMN_EMAIL + " TEXT, " +
                PersonEntry.COLUMN_PASSWORD + " TEXT);";

        final String SQL_CREATE_TABLE_MAIN = "CREATE TABLE " + MainEntry.TABLE_MAIN + " (" +
                MainEntry.COLUMN_MAIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MainEntry.COLUMN_ITEM_DATE_ID + " INTEGER NOT NULL, " +
                MainEntry.COLUMN_PERSON_ID + " INTEGER NOT NULL, " +
                MainEntry.COLUMN_AMOUNT + " REAL NOT NULL, " +
                MainEntry.COLUMN_INCOME_EXPENSE_TYPE + " INTEGER NOT NULL, " +

                "FOREIGN KEY (" + MainEntry.COLUMN_PERSON_ID + ") REFERENCES " +
                PersonEntry.TABLE_PERSON + " (" + PersonEntry.COLUMN_PERSON_ID + "), " +

                "FOREIGN KEY (" + MainEntry.COLUMN_ITEM_DATE_ID + ") REFERENCES " +
                ItemDateEntry.TABLE_ITEM_DATE + " (" + ItemDateEntry.COLUMN_ITEM_DATE_ID + "));";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ITEM);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_DATE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ITEM_DATE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_PERSON);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MAIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("drop table if exists " + ItemEntry.TABLE_ITEM);
        sqLiteDatabase.execSQL("drop table if exists " + DateEntry.TABLE_DATE);
        sqLiteDatabase.execSQL("drop table if exists " + ItemDateEntry.TABLE_ITEM_DATE);
        sqLiteDatabase.execSQL("drop table if exists " + PersonEntry.TABLE_PERSON);
        sqLiteDatabase.execSQL("drop table if exists " + MainEntry.TABLE_MAIN);

        onCreate(sqLiteDatabase);
    }

    // a general query of where clause (conditional query)
    public Cursor queryExecute(String tableName, String[] retrievingColumns, String selectionColumns, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.query(tableName,
                retrievingColumns,
                selectionColumns,
                selectionArgs,
                null,
                null,
                null,
                null);
    }

    public Cursor checkEmail(String email) {
        return queryExecute(PersonEntry.TABLE_PERSON,
                new String[]{PersonEntry.COLUMN_PERSON_ID},
                PersonEntry.COLUMN_EMAIL + "=?",
                new String[]{email});
    }

    public boolean validateLogin(String email, String password) {
        Cursor cursor = queryExecute(PersonEntry.TABLE_PERSON,
                new String[]{PersonEntry.COLUMN_PERSON_ID},
                PersonEntry.COLUMN_EMAIL + "=? and " + PersonEntry.COLUMN_PASSWORD + "=?",
                new String[]{email, password});

        return cursor.getCount() != 0;
    }

    public boolean validateItemName(String itemName, String date) {
        String item_id = "", date_id = "";
        Cursor cursor = queryExecute(ItemEntry.TABLE_ITEM,
                new String[]{ItemEntry.COLUMN_ITEM_ID},
                ItemEntry.COLUMN_ITEM_NAME + "=?",
                new String[]{itemName});
        if (cursor.moveToNext()) {
            item_id = cursor.getString(0);
        } else
            return true;

        cursor = queryExecute(DateEntry.TABLE_DATE,
                new String[]{DateEntry.COLUMN_DATE_ID},
                DateEntry.COLUMN_DATE_NAME + "=?",
                new String[]{date});
        if (cursor.moveToNext()) {
            date_id = cursor.getString(0);
        } else
            return true;

        cursor = queryExecute(ItemDateEntry.TABLE_ITEM_DATE,
                new String[]{ItemDateEntry.COLUMN_ITEM_DATE_ID},
                ItemDateEntry.COLUMN_ITEM_ID + "=? and " + ItemDateEntry.COLUMN_DATE_ID + "=?",
                new String[]{item_id, date_id});
        if (cursor.moveToNext()) {
            return false;
        } else
            return true;
    }

    public void insertEmailPassword(String email, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonEntry.COLUMN_EMAIL, email);
        contentValues.put(PersonEntry.COLUMN_PASSWORD, password);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long row = sqLiteDatabase.insert(PersonEntry.TABLE_PERSON, null, contentValues);
        Log.v(LOG_TAG, "inserted row number: " + row);
    }

    public void insertData(String itemName, String date, Float amount, int incomeExpenseType) {

        int person_id = 0, item_id = 0, date_id = 0, item_date_id = 0;
        Cursor cursor;

        Log.v(LOG_TAG, "see IncomeExpenseContract.email: " + IncomeExpenseContract.email);

        //retrieve item_id if itemName found in itemTable otherwise insert the itemName and then retrieve the item_id
        cursor = queryExecute(ItemEntry.TABLE_ITEM,
                new String[]{ItemEntry.COLUMN_ITEM_ID},
                ItemEntry.COLUMN_ITEM_NAME + "=?",
                new String[]{itemName});
//        Log.v(LOG_TAG, "deptName: " + deptName
//                + " table: " + DeptEntry.TABLE_DEPT
//                + "COLUMN_DEPT_ID: " + DeptEntry.COLUMN_DEPT_ID
//                + "COLUMN_DEPT_NAME: " + DeptEntry.COLUMN_DEPT_NAME);

//        Log.v(LOG_TAG, "values of getCount(): " + cursor.getCount());

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext())
                item_id = cursor.getInt(0);
            Log.v(LOG_TAG, "dept_id: " + item_id);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ItemEntry.COLUMN_ITEM_NAME, itemName);
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            long itemRow = sqLiteDatabase.insert(ItemEntry.TABLE_ITEM, null, contentValues);
            Log.v(LOG_TAG, "inserted row number: " + itemRow);

            cursor = queryExecute(ItemEntry.TABLE_ITEM,
                    new String[]{ItemEntry.COLUMN_ITEM_ID},
                    ItemEntry.COLUMN_ITEM_NAME + "=?",
                    new String[]{itemName});
            while (cursor.moveToNext())
                item_id = cursor.getInt(0);
        }

        //retrieve date_id if dateName found in dateTable otherwise
        // insert the dateName and then retrieve the date_id
        cursor = queryExecute(DateEntry.TABLE_DATE,
                new String[]{DateEntry.COLUMN_DATE_ID},
                DateEntry.COLUMN_DATE_NAME + "=?",
                new String[]{date});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext())
                date_id = cursor.getInt(0);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DateEntry.COLUMN_DATE_NAME, date);
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            long sem = sqLiteDatabase.insert(DateEntry.TABLE_DATE, null, contentValues);
            Log.v(LOG_TAG, "inserted row number: " + sem);


            cursor = queryExecute(DateEntry.TABLE_DATE,
                    new String[]{DateEntry.COLUMN_DATE_ID},
                    DateEntry.COLUMN_DATE_NAME + "=?",
                    new String[]{date});
            while (cursor.moveToNext())
                date_id = cursor.getInt(0);
        }


        //retrieve item_date_id if item_id and date_id found in item_date table otherwise insert
        // the item_id and date_id and then retrieve the item_date
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursorResult = sqLiteDatabase.query(ItemDateEntry.TABLE_ITEM_DATE,
                new String[]{ItemDateEntry.COLUMN_ITEM_DATE_ID},
                ItemDateEntry.COLUMN_ITEM_ID + "=? and " + ItemDateEntry.COLUMN_DATE_ID + "=?",
                new String[]{Integer.toString(item_id), Integer.toString(date_id)},
                null,
                null,
                null,
                null);

        if (cursorResult.getCount() != 0) {
            while (cursorResult.moveToNext())
                item_date_id = cursorResult.getInt(0);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ItemDateEntry.COLUMN_ITEM_ID, item_id);
            contentValues.put(ItemDateEntry.COLUMN_DATE_ID, date_id);

            sqLiteDatabase.insert(ItemDateEntry.TABLE_ITEM_DATE, null, contentValues);

            cursorResult = sqLiteDatabase.query(ItemDateEntry.TABLE_ITEM_DATE,
                    new String[]{ItemDateEntry.COLUMN_ITEM_DATE_ID},
                    ItemDateEntry.COLUMN_ITEM_ID + "=? and " + ItemDateEntry.COLUMN_DATE_ID + "=?",
                    new String[]{Integer.toString(item_id), Integer.toString(date_id)},
                    null,
                    null,
                    null,
                    null);
            while (cursorResult.moveToNext())
                item_date_id = cursorResult.getInt(0);
        }

        //retrieve person_id if email found in personTable otherwise
        // insert the email and then retrieve the person_id
        cursor = queryExecute(PersonEntry.TABLE_PERSON,
                new String[]{PersonEntry.COLUMN_PERSON_ID},
                PersonEntry.COLUMN_EMAIL + "=?",
                new String[]{IncomeExpenseContract.email});
        if (cursor.getCount() != 0) {
            if (cursor.moveToNext())
                person_id = cursor.getInt(0);
        } else {
            Log.v(LOG_TAG, "seeing person_table, there is no person_id for current email");
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MainEntry.COLUMN_ITEM_DATE_ID, item_date_id);
        contentValues.put(MainEntry.COLUMN_PERSON_ID, person_id);
        contentValues.put(MainEntry.COLUMN_AMOUNT, amount);
        contentValues.put(MainEntry.COLUMN_INCOME_EXPENSE_TYPE, incomeExpenseType);

        long main = sqLiteDatabase.insert(MainEntry.TABLE_MAIN, null, contentValues);
        Log.v(LOG_TAG, "in main table inserted row number: " + main);
        //this insert() method  return the row id of the newly inserted row

    }


    public Cursor getItemData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select*from " + ItemEntry.TABLE_ITEM, null);
    }

    public Cursor getDateData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select*from " + DateEntry.TABLE_DATE, null);
    }

    public Cursor getItemDateData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select*from " + ItemDateEntry.TABLE_ITEM_DATE, null);
    }

    public Cursor getPersonData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select*from " + PersonEntry.TABLE_PERSON, null);
    }

    public Cursor getMainData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select*from " + MainEntry.TABLE_MAIN, null);
    }

    // return a arrayList object of amount within specific range of date
    public ArrayList<Float> getAmountArrayList(String fromDate, String toDate, int incomeExpenseType) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int fromItemDateId = 0, toItemDateId = 0;

        amountArrayList.clear();
        dateArrayList.clear();

        // by joining date and item_date table retrieve item_date_id related to fromDate
        Cursor cursor = sqLiteDatabase.rawQuery("select " + ItemDateEntry.COLUMN_ITEM_DATE_ID +
                " from " + ItemDateEntry.TABLE_ITEM_DATE + " INNER JOIN " + DateEntry.TABLE_DATE +
                " ON " + DateEntry.TABLE_DATE + "." + DateEntry.COLUMN_DATE_ID + " = " +
                ItemDateEntry.TABLE_ITEM_DATE + "." + ItemDateEntry.COLUMN_DATE_ID +
                " and " + DateEntry.COLUMN_DATE_NAME + " =?"
                , new String[]{fromDate});
        if (cursor.moveToFirst()) {//choose first from multiple retrieved row
            fromItemDateId = cursor.getInt(0);
            Log.v(LOG_TAG, "after joining query fromItemDateId:" + fromItemDateId);
        }

        // by joining date and item_date table retrieve item_date_id related to toDate
        cursor = sqLiteDatabase.rawQuery("select " + ItemDateEntry.COLUMN_ITEM_DATE_ID +
                " from " + ItemDateEntry.TABLE_ITEM_DATE + " INNER JOIN " + DateEntry.TABLE_DATE +
                " ON " + DateEntry.TABLE_DATE + "." + DateEntry.COLUMN_DATE_ID + " = " +
                ItemDateEntry.TABLE_ITEM_DATE + "." + ItemDateEntry.COLUMN_DATE_ID +
                " and " + DateEntry.COLUMN_DATE_NAME + " =?"
                , new String[]{toDate});
        if (cursor.moveToLast()) { //choose last from multiple retrieved row
            toItemDateId = cursor.getInt(0);
            Log.v(LOG_TAG, "after joining query toItemDateId:" + toItemDateId);
        }

        //retrieve person_id if email found in personTable otherwise
        // insert the email and then retrieve the person_id
        int person_id = 0;
        cursor = queryExecute(PersonEntry.TABLE_PERSON,
                new String[]{PersonEntry.COLUMN_PERSON_ID},
                PersonEntry.COLUMN_EMAIL + "=?",
                new String[]{IncomeExpenseContract.email});
        if (cursor.getCount() != 0) {
            if (cursor.moveToNext())
                person_id = cursor.getInt(0);
        } else {
            Log.v(LOG_TAG, "seeing in person_table, there is no person_id for current email");
        }

        //retrieve amount and date from fromItemDateId to toItemDateId
        //item_date_id like date come in increasing order
        final String SQL_STATEMENT = "select " + MainEntry.COLUMN_ITEM_DATE_ID + "," + MainEntry.COLUMN_AMOUNT +
                " from " + MainEntry.TABLE_MAIN +
                " where " + MainEntry.COLUMN_ITEM_DATE_ID + ">=" + fromItemDateId +
                " and " + MainEntry.COLUMN_ITEM_DATE_ID + "<=" + toItemDateId +
                " and " + MainEntry.COLUMN_PERSON_ID + "=?" +
                " and " + MainEntry.COLUMN_INCOME_EXPENSE_TYPE + "=?";
        cursor = sqLiteDatabase.rawQuery(SQL_STATEMENT, new String[]{Integer.toString(person_id),
                Integer.toString(incomeExpenseType)});
        while (cursor.moveToNext()) {
            String current_date = "", current_item = "", concatenation_item_date = "";
            int current_item_date_id = cursor.getInt(0);
            Float current_amount = cursor.getFloat(1);
            amountArrayList.add(current_amount);
            Log.v(LOG_TAG, "current_amount:" + current_amount);

            SQLiteDatabase sqLiteDatabase1 = this.getWritableDatabase();
            Cursor insideCursor = sqLiteDatabase1.rawQuery("select " + DateEntry.COLUMN_DATE_NAME +
                    " from " + DateEntry.TABLE_DATE + " INNER JOIN " + ItemDateEntry.TABLE_ITEM_DATE +
                    " ON " + DateEntry.TABLE_DATE + "." + DateEntry.COLUMN_DATE_ID + " = " +
                    ItemDateEntry.TABLE_ITEM_DATE + "." + ItemDateEntry.COLUMN_DATE_ID +
                    " and " + ItemDateEntry.COLUMN_ITEM_DATE_ID + " =?"
                    , new String[]{Integer.toString(current_item_date_id)});
            while (insideCursor.moveToNext()) {
                current_date = insideCursor.getString(0);
                Log.v(LOG_TAG, "current_date:" + current_date);
            }

            insideCursor = sqLiteDatabase1.rawQuery("select " + ItemEntry.COLUMN_ITEM_NAME +
                    " from " + ItemEntry.TABLE_ITEM + " INNER JOIN " + ItemDateEntry.TABLE_ITEM_DATE +
                    " ON " + ItemEntry.TABLE_ITEM + "." + ItemEntry.COLUMN_ITEM_ID + " = " +
                    ItemDateEntry.TABLE_ITEM_DATE + "." + ItemDateEntry.COLUMN_ITEM_ID +
                    " and " + ItemDateEntry.COLUMN_ITEM_DATE_ID + " =?"
                    , new String[]{Integer.toString(current_item_date_id)});
            while (insideCursor.moveToNext()) {
                current_item = insideCursor.getString(0);
                Log.v(LOG_TAG, "current_date:" + current_item);
            }

            concatenation_item_date = current_item + "," + current_date; // "date,item" string concatenation
            dateArrayList.add(concatenation_item_date);
        }
        return amountArrayList;
    }

    public ArrayList<String> getDateArrayList() {
        return dateArrayList;
    }
}
