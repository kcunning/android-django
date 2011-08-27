package net.realkatie.djangoexplorer;

import java.io.IOException;

import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.TextView;

public class Explorer extends Activity {
	DataBaseHelper myDbHelper;
	String title;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);
        getDb();
        
        Bundle extras = getIntent().getExtras();
        
        if (extras == null) {
        	title = "django";
        }
        
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
        
    }
	
	public void getDb() {
		myDbHelper = new DataBaseHelper(this);
        try {
        	myDbHelper.createDataBase();
        } catch (IOException ioe) {
        	throw new Error("Unable to create database");
        }
 
        try {
        	myDbHelper.openDataBase();
        }catch(SQLException sqle){
        	throw sqle;
        }
	}
}
