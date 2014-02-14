package edu.brandeis.samuelmoore.expensetracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ExpenseAddActivity extends Activity{
	
	public String generatedDate;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expense_add);
		
		//Cancel Button
		Button cancel = (Button) findViewById(R.id.expense_add_button_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);        
				finish();
			}
		});
		
		//Save Button
		Button save = (Button) findViewById(R.id.expense_add_button_save);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// If both fields have data	
				EditText nameTxt = (EditText) findViewById(R.id.expense_add_textbox_name);
				EditText infoTxt = (EditText) findViewById(R.id.expense_add_textbox_info);
				 
				if((nameTxt.getText().toString().length() != 0)&&(infoTxt.getText().toString().length() != 0))
				{					     
					 Intent data = new Intent();
					 
					 SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm MM/dd/yyyy");
					 Date expenseDate = new Date();
					 generatedDate = dateFormat.format(expenseDate);
					 
					 data.putExtra("name", nameTxt.getText().toString());
					 data.putExtra("info", infoTxt.getText().toString());
					 data.putExtra("date", generatedDate);
					 setResult(RESULT_OK, data);
					 finish();
				}
				else{
					Toast toast = Toast.makeText(getApplicationContext(),
			                "Make sure all fields are complete!",
			                Toast.LENGTH_SHORT);
			        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			        toast.show();
				}		
			}
		});
	}
	@Override
	public void finish(){
		     
		 super.finish();
	}
	
	public void callReturn(View view)
	{
		finish();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
