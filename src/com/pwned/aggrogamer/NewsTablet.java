package com.pwned.aggrogamer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class NewsTablet extends Fragment {
    private String title = null;
	private String description = null;
	private String category = null;
	private String link = null;
    
    public NewsTablet() {
    	 
    }
 
   /**
    * Constructor for being created explicitly  
    */
   public NewsTablet(String title, String description, String category, String link ) {
	   	this.title = title;
	   	this.description = description;
	   	this.category = category;
	   	this.link = link;
    }

    /**
     * If we are being created with saved state, restore our state
     */
    @Override
    public void onCreate(Bundle saved) {  
        super.onCreate(saved);
        if (null != saved) {
        	this.title = saved.getString("title");
    	   	this.description = saved.getString("description");
    	   	this.category = saved.getString("category");
    	   	this.link = saved.getString("link");
        }
    }
    
    /**
     * Save the attributes to be displayed
     */
    @Override
    public void onSaveInstanceState(Bundle toSave) {
    	toSave.putString("title", title);
    	toSave.putString("description", description);
    	toSave.putString("category", category);
    	toSave.putString("link", link);
    }

    /**  
     * Create the view
     */
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
    	Context c = getActivity().getApplicationContext();
    	LinearLayout itemView = null;
    	if(title != null){
    	itemView = (LinearLayout) inflater.inflate(R.layout.news_tab, container, false);

		
		TextView TVtitle = (TextView) itemView.findViewById(R.id.title);
		WebView TVdescription = (WebView) itemView.findViewById(R.id.description);
		TextView TVcategory = (TextView) itemView.findViewById(R.id.category);
		//TextView TVlink = (TextView) itemView.findViewById(R.id.link);
		//TextView TVlink.setClickable(true);
		
		 
		TVdescription.loadData("<font color='white'>"+description+"</font>", "text/html", "utf-8");
		TVdescription.setBackgroundColor(0);
		TVtitle.setText(title.trim());
			TVcategory.setText("Category: "+this.category);
			//final String NewsURL = link;
			/*
			TVlink.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NewsURL));
					startActivity(myIntent);
				} 
			}); 
			*/
    	} else {
    		itemView = new LinearLayout(c);
    	}
        return itemView; 
    	
    }
}

