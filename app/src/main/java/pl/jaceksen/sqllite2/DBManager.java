package pl.jaceksen.sqllite2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

/**
 * Created by jsen on 12.09.17.
 */

public class DBManager {


    private SQLiteDatabase sqlDB;
    static final String dbName="Students";
    static final String tableName="Logins";
    static final String colUserName="UserName";
    static final String colPassword="Password";
    static final String colId = "ID";
    static final int dbVersion=1;

    //create table
    static final String createTable="Create table IF NOT EXISTS " + tableName +
            "(ID integer PRIMARY KEY AUTOINCREMENT," + colUserName + " text," +
             colPassword + " text);";




    static class DBHelperUser extends SQLiteOpenHelper{

        Context context;

        DBHelperUser(Context context){
            super(context,dbName,null,dbVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(createTable);
            Toast.makeText(context,"Tabela utworzona",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("Drop table IF EXISTS " + tableName);
            onCreate(sqLiteDatabase);
        }
    }





    //konstruktor
    public DBManager(Context context){
        DBHelperUser db = new DBHelperUser(context);
        sqlDB = db.getWritableDatabase();
    }


    //insert data
    public long Insert(ContentValues values){
        long ID=sqlDB.insert(tableName,"",values);
        return ID;
    }


    //retrieve data wher id=1
    public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(tableName);

        Cursor cursor = qb.query(sqlDB,projection,selection,selectionArgs,null,null,sortOrder);
        return cursor;
    }


    //delete
    public int delete(String selection, String[] selectionArgs){
        int count = sqlDB.delete(tableName,selection,selectionArgs);
        return count;
    }

    //update
    public int update(ContentValues values, String selection, String[] selectionArgs){

        int count = sqlDB.update(tableName, values, selection, selectionArgs);
        return count;

    }


}
