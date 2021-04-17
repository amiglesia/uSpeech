package com.example.uspeech;





import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;





import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private Handler mHandler = new Handler(); 
	 DatabaseHandler dbHandler = new DatabaseHandler(this);
	 String response=null;
	 String speech = null, pin = null, status = null, message=null;
	 
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        
        
        btnSpeak.setOnClickListener(new View.OnClickListener() {
        	 
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        
        
	}
	// Creating alert Dialog with one Button
	public void Alert(String pin, String status) {
		
		if ((0<=Integer.parseInt(pin))&&(Integer.parseInt(pin)<=5)){
			if (status=="1")
				{status = "on";}
			else if (status=="0")
				{status ="off";}
			
			if (pin.equalsIgnoreCase("0")){
				message = "All socket are " + status + " now.";
			}
			else	
			{
			message = "Socket " + pin + " is " + status + " now.";
			}
				
		}
		else
		{
			message = "There is no socket number " + pin + ".";
		}
		
		AlertDialog alertDialog = new AlertDialog.Builder(
				MainActivity.this).create();

		// Setting Dialog Title
		alertDialog.setTitle("uSpeech");

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.tick);

		// Setting OK Button
		alertDialog.setButton("OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int which) {
						// Write your code here to execute after dialog
						// closed
						
					}
				});

		// Showing Alert Message
		alertDialog.show();
		//Calling the class to remove the speech input after n second/s
		mHandler.postDelayed(mUpdateTimeTask, 5000);//5000 is equal to 5 seconds
		return ;
		
	}	
	
	
	/**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
 
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case REQ_CODE_SPEECH_INPUT: {
            if (resultCode == RESULT_OK && null != data) {
 
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                txtSpeechInput.setText(result.get(0));
            }
            break;
        }
 
        }
        //Calling the method responsible for clarifying if the spoken word is a command 
        //to get its pin and what to do.
        this.GetPinStatus();
    }
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void Setting(View v) {
		Intent intent = new Intent(MainActivity.this, CommandScreen.class);
		startActivity(intent);

	    }
	public void Webview(View v) {
		Intent intent = new Intent(MainActivity.this, Webview.class);
		startActivity(intent);

	    }
	
	/*
	 * Method to get the pin and to either on or off	
	*/
	public void GetPinStatus()
    {
			
		speech = txtSpeechInput.getText().toString();
    	
    	if (speech.length()!=0){
    		response = dbHandler.Get_Off(speech);
    		if (response.length()==0){
    			response = dbHandler.Get_On(speech);
    		}
    		if (response.length()!=0){
    			
    		
    		pin = response.substring(0, response.indexOf(" "));
			status = response.substring(response.indexOf(" " )+1);
			
			String stat = null;
	    	
	    	if (status.equalsIgnoreCase("commandOn"))
			{
				stat = "1";
			}
			else if (status.equalsIgnoreCase("commandOff"))
			{
				stat = "0";
			}
			else{
				stat = "ERROR";
			}
			//Calling the method to send the command to the RPi
			this.Send(pin,stat);
	    	}
    	}
    	
    	else
        {
        	// display message if text fields are empty
            Toast.makeText(getBaseContext(),"Field are required",Toast.LENGTH_SHORT).show();
        }
    
    
    }
		
	
	
	public void Send(String pin1, String status1)
	{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		HttpClient httpclient = new DefaultHttpClient();
   	    HttpPost httppost = new HttpPost("http://192.168.10.50/receive.php");//Url of the Rpi
   	    
		 try {
		   	   List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		       nameValuePairs.add(new BasicNameValuePair("id", "12345"));
		       nameValuePairs.add(new BasicNameValuePair("pin", pin1));
		       nameValuePairs.add(new BasicNameValuePair("status", status1));
		       httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		       StrictMode.setThreadPolicy(policy);
		   	   httpclient.execute(httppost);
		   	//calling alert if successful
		   this.Alert(pin1,status1);
		   	
		     } catch (ClientProtocolException e) {
		    	 Toast.makeText(getApplicationContext()," ERROR ClientProtocolException", Toast.LENGTH_LONG).show();
		    	 // TODO Auto-generated catch block
		     } catch (IOException e) {
		         // TODO Auto-generated catch block
		    	 Toast.makeText(getApplicationContext()," ERROR IOException", Toast.LENGTH_LONG).show();
		     }
		
		 
	}
	//Method to remove the text
	private Runnable mUpdateTimeTask = new Runnable() 
	 {
	    public void run() 
	    {
	    	txtSpeechInput.setText("");       
	    }
	};
	
	
	
}
