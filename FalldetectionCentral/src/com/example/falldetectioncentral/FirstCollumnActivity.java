package com.example.falldetectioncentral;

import java.util.ArrayList;

import org.societies.android.api.cis.SocialContract;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class FirstCollumnActivity extends FragmentActivity implements
LoaderManager.LoaderCallbacks<Cursor> {
	
	private Button addCommunity;
	private Context context;
	private ContentResolver resolver;
	private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
	private CursorAdapter adapter;
	private String activeCommunityGlobalID="";
	private String tempElderPesonGlobalID;
	private String myGlobalID;
	private RadioGroup radioGroup;
	private EditText addCommunityDialogName, addCommunityDialogID, addCommunityDialogType, addCommunityDialogPersonDescription, addCommunityDialogPersonEmail, addCommunityDialogDescription, addCommunityDialogOrigin;
	private ArrayList<RelativeLayout> orangedotViews = new ArrayList<RelativeLayout>();
	private TableLayout incommingMessageTablelayout;
	  private ArrayList<communityActivityCountClass> feedList = new ArrayList<communityActivityCountClass>();
	  private int lastPositionListView=-1;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_central_main_mobile);
	        context = this;
	        addCommunity = (Button)findViewById(R.id.addCommunity);
	        incommingMessageTablelayout = (TableLayout)findViewById(R.id.incomming_message_tablelayout);
	       // radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
	        resolver = getContentResolver();
	        activeCommunityGlobalID = getIntent().getStringExtra("ActiveCommunityGlobalID");
	/*        resolver.delete(SocialContract.Communities.CONTENT_URI, null, null);
	        resolver.delete(SocialContract.People.CONTENT_URI, null, null);
	        resolver.delete(SocialContract.CommunityActivity.CONTENT_URI, null, null);
	        resolver.delete(SocialContract.Membership.CONTENT_URI, null, null);
	 */       
	        
	        insertMyself();
	        queryMyself();
	        
	        final ContentObserver communityObserver = new ContentObserver(new Handler()){
	        	
	        	@Override public boolean deliverSelfNotifications() { 
	                return true; 
	                }
	        	
	       	 @Override
	            public void onChange(boolean selfChange) {
	                super.onChange(selfChange);
	            	getSupportLoaderManager().restartLoader(1, null, mCallbacks);
	       	 }
	       };
	       
	       resolver.registerContentObserver(SocialContract.Communities.CONTENT_URI, false, communityObserver);
	       
	       
	       
	       
	       
	       
	       
	       
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
                          					
                          				}
                          				
                          				//Endringen har skjedd i en annen feed enn den aktive
                          				else{
                          					showNotificationOrange(i);
                          				}
                          				
                          				
                          			}
                          			
                          		}
                          		
                          	}
                          	
                          	
                          	comCursor.moveToNext();
                          }
                      	
                 	 }
                 };
                  
          resolver.registerContentObserver(SocialContract.CommunityActivity.CONTENT_URI, false, communityActivityObserver);
	       
	       
	       
	       
	       
          
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
          	feedList.add(new communityActivityCountClass(comCursor.getString(0), counter));
          	
          	
          	comCursor.moveToNext();
          }
          
          
          
          
          
	       
	       
	       
	       
	       
	       
	       
	       
	       
	       
           mCallbacks = this;
           
           String[] from = new String[] { SocialContract.Communities._ID, SocialContract.Communities.NAME};

           adapter = new SimpleCursorAdapter(this,  R.layout.listview_item_layout, null,
                   from, new int[]{R.id.textview, R.id.textview2},
                   0);
           final ListView listView = (ListView) findViewById(R.id.list);
           listView.setAdapter(adapter);
           listView.setOnItemClickListener(new OnItemClickListener() {

   			public void onItemClick(AdapterView<?> adapterView, View view, int position,
   					long id) {
   				lastPositionListView = position;
   				adapterView.requestFocusFromTouch(); // IMPORTANT!
   				adapterView.setSelection(position);
   				    
   				Cursor communityCursor = resolver.query(SocialContract.Communities.CONTENT_URI,
   						new String[] {SocialContract.Communities._ID, SocialContract.Communities.NAME, SocialContract.Communities.TYPE, 
   						SocialContract.Communities.DESCRIPTION}, null, null, null);
   				
   				communityCursor.moveToFirst();
   				communityCursor.move(position);
   				
   				Cursor membershipCursor = resolver.query(SocialContract.Membership.CONTENT_URI, 
   						new String []{SocialContract.Membership._ID_MEMBER}, 
   						SocialContract.Membership._ID_COMMUNITY +"='"+  communityCursor.getString(0)  + "'" + " AND "
   						+ SocialContract.Membership.TYPE +"='eldre'", null, null );
   				membershipCursor.moveToFirst();
   				
   				if (membershipCursor.getCount()>0){
   					tempElderPesonGlobalID = membershipCursor.getString(0);
   					Cursor peopleCursor = resolver.query(SocialContract.People.CONTENT_URI,
   							new String []{SocialContract.People.DESCRIPTION}, SocialContract.People.GLOBAL_ID +"='"+tempElderPesonGlobalID+"'", null,	null);
   					peopleCursor.moveToFirst();
   					
   				}
   				activeCommunityGlobalID = communityCursor.getString(0);
   				view.getFocusables(position);
   				view.setSelected(true);
   			}
   		});
	        
           getSupportLoaderManager().initLoader(1, null, this);
           
           
           addCommunity.setOnClickListener(new OnClickListener() {
   			
   			public void onClick(View v) {
   				
   				AlertDialog.Builder addCommunityDialog = new AlertDialog.Builder(context);
   				final View addCommunityView = LayoutInflater.from(context).inflate(R.layout.create_community_menu_mobile, null, false);
   				addCommunityDialogName = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogName);
   			//	addCommunityDialogID = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogID);
   			//	addCommunityDialogID.setText(myGlobalID);
   				//addCommunityDialogType = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogType);
   				addCommunityDialogDescription = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogDescription);
   				
   				
   				//Setting this hardcoded to "box.com" from now
   				//addCommunityDialogOrigin = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogOrigin);
   			
   				
   				
   			//	addCommunityDialogPersonDescription = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogPersonDescription);
   			//	addCommunityDialogPersonEmail = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogPersonMail);
   				
   				
   			//	addCommunityDialogID.setText("");
   				addCommunityDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {  
   					   public void onClick(DialogInterface dialog, int id) {  
   						    String addCommunityDialogNameString = addCommunityDialogName.getText().toString();
   						//    String addCommunityDialogIDString = addCommunityDialogID.getText().toString();
   						//    int selectedRadioID = radioGroup.getCheckedRadioButtonId();
   						//    RadioButton radioButton = (RadioButton) findViewById(selectedRadioID);
   						    
   					//	    String addCommunityDialogTypeString = radioButton.getText().toString();
   						    String addCommunityDialogDescriptionString = addCommunityDialogDescription.getText().toString();
   						    String addCommunityDialogOriginString = "box.com";
   						    String addCommunityDialogPersonMailString = "ubifall@gmail.com";

   						 //   GLOBAL_ID (needed temporally until we have a working sync adapter)
   						 //   NAME
   						 //   OWNER_ID
   						//    TYPE
   						//    DESCRIPTION (optional, will be set to "na" if not provided)
   						//    ORIGIN 
   						    
   						     ContentValues values = new ContentValues();
   						   //  double communityGlobalID = Math.random();
   						 //    values.put(SocialContract.Communities._ID, communityGlobalID);
   						 //    values.put(SocialContract.Communities._ID, communityGlobalID*100);
   						     values.put(SocialContract.Communities.NAME, addCommunityDialogNameString);
   						     values.put(SocialContract.Communities.OWNER_ID, myGlobalID);
   						     values.put(SocialContract.Communities.ACCOUNT_TYPE, addCommunityDialogOriginString);
   						     values.put(SocialContract.Communities.DESCRIPTION, addCommunityDialogDescriptionString);
   						     values.put(SocialContract.Communities.TYPE, "Fall-detection");
   							 Uri uri = resolver.insert(SocialContract.Communities.CONTENT_URI, values);
   							String communityGlobalID =  uri.getLastPathSegment();
   							 values.clear();
   							 
   							 //TODO bytte ut med counter eller lignende
   						//	 double personGlobalID = Math.random();
   					//		 values.put(SocialContract.People._ID, personGlobalID*100);
   							 values.put(SocialContract.People.NAME, addCommunityDialogNameString);
   							 values.put(SocialContract.People.ACCOUNT_TYPE, addCommunityDialogOriginString);
   							// values.put(SocialContract.People.DESCRIPTION, addCommunityDialogPersonDescriptionString);
   							 values.put(SocialContract.People.EMAIL, addCommunityDialogPersonMailString);
   							 uri = resolver.insert(SocialContract.People.CONTENT_URI, values);
   							 String personGlobalID = uri.getLastPathSegment();
   							 values.clear();
   							 
   						//	 values.put(SocialContract.Membership._ID, Math.random()*100);
   							 values.put(SocialContract.Membership._ID_MEMBER, personGlobalID);
   							 values.put(SocialContract.Membership._ID_COMMUNITY, communityGlobalID);
   							 values.put(SocialContract.Membership.TYPE, "eldre");
   							 values.put(SocialContract.Membership.ACCOUNT_TYPE, addCommunityDialogOriginString);
   							 resolver.insert(SocialContract.Membership.CONTENT_URI, values);
   							 
   							 
   							resolver.notifyChange(SocialContract.Communities.CONTENT_URI, communityObserver);
   							addNotificationOrange();
   						}  
   						}); 
   				addCommunityDialog.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {  
   					   public void onClick(DialogInterface dialog, int id) {  
   						     dialog.cancel(); 
   						}  
   						});  
   				
   		
   				
   				addCommunityDialog.setView(addCommunityView);
   				addCommunityDialog.setTitle("Opprett ett community");
   				addCommunityDialog.show();
   			}
   		});
           
           
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
               
               
               tabs.setOnTabChangedListener(new OnTabChangeListener() {
                  public void onTabChanged(String collumn) {
                	   
                	  if (collumn.equals("secondCollumn")){
                		Intent i = new Intent(getApplicationContext(), SecondCollumnActivity.class);
                		i.putExtra("TabNumber", 1); 
                		i.putExtra("ActiveCommunityGlobalID", activeCommunityGlobalID);
                      	startActivity(i);
                	  }
                	  else if(collumn.equals("thirdCollumn")){
                		  Intent i = new Intent(getApplicationContext(), ThirdCollumnActivity.class);
                		  i.putExtra("TabNumber", 2); 
                		  i.putExtra("ActiveCommunityGlobalID", activeCommunityGlobalID);
                          startActivity(i);
                	  }
                  }     
            });  
               
               
               
               Cursor tempcommunityCursor = resolver.query(SocialContract.Communities.CONTENT_URI, null, null, null, null);
               tempcommunityCursor.moveToFirst();
               
               
               
               int numberOfDots = tempcommunityCursor.getCount();
               drawNotificationOranges(numberOfDots);
              
               
               
           
	 }

	 
	 @Override
		protected void onPause()
		{
		 	super.onPause();
			Log.d("TAG", "%app onPause()");
	        startService(new Intent(this, ServiceClass.class));
		}
	   
	   
		public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
			String[] from;
			switch (id){
	        case 1:
	        	
	        	from = new String[] { SocialContract.Communities.NAME,  SocialContract.Communities._ID};
				   return new CursorLoader(this, SocialContract.Communities.CONTENT_URI,
						   from, null, null, null);
		
			default:
				return null;
			}   
		}
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		int id = loader.getId();
		
		switch(id){
		case 1: 
			  adapter.swapCursor(cursor);
			break;
		default:
			break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		int id = loader.getId();
	    switch(id) {
	        case 1:
	        	 adapter.swapCursor(null);
	        	 break;
	        default:
	            break;
	    }

	}
  
 	 public void queryMyself(){
   		 
   		 Cursor meCursor = resolver.query(SocialContract.Me.CONTENT_URI, null, null, null, null);
   		 meCursor.moveToFirst();
   		 myGlobalID = meCursor.getString(1);
   		 
   		 
   	 }

	 public void insertMyself(){
		 
		 resolver.delete(SocialContract.Me.CONTENT_URI, null, null);
		 
		    
		     ContentValues memberValues = new ContentValues();
		     memberValues.put(SocialContract.Me.GLOBAL_ID, "12345678");
			 memberValues.put(SocialContract.Me.NAME, "Andreas");
			 memberValues.put(SocialContract.Me.ACCOUNT_TYPE, "box.com");
			 memberValues.put(SocialContract.Me.USER_NAME, "andlru");
			 memberValues.put(SocialContract.Me.PASSWORD, "passord");
			 resolver.insert(SocialContract.Me.CONTENT_URI, memberValues);
			 
			 
	 }

	 
	  public void drawNotificationOranges(int number){
	    	
	    	 RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(86,86);
	         LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(70,70);
	         
	         incommingMessageTablelayout.removeAllViews();
	         
	         //Height == 86
	    	for (int i=0;i<number;i++){
	    		 ImageView orangeDot = new ImageView(context);
	             orangeDot.setImageResource(R.drawable.orange_dot2);
	             RelativeLayout rad = new RelativeLayout(context);
	             rad.setLayoutParams(relativeLayoutParams);
	             orangeDot.setLayoutParams(layoutParams);
	             orangeDot.setPadding(0, 25, 0, 10);
	             rad.addView(orangeDot);
	             rad.setVisibility(View.INVISIBLE);
	             orangedotViews.add(rad);
	             incommingMessageTablelayout.addView(rad);
	    	}
	    	
	          
	    }
	    
	    public void addNotificationOrange(){
	    	
	    	
	    	RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(45,45);
	        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(45,45);
	    	
	    	
	    	 ImageView orangeDot = new ImageView(context);
	         orangeDot.setImageResource(R.drawable.orange_dot2);
	         RelativeLayout rad = new RelativeLayout(context);
	         rad.setLayoutParams(relativeLayoutParams);
	         orangeDot.setLayoutParams(layoutParams);
	         orangeDot.setPadding(5, 14, 0, 10);
	         rad.addView(orangeDot);
	         rad.setVisibility(View.INVISIBLE);
	         orangedotViews.add(rad);
	         incommingMessageTablelayout.addView(rad);
	    }
	    
	    public void showNotificationOrange(int number){
	    	orangedotViews.get(number).setVisibility(View.VISIBLE);
	    }
	    
	    public void hideNotificationOrange(int number){
	    	orangedotViews.get(number).setVisibility(View.INVISIBLE);
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
