package com.pwned.aggrogamer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;
 

public class AggroRSSTablet extends FragmentActivity {
	private ProgressDialog m_ProgressDialog = null;
    private ArrayList<NewsItem> m_news = null;
    private HashMap<String, String> tempArray = new HashMap<String, String>();
    private OrderAdapter m_adapter;
    private Runnable viewNews;
    private static final String BREAK_TAG = "item";
    private ListView lv;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_tab);
     
        
        lv = (ListView) findViewById(R.id.atablist);
        
        m_news = new ArrayList<NewsItem>();
        m_adapter = new OrderAdapter(this, R.layout.row, m_news);
        lv.setAdapter(m_adapter);
       
        viewNews = new Thread(){
            @Override
            public void run() {
                getNews();
            }
        };
        Thread thread =  new Thread(null, viewNews, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(AggroRSSTablet.this,"","Loading Game News", true);
    }
     
    @Override
	protected void onSaveInstanceState(Bundle outState) 
    {
		super.onSaveInstanceState(outState); // the UI component values are saved here.
	}
	
	@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }
	
	 @Override
	 protected void onStop() 
	 {
		 super.onStop();
	 }
	 
	 @Override
	 protected void onStart() 
	 {
		 super.onStart();
	 }
    private Thread returnRes = new Thread() {

        @Override
        public void run() {
            if(m_news != null && m_news.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_news.size();i++)
                m_adapter.add(m_news.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,0,0, "Refresh");
    	return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
        case 0:
        	Intent myIntent = new Intent(AggroRSSTablet.this, AggroRSSTablet.class);
			startActivity(myIntent);
            return true;
        case 1:
            return true;
        }
        return false;
    }
    
    private void stackAFragment(String title, String description, String category, String link ) {
    	Fragment f = new NewsTablet(title,description,category,link);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.the_frag, f);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null); 
        ft.commit(); 
    
    }
    
    private void getNews(){
          try{
              m_news = new ArrayList<NewsItem>();           
              try {
      			String url = "http://aggrogamer.com/rss/mobile/?text=true"; 
      			URL aURL = new URL(url);
      			URLConnection conn = aURL.openConnection();
      			conn.connect();
      			InputStream is = conn.getInputStream();
      			/* Buffered is always good for a performance plus. */
      			BufferedInputStream response = new BufferedInputStream(is);

      			if (response != null) {
      				try {

      					XmlPullParserFactory factory = XmlPullParserFactory
      							.newInstance();
      					factory.setNamespaceAware(true);
      					XmlPullParser xpp = factory.newPullParser();

      					xpp.setInput(response, null);
      					int eventType = xpp.getEventType();
      					while (eventType != XmlPullParser.END_DOCUMENT) {

      						if (eventType == XmlPullParser.START_TAG) {
      							String key = xpp.getName();
      							eventType = xpp.next();
      							if (eventType == XmlPullParser.TEXT) {
      								String value = xpp.getText();
      								tempArray.put(key, value);
      								// Toast.makeText(this, key +" => "+
      								// value,Toast.LENGTH_LONG).show();

      							}
      						}
      						if (eventType == XmlPullParser.END_TAG) {
      							String endTag = xpp.getName();

      							if (endTag.equalsIgnoreCase(BREAK_TAG)) {

      								NewsItem o = new NewsItem();
      								o.setTitle(tempArray.get("title"));
      				              	o.setDescription(tempArray.get("description"));
      				              	o.setCategory(tempArray.get("category"));
      				              	o.setLink(tempArray.get("link"));
      				              
      				              	m_news.add(o);
      							}

      						}
      						eventType = xpp.next();

      					}

      				} catch (Exception e) {
      				}

      			} else {
      				//Toast.makeText(this, "response bad", Toast.LENGTH_LONG).show();
      			}
      		} catch (IOException e) {
      			//String mess = e.getMessage();
      			//Toast.makeText(this, mess, Toast.LENGTH_LONG).show();
      		}
              
              
              Log.i("ARRAY", ""+ m_news.size());
            } catch (Exception e) {
              Log.e("BACKGROUND_PROC", e.getMessage());
            }
            runOnUiThread(returnRes);
        }
    private class OrderAdapter extends ArrayAdapter<NewsItem> {

        private ArrayList<NewsItem> items;

        public OrderAdapter(Context context, int textViewResourceId, ArrayList<NewsItem> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                final String description;
                final String title;
                final String link;
                final String category;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                     
                }
                NewsItem o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.toptext);
                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        if (tt != null) {
                              tt.setText(o.getTitle());                            
                        } 
                        if(bt != null){
                              bt.setText(o.getCategory());
                        }
                        description = o.getDescription();
                        title = o.getTitle();
                        link = o.getLink();
                        category = o.getCategory();
                        v.setOnClickListener(new Button.OnClickListener() {
                			public void onClick(View v) {
                				stackAFragment(title,description,category,link);
                				v.setBackgroundColor(12897243);
                				
                			}
                		});
                }
                return v;
        }
    }
}