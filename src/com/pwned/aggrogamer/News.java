package com.pwned.aggrogamer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class News extends Activity {
	protected static final String NewsURL = null;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	 	super.onCreate(savedInstanceState);
	 	setContentView(R.layout.news);
	 
	 	TextView title = (TextView) findViewById(R.id.title);
	 	TextView description = (TextView) findViewById(R.id.description);
	 	TextView category = (TextView) findViewById(R.id.category);
	 	TextView link = (TextView) findViewById(R.id.link);
	 	link.setClickable(true);
	  
	
	 	Bundle extras = getIntent().getExtras();
	 	if (extras != null) {
	 		description.setText(extras.getString("description").replaceAll("<br />", "\n").replaceAll("<br>", ""));
	 		title.setText(extras.getString("title").trim());
	 		category.setText("Category: "+extras.getString("category"));
	 		final String NewsURL = extras.getString("link");
	 		link.setOnClickListener(new Button.OnClickListener() {
	 			public void onClick(View v) {
	 				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NewsURL));
	 				startActivity(myIntent);
	 			}
	 		});
	 	}
	}
}

