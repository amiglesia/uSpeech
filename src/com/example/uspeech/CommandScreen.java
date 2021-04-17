package com.example.uspeech;

import java.util.ArrayList;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommandScreen extends Activity {
	
	Button add_btn;
    ListView Contact_listview;
    ArrayList<Command> command_data = new ArrayList<Command>();
    Command_Adapter cAdapter;
    DatabaseHandler db;
    String Toast_msg;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.screen_command);
	try {
	    Contact_listview = (ListView) findViewById(R.id.list);
	    Contact_listview.setItemsCanFocus(false);
	    add_btn = (Button) findViewById(R.id.add_btn);

	    Set_Referash_Data();

	} catch (Exception e) {
	    // TODO: handle exception
	    Log.e("some error", "" + e);
	}
	add_btn.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent add_user = new Intent(CommandScreen.this,
			Add_Update.class);
		add_user.putExtra("called", "add");
		add_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(add_user);
		finish();
	    }
	});

    }
    
    public void Set_Referash_Data() {
    	command_data.clear();
    	db = new DatabaseHandler(this);
    	ArrayList<Command> command_array_from_db = db.Get_Commands();

    	for (int i = 0; i < command_array_from_db.size(); i++) {

    	    int tidno = command_array_from_db.get(i).getID();
    	    String pin = command_array_from_db.get(i).getPin();
    	    String on = command_array_from_db.get(i).getCommandOn();
    	    String off = command_array_from_db.get(i).getCommandOff();
    	    Command cmd = new Command();
    	    cmd.setID(tidno);
    	    cmd.setPin(pin);
    	    cmd.setCommandOn(on);
    	    cmd.setCommandOff(off);

    	    command_data.add(cmd);
    	}
    	db.close();
    	cAdapter = new Command_Adapter(CommandScreen.this, R.layout.listview_row,
    		command_data);
    	Contact_listview.setAdapter(cAdapter);
    	cAdapter.notifyDataSetChanged();
        }
    	
    public void Show_Toast(String msg) {
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }

    @Override
    public void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    Set_Referash_Data();

    }
    
    public class Command_Adapter extends ArrayAdapter<Command> {
    	Activity activity;
    	int layoutResourceId;
    	Command user;
    	ArrayList<Command> data = new ArrayList<Command>();

    	public Command_Adapter(Activity act, int layoutResourceId,
    		ArrayList<Command> data) {
    	    super(act, layoutResourceId, data);
    	    this.layoutResourceId = layoutResourceId;
    	    this.activity = act;
    	    this.data = data;
    	    notifyDataSetChanged();
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    	    View row = convertView;
    	    UserHolder holder = null;

    	    if (row == null) {
    		LayoutInflater inflater = LayoutInflater.from(activity);

    		row = inflater.inflate(layoutResourceId, parent, false);
    		holder = new UserHolder();
    		holder.pin = (TextView) row.findViewById(R.id.command_pin);
    		holder.on = (TextView) row.findViewById(R.id.command_on);
    		holder.off = (TextView) row.findViewById(R.id.command_off);
    		holder.edit = (Button) row.findViewById(R.id.btn_update);
    		holder.delete = (Button) row.findViewById(R.id.btn_delete);
    		row.setTag(holder);
    	    } else {
    		holder = (UserHolder) row.getTag();
    	    }
    	    user = data.get(position);
    	    holder.edit.setTag(user.getID());
    	    holder.delete.setTag(user.getID());
    	    holder.pin.setText(user.getPin());
    	    holder.on.setText(user.getCommandOn());
    	    holder.off.setText(user.getCommandOff());

    	    holder.edit.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    		    // TODO Auto-generated method stub
    		    Log.i("Edit Button Clicked", "**********");

    		    Intent update_user = new Intent(activity,
    			    Add_Update.class);
    		    update_user.putExtra("called", "update");
    		    update_user.putExtra("USER_ID", v.getTag().toString());
    		    activity.startActivity(update_user);

    		}
    	    });
    	    holder.delete.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(final View v) {
    		    // TODO Auto-generated method stub

    		    // show a message while loader is loading

    		    AlertDialog.Builder adb = new AlertDialog.Builder(activity);
    		    adb.setTitle("Delete?");
    		    adb.setMessage("Are you sure you want to delete ");
    		    final int user_id = Integer.parseInt(v.getTag().toString());
    		    adb.setNegativeButton("Cancel", null);
    		    adb.setPositiveButton("Ok",
    			    new AlertDialog.OnClickListener() {
    				@Override
    				public void onClick(DialogInterface dialog,
    					int which) {
    				    // MyDataObject.remove(positionToRemove);
    				    DatabaseHandler dBHandler = new DatabaseHandler(
    					    activity.getApplicationContext());
    				    dBHandler.Delete_Contact(user_id);
    				    CommandScreen.this.onResume();

    				}
    			    });
    		    adb.show();
    		}

    	    });
    	    return row;

    	}
    	class UserHolder {
    	    TextView pin;
    	    TextView on;
    	    TextView off;
    	    Button edit;
    	    Button delete;
    	}
    }
}
