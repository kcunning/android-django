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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
        
        String desc = myDbHelper.getDescription(parent_id, "modules");
        TextView descView = (TextView) findViewById(R.id.description);
        descView.setText(desc);
        
        
        
        
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
	
	public class ModuleAdapter extends BaseAdapter {

		private ArrayList<ModuleClass> mods;
		private Context c;
		
		public ModuleAdapter(Context c, ArrayList<ModuleClass> mods) {
			this.c = c;
			this.mods = mods;
		}
		
		public int getCount() {
			return mods.size();
		}

		public Object getItem(int position) {
			return mods.get(position);
		}

		public long getItemId(int id) {
			return id;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout rowLayout = new LinearLayout(getApplicationContext());
			ModuleClass m = mods.get(position);
			
			if (convertView == null) {
				rowLayout = (LinearLayout) LayoutInflater.from(c).inflate(R.layout.list_with_desc, parent, false);
				TextView title = (TextView) rowLayout.findViewById(R.id.class_title);
				title.setText(m.getTitle());
				TextView desc = (TextView) rowLayout.findViewById(R.id.class_desc);
				desc.setText(m.getDesc());
				
			} else {
				 rowLayout = (LinearLayout)convertView;
			}
			return rowLayout;
		}
		
	}
		
			
	
}
