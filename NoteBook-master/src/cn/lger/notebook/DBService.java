package cn.lger.notebook;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;

public class DBService {
	
	private static SQLiteDatabase db = null;
	
	static {
		db = SQLiteDatabase.openOrCreateDatabase("data/data/cn.lger.notebook/NoteBook.db", null);
		
		String sql = "create table NoteBook(_id integer primary key autoincrement,title varchar(255),content TEXT, createTime varchar(25))";
		
		try{
			db.rawQuery("select count(1) from NoteBook ",null);
		}catch(Exception e){
			db.execSQL(sql);
		}
	}
	
	public static SQLiteDatabase getSQLiteDatabase(){
		return db;
	}
	
	public static Cursor queryAll(){
		return db.rawQuery("select * from NoteBook ",null);
	}
	
	public static Cursor queryNoteById(Integer id){
		return db.rawQuery("select * from NoteBook where _id =?",new String[]{id.toString()});
	}
	
	public static void deleteNoteById(Integer id){
		if(id == null)
			return ;
		db.delete("NoteBook", "_id=?", new String[]{id.toString()});
	}
	
	public static void updateNoteById(Integer id, ContentValues values){
		db.update("NoteBook", values, "_id=?", new String[]{id.toString()});
	}
	
	public static void addNote(ContentValues values){
		values.put("createTime", DateFormat.format("yyyy-MM-dd kk:mm:ss", System.currentTimeMillis()).toString());
		db.insert("NoteBook", null, values);
	}
	
	
}
