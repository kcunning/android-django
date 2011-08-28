package net.realkatie.djangoexplorer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
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
	String title;
	String parent;
	List<String> ch = new ArrayList<String>();
	HashMap<String, String> hash = new HashMap<String, String>();
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);
        
        getDb();
        
        Bundle extras = getIntent().getExtras();
        
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
        
        childrenView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	Intent intent = new Intent(view.getContext(), Explorer.class);
            	String itemTitle = (String) ((TextView) view).getText();
            	title = title + "." + ((TextView) view).getText();
            	String itemId = hash.get(itemTitle);
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
