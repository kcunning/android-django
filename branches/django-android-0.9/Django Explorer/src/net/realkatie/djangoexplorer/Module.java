package net.realkatie.djangoexplorer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Module extends Activity {
	DataBaseHelper myDbHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.module);
        
        Bundle extras = getIntent().getExtras();
        
        getDb();
        
        
        String title = extras.getString("title"); 
        String parent_id = extras.getString("parent_id");
        
        
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
        
        String desc = myDbHelper.getModuleDescription(parent_id);
        TextView descView = (TextView) findViewById(R.id.description);
        descView.setText(desc);
        
        Cursor classesCursor;
        classesCursor = myDbHelper.getClasses(parent_id);
        
        List <String> classes = new ArrayList();
        if (classesCursor.moveToFirst()) {
        	do {
        		classes.add(classesCursor.getString(0));
        	} while (classesCursor.moveToNext());
        }
        
        ListView classesView = (ListView) findViewById(R.id.classes);
        ArrayAdapter<String> classesAdapter;
        
        
        classesView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, classes));
        
        
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
