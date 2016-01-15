package helper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by HarmeetSingh on 12/4/2015.
 */
public class Db1 extends SQLiteOpenHelper {
    public static String DB_PATH = "/data/data/com.example.harmeetsingh.bluescanner/databases/";
    public static String DB_NAME="yourdb.sqlite31";
    public static final int DB_VERSION = 1;
    private String t;

    public static final String USER= "devices";

    private SQLiteDatabase myDB;
    private Context context;

    public Db1(Context context){
        super(context, DB_NAME,null, DB_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close(){
        if(myDB!=null){
            myDB.close();
        }
        super.close();
    }
    public String getAllUser(){
        String listUsers = null ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        //String[] t1 = {t};
        try{
            c = db.rawQuery("select * from " + USER ,null);
           // Log.d("Intent","I"+ Arrays.toString(t1));
            if(c == null)return null;
            c.moveToFirst();
            listUsers = c.getString(1);

            c.close();
        }catch(Exception e)
        { Log.e("Error",e.getMessage());
        }
        db.close();
        return listUsers;
    }
    public void openDatabase() throws SQLException{
        String myPath = DB_PATH + DB_NAME;
        myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
    public void copyDatabase() throws IOException{
        try{
            InputStream input = context.getAssets().open(DB_NAME);
            String output = DB_PATH+DB_NAME;
            Log.d("input","M"+input+output);
            OutputStream mOutput = new FileOutputStream(output);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer))>0){
                mOutput.write(buffer,0,length);
            }
            mOutput.flush();
            mOutput.close();
            input.close();
        }catch(Exception e){
            Log.e("CopyDatabase",e.getMessage());
        }
    }
    public void createDataBase(String x) throws IOException{
        t=x;
        boolean dbExist = checkDatabase();
        if(dbExist){
        }else{
            this.getReadableDatabase();
            try{
                copyDatabase();}
            catch(IOException e){
                Log.e("Create", e.getMessage());
            }
        }
    }
    public boolean checkDatabase(){
        SQLiteDatabase temp = null;
        try{
            String myPath = DB_NAME + DB_PATH;
            Log.d("OutPut","L"+myPath);
            temp = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);
        }catch(SQLException e){
            Log.e("check",e.getMessage());
        }
        if(temp != null)
            temp.close();
        return temp != null ? true : false;
    }
}
