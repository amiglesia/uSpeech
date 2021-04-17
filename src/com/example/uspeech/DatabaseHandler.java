package com.example.uspeech;

import java.util.ArrayList;



import com.example.uspeech.Command;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "commandManager";

    // Contacts table name
    private static final String TABLE_COMMAND = "commands";

    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_PIN = "pin";
    private static final String KEY_ON = "commandOn";
    private static final String KEY_OFF = "commandOff";
    private final ArrayList<Command> command_list = new ArrayList<Command>();
	
	public DatabaseHandler(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		    }
	
	// Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
	String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_COMMAND + "("
		+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_PIN + " TEXT,"
		+ KEY_ON + " TEXT," + KEY_OFF + " TEXT" + ")";
	db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// Drop older table if existed
	db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMAND);

	// Create tables again
	onCreate(db);
    }
    
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    
    // Adding new command
    public void Add_Command(Command command) {
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(KEY_PIN, command.getPin()); // Pin
	values.put(KEY_ON, command.getCommandOn()); // Command On
	values.put(KEY_OFF, command.getCommandOff()); // Command Off
	// Inserting Row
	db.insert(TABLE_COMMAND, null, values);
	db.close(); // Closing database connection
    }
    /*
 // Getting single contact
    public Command Get_On(String pin) {
    	SQLiteDatabase db = this.getReadableDatabase();

    	Cursor cursor = db.query(TABLE_COMMAND, new String[] { KEY_ID,
    		KEY_PIN, KEY_ON, KEY_OFF }, KEY_ON + "=?"  ,
    		new String[] { String.valueOf(pin) }, null, null, null, null);
    	
    	int index1=cursor.getColumnIndex(KEY_ON);
    	if (cursor != null)
    	    cursor.moveToFirst();

    	Command command = new Command(
    		cursor.getString(1), cursor.getColumnName(index1));
    	// return contact
    	if ((cursor.getString(1).length()==0)&& (cursor.getColumnName(index1).length()==0))
    	{
    		cursor = db.query(TABLE_COMMAND, new String[] { KEY_ID,
    	    		KEY_PIN, KEY_ON, KEY_OFF }, KEY_OFF + "=?"  ,
    	    		new String[] { String.valueOf(pin) }, null, null, null, null);
    	    	
    	    	int index2=cursor.getColumnIndex(KEY_OFF);
    	    	if (cursor != null)
    	    	    cursor.moveToFirst();

    	    	command = new Command(
    	    		cursor.getString(1), cursor.getColumnName(index2));
    	}
    	
    	
    	
    	cursor.close();
    	db.close();

    	return command;
    }
    /*
    public Command Get_Off(String pin) {
    	SQLiteDatabase db = this.getReadableDatabase();

    	
    	// return contact
    	cursor.close();
    	db.close();

    	return command;
    }
	*/
    
    
    // Getting on
    
    public String Get_On(String pin) {
	SQLiteDatabase db = this.getReadableDatabase();

	Cursor cursor = db.query(TABLE_COMMAND, new String[] { KEY_ID,
		KEY_PIN, KEY_ON, KEY_OFF }, KEY_ON + "=?"  ,
		new String[] { String.valueOf(pin) }, null, null, null, null);
	StringBuffer buffer=new StringBuffer();
	while(cursor.moveToNext())
	{
		int index0=cursor.getColumnIndex(KEY_PIN);
		int index1=cursor.getColumnIndex(KEY_ON);
		int personId=cursor.getInt(index0);
		String txtPassword = cursor.getColumnName(index1);
		buffer.append(personId+" "+txtPassword);
	}
	
	return buffer.toString();
    }
   
 // Getting off
    public String Get_Off(String pin) {
	SQLiteDatabase db = this.getReadableDatabase();

	Cursor cursor = db.query(TABLE_COMMAND, new String[] { KEY_ID,
		KEY_PIN, KEY_ON, KEY_OFF }, KEY_OFF + "=?"  ,
		new String[] { String.valueOf(pin) }, null, null, null, null);
	StringBuffer buffer=new StringBuffer();
	while(cursor.moveToNext())
	{
		int index0=cursor.getColumnIndex(KEY_PIN);
		int index1=cursor.getColumnIndex(KEY_OFF);
		int personId=cursor.getInt(index0);
		String txtPassword = cursor.getColumnName(index1);
		buffer.append(personId+" "+txtPassword);
	}
	
	return buffer.toString();
    } 
    
    
 // Getting single command
    Command Get_Command(int id) {
	SQLiteDatabase db = this.getReadableDatabase();

	Cursor cursor = db.query(TABLE_COMMAND, new String[] { KEY_ID,
		KEY_PIN, KEY_ON, KEY_OFF }, KEY_ID + "=?",
		new String[] { String.valueOf(id) }, null, null, null, null);
	if (cursor != null)
	    cursor.moveToFirst();

	Command contact = new Command(Integer.parseInt(cursor.getString(0)),
		cursor.getString(1), cursor.getString(2), cursor.getString(3));
	// return contact
	cursor.close();
	db.close();

	return contact;
    }
    
    

    // Getting All Contacts
    public ArrayList<Command> Get_Commands() {
	try {
	    command_list.clear();

	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_COMMAND;

	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);

	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
		do {
		    Command command = new Command();
		    command.setID(Integer.parseInt(cursor.getString(0)));
		    command.setPin(cursor.getString(1));
		    command.setCommandOn(cursor.getString(2));
		    command.setCommandOff(cursor.getString(3));
		    // Adding contact to list
		    command_list.add(command);
		} while (cursor.moveToNext());
	    }

	    // return contact list
	    cursor.close();
	    db.close();
	    return command_list;
	} catch (Exception e) {
	    // TODO: handle exception
	    Log.e("all_command", "" + e);
	}

	return command_list;
    }
    
    // Updating single contact
    public int Update_Contact(Command command) {
	SQLiteDatabase db = this.getWritableDatabase();

	ContentValues values = new ContentValues();
	values.put(KEY_PIN, command.getPin());
	values.put(KEY_ON, command.getCommandOn());
	values.put(KEY_OFF, command.getCommandOff());

	// updating row
	return db.update(TABLE_COMMAND, values, KEY_ID + " = ?",
		new String[] { String.valueOf(command.getID()) });
    }
    
    // Deleting single contact
    public void Delete_Contact(int id) {
	SQLiteDatabase db = this.getWritableDatabase();
	db.delete(TABLE_COMMAND, KEY_ID + " = ?",
		new String[] { String.valueOf(id) });
	db.close();
    }

    // Getting contacts Count
    public int Get_Total_Contacts() {
	String countQuery = "SELECT  * FROM " + TABLE_COMMAND;
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.rawQuery(countQuery, null);
	cursor.close();

	// return count
	return cursor.getCount();
    }

}
