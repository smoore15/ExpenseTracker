package edu.brandeis.samuelmoore.expensetracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView;

public class MainActivity extends Activity implements OnItemClickListener{

	DBAdapter2 myDb;
	SimpleCursorAdapter myCursorAdapter;
	static final int RETURN_ENTRY_VALUE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);				
				
		openDB();
		populateListViewFromDB();
		registerListClickCallback();
		

		//Add button
		Button add_button = (Button) findViewById(R.id.main_add_button);
		add_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent addExpense = new Intent(MainActivity.this, ExpenseAddActivity.class);
				startActivityForResult(addExpense, 1);
			}
		});
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		closeDB();
	}
	
	private void openDB(){
		myDb = new DBAdapter2(this);
		myDb.open();
	}
	
	private void closeDB(){
		myDb.close();
	}
		
	public void addRecord(String name, String info, String date){
		myDb.insertRow(name,info,date);
	}
	
	public void DisplayRecord(Cursor c){
		new AlertDialog.Builder(this)
		.setTitle("" + c.getString(1))
		.setMessage("" + c.getString(1) + "\n"
					   + c.getString(2) + "\n"
					   + c.getString(3))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which){}
		})
		.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub	
			}
		})
		.show();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RETURN_ENTRY_VALUE)    {
		if(resultCode == RESULT_OK){
			
				 String name = data.getExtras().getString("name");
				 String info = data.getExtras().getString("info");
				 String date = data.getExtras().getString("date");
								 
				 addRecord(name,info,date);
		     }
		     if (resultCode == RESULT_CANCELED) {}
		  }
	}//onActivityResult
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void registerListClickCallback() {
		ListView myList = (ListView) findViewById(R.id.main_listview);
		myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, 
					int position, long idInDB) {

				updateItemForId(idInDB);
				displayDialogForId(idInDB);
			}
		});
	}
	
	private void updateItemForId(long idInDB) {
		Cursor cursor = myDb.getRow(idInDB);
		if (cursor.moveToFirst()) {
			long idDB = cursor.getLong(DBAdapter2.COL_ROWID);
			String name = cursor.getString(DBAdapter2.COL_NAME);
			String info = cursor.getString(DBAdapter2.COL_INFO);
			String date = cursor.getString(DBAdapter2.COL_DATE);

			myDb.updateRow(idInDB, name, info, date);
		}
		cursor.close();
		populateListViewFromDB();		
	}
	
	private void displayDialogForId(final long idInDB) {
		Cursor cursor = myDb.getRow(idInDB);
		if (cursor.moveToFirst()) {
			long idDB = cursor.getLong(DBAdapter2.COL_ROWID);
			String name = cursor.getString(DBAdapter2.COL_NAME);
			String info = cursor.getString(DBAdapter2.COL_INFO);
			String date = cursor.getString(DBAdapter2.COL_DATE);
			
			String message = "ID: " + idDB + "\n" 
					+ "Name: " + name + "\n"
					+ "Info: " + info + "\n"
					+ "Date: " + date;
			
			new AlertDialog.Builder(this)
			.setTitle("ID: " + idInDB)
			.setMessage(message)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){}
			})
			.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {					
					myDb.deleteRow(idInDB);
					populateListViewFromDB();
					myCursorAdapter.notifyDataSetChanged();
				}
			})
			.show();
		}
		cursor.close();
	}
	
	@SuppressWarnings("deprecation")
	private void populateListViewFromDB() {
		// TODO Auto-generated method stub
		Cursor cursor = myDb.getAllRows();
		
		// Allow activity to manage lifetime of the cursor.
		// DEPRECATED! Runs on the UI thread, OK for small/short queries.
		startManagingCursor(cursor);
		
		// Setup mapping from cursor to view fields:
		String[] fromFieldNames = new String[] 
				{DBAdapter2.KEY_NAME, DBAdapter2.KEY_INFO, DBAdapter2.KEY_DATE};
		int[] toViewIDs = new int[]
				{R.id.expense_entry_name,     R.id.expense_entry_info,           R.id.expense_entry_date};
		
		// Create adapter to may columns of the DB onto elements in the UI.
		myCursorAdapter = 
				new SimpleCursorAdapter(
						this,		// Context
						R.layout.expense_entry,	// Row layout template
						cursor,					// cursor (set of DB records to map)
						fromFieldNames,			// DB Column names
						toViewIDs				// View IDs to put information in
						);
		
		// Set the adapter for the list view
		ListView myList = (ListView) findViewById(R.id.main_listview);
		myList.setAdapter(myCursorAdapter);
		myCursorAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		updateItemForId(arg3);
		displayDialogForId(arg3);
	}
}