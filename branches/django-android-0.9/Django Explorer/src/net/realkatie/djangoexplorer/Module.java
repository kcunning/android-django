package net.realkatie.djangoexplorer;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Module extends Activity {
	DataBaseHelper myDbHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		System.out.println("In Module");
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
        
        ArrayList <ModuleClass> classes = new ArrayList();
        if (classesCursor.moveToFirst()) {
        	do {
        		ModuleClass mod = new ModuleClass();
        		mod.setTitle(classesCursor.getString(0));
        		mod.setDescription(classesCursor.getString(1));
        		classes.add(mod);
        		
        	} while (classesCursor.moveToNext());
        }
        
        ListView classesView = (ListView) findViewById(R.id.classes);
        
        ModuleAdapter adapter = new ModuleAdapter(this, R.layout.list_with_desc, classes);
        classesView.setAdapter(adapter);
        
        
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
	
	public class ModuleAdapter extends ArrayAdapter<ModuleClass> {

		private ArrayList<ModuleClass> items;
		
		public ModuleAdapter(Context context, int textViewResourceId, ArrayList<ModuleClass> items) {
			super(context, textViewResourceId);
			this.items = items;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			System.out.println("In getView");
			View v = convertView;
			
			LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_with_desc, null);
            
			ModuleClass mod = items.get(position);
			
				System.out.println("There's a mod");
				TextView titleView = (TextView) v.findViewById(R.id.class_title);
				TextView descView = (TextView) v.findViewById(R.id.class_desc);
				titleView.setText(mod.getTitle());
				System.out.println(mod.getTitle());
				descView.setText(mod.getDesc());
			
			return v;
		}
		
			
	}
}
