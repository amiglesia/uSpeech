package com.example.uspeech;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Add_Update extends Activity{
	
	 EditText add_pin, add_on, add_off;
	 Button add_save_btn, add_view_all, update_btn, update_view_all;
	 LinearLayout add_view, update_view;
	 String valid_off = null, valid_on = null, valid_pin = null,
	    	    Toast_msg = null, valid_user_id = "";
	 int USER_ID;
	    DatabaseHandler dbHandler = new DatabaseHandler(this);
	    
	    
	    
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	// TODO Auto-generated method stub
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.add_update);

	    	// set screen
	    	Set_Add_Update_Screen();

	    	// set visibility of view as per calling activity
	    	String called_from = getIntent().getStringExtra("called");

	    	if (called_from.equalsIgnoreCase("add")) {
	    	    add_view.setVisibility(View.VISIBLE);
	    	    update_view.setVisibility(View.GONE);
	    	} else {

	    	    update_view.setVisibility(View.VISIBLE);
	    	    add_view.setVisibility(View.GONE);
	    	    USER_ID = Integer.parseInt(getIntent().getStringExtra("USER_ID"));

	    	    Command c = dbHandler.Get_Command(USER_ID);

	    	    add_pin.setText(c.getPin());
	    	    add_on.setText(c.getCommandOn());
	    	    add_off.setText(c.getCommandOff());
	    	    // dbHandler.close();
	    	}
	    	
	    	add_save_btn.setOnClickListener(new View.OnClickListener() {

	    	    @Override
	    	    public void onClick(View v) {
	    		// TODO Auto-generated method stub
	    		// check the value state is null or not
	    	    	valid_pin=add_pin.getText().toString();
	    	    	valid_on=add_on.getText().toString();
	    	    	valid_off=add_off.getText().toString();
	    	    	
	    	    if (valid_pin != null && valid_off != null
	    			&& valid_on != null && valid_pin.length() != 0
	    			&& valid_off.length() != 0
	    			&& valid_on.length() != 0) {

	    		    dbHandler.Add_Command(new Command(valid_pin,
	    			    valid_on, valid_off));
	    		    Toast_msg = "Data inserted successfully";
	    		    Show_Toast(Toast_msg);
	    		    Reset_Text();

	    		}

	    	    }
	    	});

	    	update_btn.setOnClickListener(new View.OnClickListener() {

	    	    @Override
	    	    public void onClick(View v) {
	    		// TODO Auto-generated method stub

	    		valid_pin = add_pin.getText().toString();
	    		valid_off = add_off.getText().toString();
	    		valid_on = add_on.getText().toString();

	    		// check the value state is null or not
	    		if (valid_pin != null && valid_off != null
	    			&& valid_on != null && valid_pin.length() != 0
	    			&& valid_off.length() != 0
	    			&& valid_on.length() != 0) {

	    		    dbHandler.Update_Contact(new Command(USER_ID, valid_pin,
	    			    valid_on, valid_off));
	    		    dbHandler.close();
	    		    Toast_msg = "Data Update successfully";
	    		    Show_Toast(Toast_msg);
	    		    Reset_Text();
	    		} else {
	    		    Toast_msg = "Sorry Some Fields are missing.\nPlease Fill up all.";
	    		    Show_Toast(Toast_msg);
	    		}

	    	    }
	    	});
	    	update_view_all.setOnClickListener(new View.OnClickListener() {

	    	    @Override
	    	    public void onClick(View v) {
	    		// TODO Auto-generated method stub
	    		Intent view_user = new Intent(Add_Update.this,
	    			CommandScreen.class);
	    		view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	    			| Intent.FLAG_ACTIVITY_NEW_TASK);
	    		startActivity(view_user);
	    		finish();
	    	    }
	    	});

	    	add_view_all.setOnClickListener(new View.OnClickListener() {

	    	    @Override
	    	    public void onClick(View v) {
	    		// TODO Auto-generated method stub
	    		Intent view_user = new Intent(Add_Update.this,
	    			CommandScreen.class);
	    		view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	    			| Intent.FLAG_ACTIVITY_NEW_TASK);
	    		startActivity(view_user);
	    		finish();
	    	    }
	    	});
	    	
	    }
		
	    public void Set_Add_Update_Screen() {

	    	add_pin = (EditText) findViewById(R.id.add_pin);
	    	add_on = (EditText) findViewById(R.id.add_on);
	    	add_off = (EditText) findViewById(R.id.add_off);

	    	add_save_btn = (Button) findViewById(R.id.add_save_btn);
	    	update_btn = (Button) findViewById(R.id.update_btn);
	    	add_view_all = (Button) findViewById(R.id.add_view_all);
	    	update_view_all = (Button) findViewById(R.id.update_view_all);

	    	add_view = (LinearLayout) findViewById(R.id.add_view);
	    	update_view = (LinearLayout) findViewById(R.id.update_view);

	    	add_view.setVisibility(View.GONE);
	    	update_view.setVisibility(View.GONE);

	        }

	    public void Show_Toast(String msg) {
	    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	        }

	        public void Reset_Text() {

	    	add_pin.getText().clear();
	    	add_on.getText().clear();
	    	add_off.getText().clear();

	        }
	
}
