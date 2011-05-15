package com.pwned.aggrogamer;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.TreeMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class AggroWidget extends AppWidgetProvider {
	
	private static Runnable viewNews;
	private static Context ctx;
	
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        context.startService(new Intent(context, UpdateService.class));
    }
    
    public static class UpdateService extends Service {
        @Override
        public void onStart(Intent intent, int startId) {
        	ctx = this;
        	viewNews = new Thread(){
        		@Override
                public void run() {
                	buildUpdate(ctx);
                }
            };
            Thread thread =  new Thread(null, viewNews, "AggroWidgetBackground");
            thread.start();
        }
        
       

        public RemoteViews buildUpdate(Context context) {
            RemoteViews updateViews = null;
            String pageContent = "";
            String pageURL = "";
            String pageTitle = "";
            //Bitmap returnedBitmap = null;
            TreeMap <String, String>uArray = new TreeMap<String, String>();
            
            
            try
    		{
    			String url = "http://aggrogamer.com/rss/latest";
    			
    			

    			URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                /* Buffered is always good for a performance plus. */
                BufferedInputStream response = new BufferedInputStream(is);
                
    			if ( response != null )
    			{
    				
    				try{

    					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    					factory.setNamespaceAware(true);
    					XmlPullParser xpp = factory.newPullParser();
    					
    					xpp.setInput( response, null );
    					int eventType = xpp.getEventType();
    					while (eventType != XmlPullParser.END_DOCUMENT) {
    						

    						
    						
    						if(eventType == XmlPullParser.START_TAG) {
    							String key = xpp.getName();
    							eventType = xpp.next();
    							if(eventType == XmlPullParser.TEXT) {
    								String value = xpp.getText();
    								uArray.put(key,value);
    							}
    						}
    						eventType = xpp.next();
    						
    					}
    					
    				}catch(Exception e){}
    				
    				
    				
    			}
    			else
    			{
    				Toast.makeText(this, "response bad",Toast.LENGTH_LONG).show();
    			}
    		} catch (IOException e) {
    			String mess = e.getMessage();
    			Toast.makeText(this, mess,Toast.LENGTH_LONG).show();
    		}
            
    		pageContent = uArray.get("description")+"\n";
    		pageTitle = uArray.get("title");
    		pageURL = uArray.get("link");
                updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_word);
                
                updateViews.setTextViewText(R.id.word_title, pageTitle);
                updateViews.setTextViewText(R.id.definition, pageContent);
                updateViews.setTextViewText(R.id.click, "[Click to Read Full Article]");
                                
                Intent defineIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageURL));
                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        0 /* no requestCode */, defineIntent, 0 /* no flags */);
                updateViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
            
                ComponentName thisWidget = new ComponentName(context, AggroWidget.class);
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                manager.updateAppWidget(thisWidget, updateViews);    
                
            return updateViews;
        }
        
        @Override
        public IBinder onBind(Intent intent) {
            // We don't need to bind to this service
            return null;
        }
    }
}
