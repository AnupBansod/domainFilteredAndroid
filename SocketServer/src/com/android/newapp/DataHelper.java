package com.android.newapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
 
import java.util.ArrayList;
import java.util.List;
 
public class DataHelper {
 
   private static final String DATABASE_NAME = "domain_list";
   private static final int DATABASE_VERSION = 3;
   private static final String TABLE_NAME = "domain_allow_deny";
 
   private Context context;
   private SQLiteDatabase db;
 
   private SQLiteStatement insertStmt;
   private static final String INSERT = "insert into "
      + TABLE_NAME + "(dname,app,allow) values (?,?,?)";
 
   public DataHelper(Context context) {
      this.context = context;
      OpenHelper openHelper = new OpenHelper(this.context);
      this.db = openHelper.getWritableDatabase();
      this.insertStmt = this.db.compileStatement(INSERT);
      
   }
 
   public long insert(String dname,String app,long allow) {
      this.insertStmt.bindString(1, dname);
      this.insertStmt.bindString(2, app);
      this.insertStmt.bindLong(3, allow);
      Log.w("Anup", "Inserting into DB");
      long retval = this.insertStmt.executeInsert();
      this.db.close();
      return retval;
   }
   public long update(String dname,String app, long allow) {
	   //private ContentValues createContentValues(String category, String summary,String description) {
			ContentValues values = new ContentValues();
			values.put("dname",dname);
			values.put("app",app);
			values.put("allow",allow);
	//  public boolean updateTodo(long rowId, String category, String summary,String description) {
		return this.db.update(TABLE_NAME, values, "dname=?", new String[] { dname });
  }
 
   public void deleteAll() {
      this.db.delete(TABLE_NAME, null, null);
   }
   public long getAllow(String dname,String app)
   {
	   Log.w("Anup", "getting allow 1");
	   long retval = 2;
	   Log.w("Anup", "getting allow 2");
	   
	   //Cursor cursor = this.db.query(TABLE_NAME, new String[] { "dname" ,"allow"},
	//		   null , null, null, null, null);
	   //Cursor cursor = db.query(TABLE_NAME, null," dname = '" + dname + "'", null, null, null, null);
	   Cursor cursor = db.query(TABLE_NAME, new String[] { "dname" , "app" , "allow"},
               "dname = '" + dname.toString() + "'", null, null, null, null);
	   
	   Log.w("Anup", "getting allow 3");
	   cursor.moveToFirst();
	   if (cursor.getCount() == 0){
		   Log.w("Anup", "count == 0 ");
		   Log.e("Anup", "returning 2 value as cursor is empty "+ retval);
		   cursor.close();
		   return retval;
		  }
	   
	   Log.w("Anup", "getting allow 4");
	   
	   Log.w("Anup", "getting allow 5");
	   while(!cursor.isLast()){   
		   Log.w("Anup", "getting allow 6");
		   Log.w("Anup", "in while domain : app == " + cursor.getString(0).toString() +" : "+cursor.getString(1).toString());
		   if(cursor.getString(1).toString().equals(app)){
			   retval =   cursor.getLong(2);
			   if (cursor != null && !cursor.isClosed()) 
				   cursor.close();
			   Log.w("MyDB1", "returning allow/deny value"+ retval);	   
			   return retval;
		   }
		    	  	cursor.moveToNext();
	   }
	   if(cursor.getString(1).toString().equals(app)){
		   retval =   cursor.getLong(2);
		   if (cursor != null && !cursor.isClosed()) 
			   cursor.close();
		   Log.w("MyDB1", "returning allow/deny value"+ retval);	   
		   return retval;
	   }
	   Log.e("Anup", "returning Unknown "+ retval);
	   cursor.close();
	   return retval;
   }
   /*public String getdomain(String pname1)
   {
	   Log.w("MyDB", "getting domain");
	   String retval = "temp";
	   Cursor cursor = this.db.query(TABLE_NAME, new String[] { "pname" , "name" , "pass" , "domain" , "stun", "stunserv", "ilbc" , "g722" , "pcmu" },
			   null , null, null, null, null);
	   cursor.moveToFirst();
	   while(!cursor.isLast()){
		   if(cursor.getString(0).toString().equals(pname1)){
			   retval =   cursor.getString(3);
			   if (cursor != null && !cursor.isClosed()) 
				   cursor.close();
			   Log.e("MyDB1", "returning domain"+ retval);	   
			   return retval;
		   }
		    	  	cursor.moveToNext();
	   }
	   if(cursor.getString(0).toString().equals(pname1)){
		   if(cursor.getString(0).toString().equals(pname1)){
			   retval =   cursor.getString(3);
			   if (cursor != null && !cursor.isClosed()) 
				   cursor.close();
			   Log.e("MyDB2", "returning domain"+ retval);
			   return retval;
		   }
		  
	   }
	   Log.e("MyDB", "returning NULL name"+ retval);
	   return null;
   }*/
   
   
   public List<String> selectAll() {
      List<String> list = new ArrayList<String>();
      Cursor cursor = this.db.query(TABLE_NAME, new String[] { "pname" }, //"name","pass","domain"
        null, null, null, null, null);
      if (cursor.moveToFirst()) {
         do {
            list.add(cursor.getString(0));
         } while (cursor.moveToNext());
      }
      if (cursor != null && !cursor.isClosed()) {
         cursor.close();
      }
      return list;
   }
 
   private static class OpenHelper extends SQLiteOpenHelper {
 
      OpenHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }
 
      @Override
      public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE " + TABLE_NAME + "(dname TEXT,app TEXT,allow NUMBER,PRIMARY KEY (dname, app))");
      }
 
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
         onCreate(db);
      }
   }
}