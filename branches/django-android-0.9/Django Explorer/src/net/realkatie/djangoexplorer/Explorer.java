package net.realkatie.djangoexplorer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Explorer extends Activity {
	DataBaseHelper myDbHelper;
	
	String parent;
	List <String> packagesWithModules;
	List<String> ch = new ArrayList<String>();
	HashMap<String, String> hash = new HashMap<String, String>();
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);
        
        getDb();
        
        String title;
        
        Bundle extras = getIntent().getExtras();
        
        if (packagesWithModules == null) {
        	packagesWithModules = myDbHelper.getPackagesWithModules();
        }
        
        if (extras == null) {
        	title = "django";
        	parent = "0";
        } else {
        	parent = (String) extras.get("parent_id");
        	
        	title = (String) extras.get("title");
        }
        
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
        
        final Cursor cursor = myDbHelper.getChildren(parent);
        int titleIndex = 1;
        int idIndex = 0;
        if (cursor.moveToFirst()) {
        	do {
        		ch.add(cursor.getString(titleIndex));
        		hash.put(cursor.getString(titleIndex), cursor.getString(idIndex));
        	} while (cursor.moveToNext());
        }
        
        
        
        ListView childrenView = (ListView) findViewById(R.id.children);
        childrenView.setClickable(true);
        
        childrenView.setAdapter(new ArrayAdapter<String>(this, R.layout.node_list_item, ch));
        final String nextTitle = title;
        childrenView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	
            	
            	String itemTitle = (String) ((TextView) view).getText();
            	String title = nextTitle + "." + ((TextView) view).getText();
            	String itemId = hash.get(itemTitle);
            	Intent intent;
            	if (packagesWithModules.contains(itemId)) {
            		intent = new Intent(view.getContext(), Package.class);
            	} else {
            		intent = new Intent(view.getContext(), Explorer.class);
            		
            	}
            	
            	intent.putExtra("title", title);
            	intent.putExtra("parent_id", itemId);
            	startActivityForResult(intent, 0);
            	
            }
        });
			
       
        
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
