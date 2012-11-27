package com.example.falldetectioncentral;

import org.societies.android.api.cis.SocialContract;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class ServiceClass extends Service{

	private ContentResolver resolver;
	private Context context;
	private Notification notification; 
	private  NotificationManager mNotificationManager;
	private int icon;
	private CharSequence tickerText = "Falldeteksjon-system";
	private long time;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
		public void onCreate() {
	        super.onCreate();
	        
	        context = this;
	        resolver = getContentResolver();
	        
               
               final ContentObserver communityActivityObserver = new ContentObserver(new Handler()){
                      	
                      	@Override public boolean deliverSelfNotifications() { 
                              return true; 
                              }
                      	
                     	 @Override
                          public void onChange(boolean selfChange) {
                             
                              
                              
                              if (!context.getPackageName().equalsIgnoreCase(((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getPackageName()))
                  	        {
                  	        	//TODO Finne alle nye meldinger siden fallapplikasjonen var åpnet sist. Om noen av disse var alarm. 
                            	  //Hvis alarm, rødt icon. Og fallalarm tekst.
                            	  //Ellers antall meldinger og innholdet i den siste
                            	  //TODO Denne timestamp-metoden for å hente ut siste meldinger vil avhenge av at Creation_time fungerer
                  	        	
                  	        	
                  	        	 String ns = Context.NOTIFICATION_SERVICE;
                 		        mNotificationManager = (NotificationManager) getSystemService(ns);
                 		        
                 		   //     if (!isAlarmOff){
                 		    //    	icon = R.drawable.red_slippery;
                 	    	//	}
                 		   //     else{
                 		        	icon = R.drawable.red_slippery;
                 		    //    }
                 		       	CharSequence text = "Denne er midlertidig";
                 		       	Notification notification = new Notification(icon, text, System.currentTimeMillis());
                 		       	Intent intent = new Intent(context, CentralMainActivity.class); 
                 		    	intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                 		    	 intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                 		       	PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                 		       	                intent, 0);
                 		       	
                 		       	
                 		       	CharSequence contentTitle="Test";
                 		      /* 	if (numberOfUnreadMessages == 1)
                 		       		contentTitle = "1 ny melding";
                 		       	else if (numberOfUnreadMessages > 1){
                 		       		contentTitle = numberOfUnreadMessages +" nye meldinger";
                 		       	}*/
                 		       		
                 		       	notification.setLatestEventInfo(context, contentTitle,
                 		       	      text, contentIntent);
                 		       	mNotificationManager.notify(1, notification);
                 		       	
                 		       	
                 		       	
                 		       	
                 		       	
                  	        }
                              
                              super.onChange(selfChange);
                              
                     	 }
                     };
                      
                     ServiceClass.this.getContentResolver().registerContentObserver (SocialContract.CommunityActivity.CONTENT_URI, true, communityActivityObserver); 
	 }

	 
	 @Override
	 public int onStartCommand(Intent intent, int flags, int startId) {
		 
		time = System.currentTimeMillis();
		 
		 return START_STICKY;

	 }
	 
	 
}
