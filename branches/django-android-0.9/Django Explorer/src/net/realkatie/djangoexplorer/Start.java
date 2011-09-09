package net.realkatie.djangoexplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start extends Activity {
	//This is the first activity called when the app is started.
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        final Button exploreButton = (Button) findViewById(R.id.explore_button);
        exploreButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Packages.class);
				startActivityForResult(intent, 0);
			}
		});
    }
}