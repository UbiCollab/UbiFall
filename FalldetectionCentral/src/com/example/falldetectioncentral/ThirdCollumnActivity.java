package com.example.falldetectioncentral;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.societies.android.api.cis.SocialContract;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class ThirdCollumnActivity extends Activity {

	
	private String activeCommunityGlobalID="";
	private Button userOkButton, sendMessageButton;
	private EditText inputMessageDialog;
	private ScrollView scroll;
	private ContentResolver resolver;
	private TableLayout tableLayout;
	private String myGlobalID;
	private int screenWidth;
	private Context context;
	private ArrayList<communityActivityCountClass> feedList = new ArrayList<communityActivityCountClass>();
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_central_third_mobile);
	        context = this;
	        
	        DisplayMetrics displaymetrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	        screenWidth = displaymetrics.widthPixels;
	        
	        scroll = (ScrollView)findViewById(R.id.scrollview);
	        userOkButton = (Button)findViewById(R.id.okButton);
	        sendMessageButton = (Button)findViewById(R.id.sendButton);
	        inputMessageDialog = (EditText)findViewById(R.id.inputDialog);
	        inputMessageDialog.setBackgroundResource(R.drawable.edittext_boxes_selector);
	        tableLayout = (TableLayout) findViewById(R.id.messagesTableLayout);
	        tableLayout = (TableLayout) findViewById(R.id.messagesTableLayout);
	        userOkButton.setVisibility(View.INVISIBLE);	        
	        resolver = getContentResolver();
	        
	        final TabHost tabs = (TabHost) this.findViewById(R.id.my_tabhost);
	           tabs.setup();
	           
	           TabSpec tabSpecCollumnOne = tabs.newTabSpec("firstCollumn");
	           tabSpecCollumnOne.setIndicator("Brukere");
	           tabSpecCollumnOne.setContent(R.id.content);
	           tabs.addTab(tabSpecCollumnOne);
	           
	           TabSpec tabSpecCollumnTwo = tabs.newTabSpec("secondCollumn");
	           tabSpecCollumnTwo.setIndicator("Info");
	           tabSpecCollumnTwo.setContent(R.id.content);
	           tabs.addTab(tabSpecCollumnTwo);
	           
	           
	           TabSpec tabSpecCollumnThree = tabs.newTabSpec("thirdCollumn");
	           tabSpecCollumnThree.setIndicator("Feed");
	           tabSpecCollumnThree.setContent(R.id.content);
	           tabs.addTab(tabSpecCollumnThree);
	           
	           int tabNumber = getIntent().getIntExtra("TabNumber", 0);
	           tabs.setCurrentTab(tabNumber);
	           activeCommunityGlobalID = getIntent().getStringExtra("ActiveCommunityGlobalID");
	               
	               tabs.setOnTabChangedListener(new OnTabChangeListener() {
	                  public void onTabChanged(String collumn) {
	                	   
	                	  if(collumn.equals("firstCollumn")){
	                			Intent i = new Intent(getApplicationContext(), FirstCollumnActivity.class);
	                			i.putExtra("TabNumber", 0); 
	                			i.putExtra("ActiveCommunityGlobalID", activeCommunityGlobalID);
		                      	startActivity(i);
	                	  }
	                	   
	                	  else if (collumn.equals("secondCollumn")){
		                		Intent i = new Intent(getApplicationContext(), SecondCollumnActivity.class);
		                		i.putExtra("TabNumber", 1); 
		                		i.putExtra("ActiveCommunityGlobalID", activeCommunityGlobalID);
		                      	startActivity(i);
	                	  }
	                	  
	                  }     
	            }); 
	               
	               
	               final ContentObserver communityActivityObserver = new ContentObserver(new Handler()){
	                      	
	                      	@Override public boolean deliverSelfNotifications() { 
	                              return true; 
	                              }
	                      	
	                     	 @Override
	                          public void onChange(boolean selfChange) {
	                              super.onChange(selfChange);
	                              
	                              Log.d("APP", "%app app changed");
	                              //Nå må jeg hente inn på nytt. Se om det er endring i noen av feedsene
	                              
	                              
	                              Cursor feedCursor = resolver.query(SocialContract.CommunityActivity.CONTENT_URI,
	                              		new String []{SocialContract.CommunityActivity.OBJECT}, null, null, null);
	                             
	                              
	                              Cursor comCursor = resolver.query(SocialContract.Communities.CONTENT_URI,
	                              		new String []{SocialContract.Communities._ID}, null, null, null);
	                              comCursor.moveToFirst();
	                              
	                              //Alle communities har en feed. Jeg må søke igjennom communities og counte feedsene deres
	                              
	                              
	                              while(!comCursor.isAfterLast()){
	                              	 feedCursor.moveToFirst();
	                              	int counter = 0;
	                              	while (!feedCursor.isAfterLast()){
	                              		
	                              		//Dette betyr at det er en treff på en feedpost
	                              		if (feedCursor.getString(0).equals(comCursor.getString(0))){
	                              			counter++;
	                              		}
	                              		
	                              		
	                              		feedCursor.moveToNext();
	                              	}
	                              	
	                              	for (int i=0;i<feedList.size();i++){
	                              		
	                              		if(feedList.get(i).getGlobal_id().equals(comCursor.getString(0))){
	                              			
	                              			int diff = counter - feedList.get(i).getCounter();
	                              			if(diff != 0){
	                              				
	                              				if (comCursor.getString(0).equals(activeCommunityGlobalID)){
	                              					 setupCommunityMessages();
	                              				}
	                              				
	                              				//Endringen har skjedd i en annen feed enn den aktive
	                              				else{
	                              					//TODO Legge inn den orange dotten 
	                              				}
	                              				
	                              				
	                              			}
	                              			
	                              		}
	                              		
	                              	}
	                              	
	                              	
	                              	comCursor.moveToNext();
	                              }
	                          	
	                     	 }
	                     };
	                      
	              resolver.registerContentObserver(SocialContract.CommunityActivity.CONTENT_URI, false, communityActivityObserver);
	                      
	                      
	               
	               
	               
	               sendMessageButton.setOnClickListener(new OnClickListener() {
	       			
	       			public void onClick(View v) {
	       				
	       				
	       				if (activeCommunityGlobalID.length()>0 && inputMessageDialog.getText().toString().length() > 0 ){
	       					
	       					String input = inputMessageDialog.getText().toString();
	       					
	       					if (input.length() > 0){
	       					
	       						Cursor activeCommunityCursor = resolver.query(SocialContract.Communities.CONTENT_URI,
	       								new String []{SocialContract.Communities.OWNER_ID}, 
	       								SocialContract.Communities._ID +"='"+activeCommunityGlobalID+"'", null, null);
	       						activeCommunityCursor.moveToFirst();
	       						
	       						
	       						ContentValues values = new ContentValues();
	       						values.put(SocialContract.CommunityActivity._ID, Math.random());
	       						values.put(SocialContract.CommunityActivity.ACTOR, myGlobalID);      //SenderGlobalID
	       						values.put(SocialContract.CommunityActivity._ID_FEED_OWNER, activeCommunityCursor.getString(0)); //owner ID
	       						values.put(SocialContract.CommunityActivity.OBJECT, activeCommunityGlobalID);	  //Community Global ID
	       						values.put(SocialContract.CommunityActivity.TARGET, 1);     //Type melding: Vanlig (1), Warning (2) eller Alarm (3)
	       						values.put(SocialContract.CommunityActivity.VERB, input);       //Teksten
	       						values.put(SocialContract.CommunityActivity.ACCOUNT_TYPE, "box.com");
	       						resolver.insert(SocialContract.CommunityActivity.CONTENT_URI, values);
	       						setupCommunityMessages();
	       						inputMessageDialog.setText("");
	       						
	       						scroll.post(new Runnable() {            
	       						    public void run() {
	       						    	scroll.fullScroll(View.FOCUS_DOWN);              
	       						    }
	       						});
	       						
	       						//TODO HUSK DETTE
	       						//Så hvis owner ID og SenderGlobalID er likt blir det mottatt som en sentralmelding
	       						//Om det er ulikt mottas det som en vanlig melding
	       						//Om det er ulikt, men ACTOR er det samme som myGlobalId settes det som en egenmelding 
	       						
	       					}
	       				
	       				}
	       			}
	       		});
	               
	               
	           //    insertMyself();
	               queryMyself();
	               
	               
	               setupCommunityMessages();
	               
	               
	 }

	 
	 @Override
		protected void onPause()
		{
		 	super.onPause();
			Log.d("TAG", "%app onPause()");
	        startService(new Intent(this, ServiceClass.class));
		}
	 
	 
 	 public void queryMyself(){
   		 
   		 Cursor meCursor = resolver.query(SocialContract.Me.CONTENT_URI, null, null, null, null);
   		 meCursor.moveToFirst();
   		 myGlobalID = meCursor.getString(1);
   		 
   		 
   	 }

	 public void insertMyself(){
		 
		 resolver.delete(SocialContract.Me.CONTENT_URI, null, null);
		 
		    
		     ContentValues memberValues = new ContentValues();
		     memberValues.put(SocialContract.Me._ID, "12345678");
			 memberValues.put(SocialContract.Me.NAME, "Andreas");
			 memberValues.put(SocialContract.Me.ACCOUNT_TYPE, "box.com");
			 memberValues.put(SocialContract.Me.USER_NAME, "andlru");
			 memberValues.put(SocialContract.Me.PASSWORD, "passord");
			 resolver.insert(SocialContract.Me.CONTENT_URI, memberValues);
			 
			 
	 }		
	 
	 public void setupCommunityMessages(){
	    	
	    	
	    	tableLayout.removeAllViews();

	    	  drawUserOKMessage("Brukeren er OK.", System.currentTimeMillis());
	    	  drawAlarm("Alarm! Systemet har oppdaget et fall!", System.currentTimeMillis());
	    	  drawWarning("Advarsel! Litt usikker på hva denne skal si", System.currentTimeMillis());
	    	  drawOther("Denne er en melding fra Tor", "Tor", System.currentTimeMillis());
	    	  
	    	//Sortere etter dato?
	    	Cursor communityActivityCursor = resolver.query(SocialContract.CommunityActivity.CONTENT_URI, 
	    			new String []{SocialContract.CommunityActivity.ACTOR, SocialContract.CommunityActivity.VERB, 
	    			SocialContract.CommunityActivity.CREATION_DATE, SocialContract.CommunityActivity.GLOBAL_ID_FEED_OWNER, SocialContract.CommunityActivity.TARGET}, 
	    			SocialContract.CommunityActivity.OBJECT + "='"+activeCommunityGlobalID+"'", null, null);
	    	communityActivityCursor.moveToFirst();
	    	
	    	
	    	while (!communityActivityCursor.isAfterLast()){
	    		
	    		//TODO Bytte ut dato riktig
	    		String sender = communityActivityCursor.getString(0);
	    		String tekst = communityActivityCursor.getString(1);
	    		String dato = communityActivityCursor.getString(2);
	    		String communityOwner = communityActivityCursor.getString(3);
	    		String typeMessage = communityActivityCursor.getString(4);
	    		
	    		if (typeMessage.equals("2"))
	    			drawWarning(tekst, System.currentTimeMillis());
	    		
	    		else if(typeMessage.equals("3"))
	    			drawAlarm(tekst, System.currentTimeMillis());
	    		
	    		else{ 
	    		
		    		//JEG er sender
		    		if (sender.equals(myGlobalID)){
		    			
		    			
		    			
		    			
		    			
		    			//JEG er community eier
		    			if (sender.equals(communityOwner))
		    				drawSentralMessage(tekst, System.currentTimeMillis());
						
		    				
		    			else{
		    				drawMine(tekst, System.currentTimeMillis());
		    			}
		    			
		    			
		    		}
		    		
		    		//Det er en annen sender
		    		else{
		    			
		    			//SENDEREN er community eier
		    			if (sender.equals(communityOwner))
		    				drawSentralMessage(tekst, System.currentTimeMillis());
		    			else{	
		    				drawOther(tekst, sender, System.currentTimeMillis());
		    			}
		    		}
	    		
	    		}
	    		
	    		communityActivityCursor.moveToNext();
	    	}
	    	
	    	scroll.post(new Runnable() {            
			    public void run() {
			    	scroll.fullScroll(View.FOCUS_DOWN);              
			    }
			});
	    }
	 
	 
	 public void drawMine(String message, long dateInput){
	    	
	    	
	    	TableLayout tempTableLayout = new TableLayout(context);
	    	
	    	
	     	 TableRow signedRow = new TableRow(context);
	        TextView signedText = new TextView(context);
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yy, HH:mm");
	        Date date = new Date(dateInput);
	        signedText.setText(dateFormat.format(date));
	        signedText.setTextSize(12);
	        signedText.setPadding(5, 5, 5, 5);
	        
	        TableRow messageRow = new TableRow(context);
	        TextView messageText = new TextView(context);
	        messageText.setText(message);
	        messageText.setPadding(5, 5, 5, 5);
	        messageText.setWidth(screenWidth-210);
	        messageText.setTextSize(18);
	      
	        signedRow.setGravity(Gravity.LEFT | Gravity.TOP);
	        messageRow.setGravity(Gravity.LEFT | Gravity.TOP);
	        
	        signedRow.addView(signedText);
	        messageRow.addView(messageText);
	        
	        
	        TableLayout.LayoutParams tablelayoutParams = new TableLayout.LayoutParams
	                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        
	        tablelayoutParams.setMargins(0, 0, 180, 15);
	        tempTableLayout.setLayoutParams(tablelayoutParams);
	        tempTableLayout.setBackgroundResource(R.layout.chat_borders);
	        tempTableLayout.addView(messageRow);
	        tempTableLayout.addView(signedRow);
	        
	        tableLayout.addView(tempTableLayout);

	      
	        
	   }
	    
	   public void drawOther(String message, String from, long dateInput){
		   
		   
		   TableLayout tempTableLayout = new TableLayout(context);
	    	
	    	
	   	
	   	 TableRow signedRow = new TableRow(context);
	        TextView signedText = new TextView(context);
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yy, HH:mm");
	        Date date = new Date(dateInput);
	        signedText.setText(from + " - " + dateFormat.format(date));
	        signedText.setTextSize(12);
	        signedText.setPadding(5, 5, 5, 5);
	        
	        TableRow messageRow = new TableRow(context);
	        TextView messageText = new TextView(context);
	        messageText.setText(message);
	        messageText.setPadding(5, 5, 5, 5);
	        messageText.setWidth(screenWidth-210);
	        messageText.setTextSize(18);
	        
	        signedRow.setGravity(Gravity.LEFT | Gravity.TOP);
			   messageRow.setGravity(Gravity.LEFT | Gravity.TOP);
			   
			   
	        signedRow.addView(signedText);
	        messageRow.addView(messageText);
	        
	        
	        TableLayout.LayoutParams tablelayoutParams = new TableLayout.LayoutParams
	                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        
	        tablelayoutParams.setMargins(180, 0, 0, 15);
	        tempTableLayout.setLayoutParams(tablelayoutParams);
	        tempTableLayout.setBackgroundResource(R.layout.chat_borders);
	        tempTableLayout.addView(messageRow);
	        tempTableLayout.addView(signedRow);
	        
	        tableLayout.addView(tempTableLayout);
	        
	   }
	   
	   public void drawWarning(String warning, long dateInput){
	   	
		 	TableLayout tempTableLayout = new TableLayout(context);
			
			
			 TableRow signedRow = new TableRow(context);
		   TextView signedText = new TextView(context);
		   SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yy, HH:mm");
		   Date date = new Date(dateInput);
		   signedText.setText(dateFormat.format(date));
		   signedText.setTextSize(12);
		   signedText.setPadding(5, 5, 5, 5);
		   
		   TableRow messageRow = new TableRow(context);
		   TextView messageText = new TextView(context);
		   messageText.setText(warning);
		   messageText.setPadding(5, 5, 5, 5);
		   messageText.setWidth(screenWidth-210);
		   messageText.setTextSize(18);
		 
		   signedRow.setGravity(Gravity.LEFT | Gravity.TOP);
		   messageRow.setGravity(Gravity.LEFT | Gravity.TOP);
		   
		   signedRow.addView(signedText);
		   messageRow.addView(messageText);
		   
		   
		   TableLayout.LayoutParams tablelayoutParams = new TableLayout.LayoutParams
		           (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		   
		   tablelayoutParams.setMargins(90, 0, 90, 15);
		   tempTableLayout.setLayoutParams(tablelayoutParams);
		   tempTableLayout.setBackgroundResource(R.layout.chat_borders_warning);
		   tempTableLayout.addView(messageRow);
		   tempTableLayout.addView(signedRow);
		   
		   tableLayout.addView(tempTableLayout);
	       
	        
	   }
	   
	   public void drawAlarm(String alarm, long dateInput){
	   	
		   	TableLayout tempTableLayout = new TableLayout(context);
		   TableRow signedRow = new TableRow(context);
		   TextView signedText = new TextView(context);
		   SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yy, HH:mm");
		   Date date = new Date(dateInput);
		   signedText.setText(dateFormat.format(date));
		   signedText.setTextSize(12);
		   signedText.setPadding(5, 5, 5, 5);
		   
		   TableRow messageRow = new TableRow(context);
		   TextView messageText = new TextView(context);
		   messageText.setText(alarm);
		   messageText.setPadding(5, 5, 5, 5);
		   messageText.setWidth(screenWidth-210);
		   messageText.setTextSize(18);
		 
		   signedRow.setGravity(Gravity.LEFT | Gravity.TOP);
		   messageRow.setGravity(Gravity.LEFT | Gravity.TOP);
		   
		   signedRow.addView(signedText);
		   messageRow.addView(messageText);
		   
		   
		   TableLayout.LayoutParams tablelayoutParams = new TableLayout.LayoutParams
		           (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		   
		   tablelayoutParams.setMargins(90, 0, 90, 15);
		   tempTableLayout.setLayoutParams(tablelayoutParams);
		   tempTableLayout.setBackgroundResource(R.layout.chat_borders_alarm);
		   tempTableLayout.addView(messageRow);
		   tempTableLayout.addView(signedRow);
		   
		   tableLayout.addView(tempTableLayout);
	        
	   }
	   
	   public void drawSentralMessage(String message, long dateInput){
	   	
	   	TableLayout tempTableLayout = new TableLayout(context);
		
		
		 TableRow signedRow = new TableRow(context);
	   TextView signedText = new TextView(context);
	   SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yy, HH:mm");
	   Date date = new Date(dateInput);
	   signedText.setText(dateFormat.format(date));
	   signedText.setTextSize(12);
	   signedText.setPadding(5, 5, 5, 5);
	   
	   TableRow messageRow = new TableRow(context);
	   TextView messageText = new TextView(context);
	   messageText.setText(message);
	   messageText.setPadding(5, 5, 5, 5);
	   messageText.setWidth(screenWidth-210);
	   messageText.setTextSize(18);
	 
	   signedRow.setGravity(Gravity.LEFT | Gravity.TOP);
	   messageRow.setGravity(Gravity.LEFT | Gravity.TOP);
	   
	   signedRow.addView(signedText);
	   messageRow.addView(messageText);
	   
	   
	   TableLayout.LayoutParams tablelayoutParams = new TableLayout.LayoutParams
	           (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	   
	   tablelayoutParams.setMargins(0, 0, 180, 15);
	   tempTableLayout.setLayoutParams(tablelayoutParams);
	   tempTableLayout.setBackgroundResource(R.layout.chat_borders);
	   tempTableLayout.addView(messageRow);
	   tempTableLayout.addView(signedRow);
	   
	   tableLayout.addView(tempTableLayout);

	        
	   }
	   
	   public void drawUserOKMessage(String message, long dateInput){
		   
		   	TableLayout tempTableLayout = new TableLayout(context);
			
			
			 TableRow signedRow = new TableRow(context);
		   TextView signedText = new TextView(context);
		   SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yy, HH:mm");
		   Date date = new Date(dateInput);
		   signedText.setText(dateFormat.format(date));
		   signedText.setTextSize(12);
		   signedText.setPadding(5, 5, 5, 5);
		   
		   TableRow messageRow = new TableRow(context);
		   TextView messageText = new TextView(context);
		   messageText.setText(message);
		   messageText.setPadding(5, 5, 5, 5);
		   messageText.setWidth(screenWidth-210);
		   messageText.setTextSize(18);
		 
		   signedRow.setGravity(Gravity.LEFT | Gravity.TOP);
		   messageRow.setGravity(Gravity.LEFT | Gravity.TOP);
		   
		   signedRow.addView(signedText);
		   messageRow.addView(messageText);
		   
		   
		   TableLayout.LayoutParams tablelayoutParams = new TableLayout.LayoutParams
		           (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		   
		   tablelayoutParams.setMargins(90, 0, 90, 15);
		   tempTableLayout.setLayoutParams(tablelayoutParams);
		   tempTableLayout.setBackgroundResource(R.layout.chat_borders_userok);
		   tempTableLayout.addView(messageRow);
		   tempTableLayout.addView(signedRow);
		   
		   tableLayout.addView(tempTableLayout);
	       
	  }
	   
	   
		private class communityActivityCountClass{
			private String global_id;
			private int counter;
			
			public communityActivityCountClass(String global_id, int counter){
				this.setGlobal_id(global_id);
				this.setCounter(counter);
			}
			public void incrementCounter(){
				counter++;
			}

			public void decrementCounter(){
				counter--;
			}
			
			public int getCounter() {
				return counter;
			}

			public void setCounter(int counter) {
				this.counter = counter;
			}

			public String getGlobal_id() {
				return global_id;
			}

			public void setGlobal_id(String global_id) {
				this.global_id = global_id;
			}
			
			
		}
		
}
