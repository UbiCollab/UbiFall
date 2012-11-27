package com.example.falldetectioncentral;

import java.text.SimpleDateFormat;
import org.societies.android.api.cis.SocialContract;
import org.societies.android.api.cis.SocialContract.UriPathIndex;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CentralMainActivity extends FragmentActivity implements
LoaderManager.LoaderCallbacks<Cursor> {
	private TableLayout associates, secondCollumn, thirdCollumn;
	private EditText name, type, description, aboutPerson, phone;
	private Context context;
	private ScrollView scroll;
	private int width = 0;
	private Button userOkButton, addCommunity, addCommunityDialogOK, addCommunityDialogCancel, addPerson, editCommunity, deleteCommunity,
	saveCommunity, cancelCommunity, sendMessageButton;
	private TableLayout tableLayout, incommingMessageTablelayout;
	private EditText addCommunityDialogName, addCommunityDialogID, addCommunityDialogType, addCommunityDialogDescription, addCommunityDialogOrigin,
	addPersonDialogName, addPersonDialogOrigin, addPersonDialogGlobalID, addPersonDialogDescription, addPersonDialogMail, addPersonDialogType,
	associateEditName, associateEditGlobalID, associateEditType, associateEditDescription, associateEditEmail, associateEditOrigin, inputMessageDialog, 
	addCommunityDialogPersonDescription, addCommunityDialogPersonEmail;

	private String tempCommunityName, tempType, tempDescription, tempAboutPerson, tempElderPesonGlobalID;
	private ListView associateListView;
	private ListView thislist;
	// Coords on top in mat
	private OnLongPressCommunityLayout onLongPressCommunityLayout;
	private AlertDialog addPersonDialogBox;
	private String associateListViewItemClickedGlobalID;
	private LinearLayout.LayoutParams layoutParams;
	private LinearLayout ll;
	private LinearLayout mainLayout;
	private PopupWindow popUp;
	private LinearLayout layout;
	private int selectedCommunityIndex = -1;
	
	private boolean popupIsShowing = false;
	private TextView tv;
	private LayoutParams params;
    private ArrayList<LoaderInfo> loaderList = new ArrayList<LoaderInfo>();
    private int loaderCounter = 1;
    private ArrayList<communityActivityCountClass> feedList = new ArrayList<communityActivityCountClass>();
	    
    private Cursor activeAssociateCursor;
    private LinearLayout backgroundLayout;
	private String activeCommunityGlobalID="";
	
	  // The callbacks through which we will interact with the LoaderManager.
	  private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

	private CursorAdapter adapter, associateListAdapter;
	private ArrayList<RelativeLayout> orangedotViews = new ArrayList<RelativeLayout>();
	private static final int LOADER_ID = 1;
	private boolean isTablet;
	private View lastCommunitySelectedItemView;
	private ContentResolver resolver;
	private String myGlobalID;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO om den er bredere enn høy --> tablet ellers mobil
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        
        Log.d("Height", "%measure h: " + height);
        Log.d("Width", "%measure w: " + width);
        
        if(width>height){
        	isTablet = true;
        	setContentView(R.layout.activity_central_main);
        }
        else{
        	 Log.d("Height", "%measure else");
        	isTablet = false;
        	Intent i = new Intent(getApplicationContext(), FirstCollumnActivity.class);
        	startActivity(i);
        	Log.d("Height", "%measure end of else");
        }
        
        Log.d("Height", "%measure after else");
        
        //TODO Burde ikke trenge denne
        if (isTablet){
        context = this;
        
        //Get XML resources
        
        name = (EditText)findViewById(R.id.name);
        name.setFocusable(false);
        type = (EditText)findViewById(R.id.type);
        type.setFocusable(false);
        description = (EditText)findViewById(R.id.description);
        description.setFocusable(false);
        aboutPerson = (EditText)findViewById(R.id.personDescription);
        aboutPerson.setFocusable(false);
        phone = (EditText)findViewById(R.id.phone);
        phone.setFocusable(false);
        scroll = (ScrollView)findViewById(R.id.scrollview);
        userOkButton = (Button)findViewById(R.id.okButton);
        addCommunity = (Button)findViewById(R.id.addCommunity);
        addPerson = (Button)findViewById(R.id.addPerson);
        editCommunity = (Button)findViewById(R.id.button_change_community_info);
        deleteCommunity = (Button)findViewById(R.id.button_delete_community);
        onLongPressCommunityLayout = new OnLongPressCommunityLayout(this);
        saveCommunity = (Button)findViewById(R.id.change_community_save);
        cancelCommunity = (Button)findViewById(R.id.change_community_cancel);
        saveCommunity.setVisibility(View.INVISIBLE);
        cancelCommunity.setVisibility(View.INVISIBLE);
        secondCollumn = (TableLayout)findViewById(R.id.secondCollumn);
        sendMessageButton = (Button)findViewById(R.id.sendButton);
        inputMessageDialog = (EditText)findViewById(R.id.inputDialog);
        incommingMessageTablelayout = (TableLayout)findViewById(R.id.incomming_message_tablelayout);
        thislist = (ListView)findViewById(R.id.list);
        backgroundLayout = (LinearLayout)findViewById(R.id.firstLinearLayout);
        backgroundLayout.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
				return true;			}
		});
        scroll.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
				return false;
			}
		});
        
      
        
        
        
        
        
        
        setEditBoxesNotFocusableBackground();

        resolver = getContentResolver();
        
    /*    resolver.delete(SocialContract.Communities.CONTENT_URI, null, null);
        resolver.delete(SocialContract.People.CONTENT_URI, null, null);
        resolver.delete(SocialContract.CommunityActivity.CONTENT_URI, null, null);
        resolver.delete(SocialContract.Membership.CONTENT_URI, null, null);*/
        
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
       
        
        final ContentObserver membershipObserver = new ContentObserver(new Handler()){
        	
        	@Override public boolean deliverSelfNotifications() { 
                return true; 
                }
        	
       	 @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
            	getSupportLoaderManager().restartLoader(2, null, mCallbacks);
       	 }
       };
        
        resolver.registerContentObserver(SocialContract.Membership.CONTENT_URI, false, membershipObserver);
        
        
 final ContentObserver communityActivityObserver = new ContentObserver(new Handler()){
        	
        	@Override public boolean deliverSelfNotifications() { 
                return true; 
                }
        	
       	 @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                
                
                //Nå må jeg hente inn på nytt. Se om det er endring i noen av feedsene
                
                
                Cursor feedCursor = resolver.query(SocialContract.CommunityActivity.CONTENT_URI,
                		new String []{SocialContract.CommunityActivity.OBJECT}, null, null, null);
               
                
                Cursor comCursor = resolver.query(SocialContract.Communities.CONTENT_URI,
                		new String []{SocialContract.Communities.GLOBAL_ID}, null, null, null);
                comCursor.moveToFirst();
                
                //Alle communities har en feed. Jeg må søke igjennom communities og counte feedsene deres
                //TODO Egentlig burde jeg bare sjekke timestamps
                
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
				
				selectedCommunityIndex = (int) id;
				//TODO Må skifte til aktiv associtate og feed loader
				
				Log.d("ListView", "%list Listitem: "+position+" clicked!");
				
				adapterView.requestFocusFromTouch(); // IMPORTANT!
				adapterView.setSelection(position);
				
				if (lastCommunitySelectedItemView != null)
					lastCommunitySelectedItemView.setBackgroundResource(R.drawable.listitem_normal);
				    
				view.setBackgroundResource(R.drawable.listitem_selected);
				
				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
			
				Cursor communityCursor = resolver.query(SocialContract.Communities.CONTENT_URI,
						new String[] {SocialContract.Communities.GLOBAL_ID, SocialContract.Communities.NAME, SocialContract.Communities.TYPE, 
						SocialContract.Communities.DESCRIPTION}, null, null, null);
				
				
				communityCursor.moveToFirst();
				communityCursor.move(position);
				name.setFocusable(false);
				name.setText(communityCursor.getString(1));
				type.setFocusable(false);
				type.setText(communityCursor.getString(2));
				description.setFocusable(false);
				description.setText(communityCursor.getString(3));
				
				Log.d("", "%comm GlobalIDCommunity: " + communityCursor.getString(0) +"   name: "+ communityCursor.getString(1));
				Cursor membershipCursor = resolver.query(SocialContract.Membership.CONTENT_URI, 
						new String []{SocialContract.Membership.GLOBAL_ID_MEMBER}, 
						SocialContract.Membership.GLOBAL_ID_COMMUNITY +"='"+  communityCursor.getString(0)  + "'" + " AND "
						+ SocialContract.Membership.TYPE +"='eldre'", null, null );
				membershipCursor.moveToFirst();
				
				if (membershipCursor.getCount()>0){
					associateListView.setVisibility(View.VISIBLE);
					tempElderPesonGlobalID = membershipCursor.getString(0);
					Cursor peopleCursor = resolver.query(SocialContract.People.CONTENT_URI,
							new String []{SocialContract.People.DESCRIPTION}, SocialContract.People.GLOBAL_ID +"='"+tempElderPesonGlobalID+"'", null,	null);
					peopleCursor.moveToFirst();
					
					aboutPerson.setFocusable(false);
					aboutPerson.setText(peopleCursor.getString(0));
				}
				else{
					associateListView.setVisibility(View.INVISIBLE);
				}
				activeCommunityGlobalID = communityCursor.getString(0);
			//	view.getFocusables((int) id);
			//	view.getFocusables(position);
			//	view.setSelected(true);
			//	view.setBackgroundColor(Color.LTGRAY);
				lastCommunitySelectedItemView = view;
				
				getSupportLoaderManager().restartLoader(2, null, mCallbacks);
				setupCommunityMessages();
			}
		});
        
        setupAssociate();
        
       
        //Initializing the loaders. 
        getSupportLoaderManager().initLoader(1, null, this);
        getSupportLoaderManager().initLoader(2, null, this);
        
        
        Cursor tempcommunityCursor = resolver.query(SocialContract.Communities.CONTENT_URI, null, null, null, null);
        tempcommunityCursor.moveToFirst();
        
        
        
        int numberOfDots = tempcommunityCursor.getCount();
        drawNotificationOranges(numberOfDots);
       
        
        addCommunity.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//Name
				//Owner ID
				//TYPE
				//Description
				//ORIGIN (box.com)
				
				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
			
				
				
				
				AlertDialog.Builder addCommunityDialog = new AlertDialog.Builder(context);
				final View addCommunityView = LayoutInflater.from(context).inflate(R.layout.create_community_menu, null, false);
				addCommunityDialogName = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogName);
				addCommunityDialogID = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogID);
				addCommunityDialogID.setText(myGlobalID);
				addCommunityDialogType = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogType);
				addCommunityDialogDescription = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogDescription);
				addCommunityDialogOrigin = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogOrigin);
				addCommunityDialogPersonDescription = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogPersonDescription);
				addCommunityDialogPersonEmail = (EditText)addCommunityView.findViewById(R.id.addCommunityDialogPersonMail);
				
				
				addCommunityDialogID.setText("");
				addCommunityDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {  
					   public void onClick(DialogInterface dialog, int id) {  
						    String addCommunityDialogNameString = addCommunityDialogName.getText().toString();
						    String addCommunityDialogIDString = addCommunityDialogID.getText().toString();
						    String addCommunityDialogTypeString = addCommunityDialogType.getText().toString();
						    String addCommunityDialogDescriptionString = addCommunityDialogDescription.getText().toString();
						    String addCommunityDialogOriginString = addCommunityDialogOrigin.getText().toString();
						    String addCommunityDialogPersonDescriptionString = addCommunityDialogPersonDescription.getText().toString();
						    String addCommunityDialogPersonMailString = addCommunityDialogPersonEmail.getText().toString();

						 //   GLOBAL_ID (needed temporally until we have a working sync adapter)
						 //   NAME
						 //   OWNER_ID
						//    TYPE
						//    DESCRIPTION (optional, will be set to "na" if not provided)
						//    ORIGIN 
						    
						     ContentValues values = new ContentValues();
						     double communityGlobalID = Math.random();
						     values.put(SocialContract.Communities.GLOBAL_ID, communityGlobalID);
						     values.put(SocialContract.Communities.NAME, addCommunityDialogNameString);
						     values.put(SocialContract.Communities.OWNER_ID, addCommunityDialogIDString);
						     values.put(SocialContract.Communities.ORIGIN, addCommunityDialogOriginString);
						     values.put(SocialContract.Communities.DESCRIPTION, addCommunityDialogDescriptionString);
						     values.put(SocialContract.Communities.TYPE, addCommunityDialogTypeString);
							 resolver.insert(SocialContract.Communities.CONTENT_URI, values);
							 
							 values.clear();
							 
							 double personGlobalID = Math.random();
							 values.put(SocialContract.People.GLOBAL_ID, personGlobalID);
							 values.put(SocialContract.People.NAME, addCommunityDialogNameString);
							 values.put(SocialContract.People.ORIGIN, addCommunityDialogOriginString);
							 values.put(SocialContract.People.DESCRIPTION, addCommunityDialogPersonDescriptionString);
							 values.put(SocialContract.People.EMAIL, addCommunityDialogPersonMailString);
							 resolver.insert(SocialContract.People.CONTENT_URI, values);
							 
							 values.clear();
							 
							 values.put(SocialContract.Membership.GLOBAL_ID, Math.random());
							 values.put(SocialContract.Membership.GLOBAL_ID_MEMBER, personGlobalID);
							 values.put(SocialContract.Membership.GLOBAL_ID_COMMUNITY, communityGlobalID);
							 values.put(SocialContract.Membership.TYPE, "eldre");
							 values.put(SocialContract.Membership.ORIGIN, addCommunityDialogOriginString);
							 resolver.insert(SocialContract.Membership.CONTENT_URI, values);
							 
							 addNotificationOrange();
							resolver.notifyChange(SocialContract.Communities.CONTENT_URI, communityObserver);
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
        
        
        addPerson.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
			
				
				if (selectedCommunityIndex != -1){
				
				
				
					AlertDialog.Builder addPersonDialog = new AlertDialog.Builder(context);
					final View addPersonView = LayoutInflater.from(context).inflate(R.layout.add_person_first_menu, null, false);
					Button addNewPerson = (Button)addPersonView.findViewById(R.id.addNewPerson);
					TableLayout tableLayout = (TableLayout)addPersonView.findViewById(R.id.addPersonTable);
	
					
					// 1) Hente xml skjema
					// 2) dynamisk legge inn checkboxer med alle eksisterende personer som ikke er i den aktive Communitien
					
					
					Cursor peopleInCommunityCursor = resolver.query(SocialContract.Membership.CONTENT_URI,
							new String []{SocialContract.Membership.GLOBAL_ID_MEMBER, SocialContract.Membership.TYPE}, 
							SocialContract.Membership.GLOBAL_ID_COMMUNITY +"='"+activeCommunityGlobalID+"'", null, null);
					peopleInCommunityCursor.moveToFirst();
					
					
					Cursor allPeople = resolver.query(SocialContract.People.CONTENT_URI, new String []{SocialContract.People.GLOBAL_ID, SocialContract.People.NAME}, 
							null, null, null);
					allPeople.moveToFirst();
					
					Cursor membershipCursor = resolver.query(SocialContract.Membership.CONTENT_URI, 
							new String[]{SocialContract.Membership.GLOBAL_ID_MEMBER, SocialContract.Membership.TYPE}, null, null, null);
					membershipCursor.moveToFirst();
					
					Log.d("allPeople", "%ppl count: "+ allPeople.getCount());
					ArrayList<People>peopleToShow = new ArrayList<People>();
					//Kjører igjennom alle folk
					while(!allPeople.isAfterLast()){
						peopleToShow.add(new People(allPeople.getString(1), allPeople.getString(0)));
						Boolean deleted = false;
						//Kjører igjennom folk i den active communitien
						while(!peopleInCommunityCursor.isAfterLast()){
							
							//Fjerner fra alle folk dem som er i communitien
							if (peopleInCommunityCursor.getString(0).equals(allPeople.getString(0))){
								peopleToShow.remove(peopleToShow.size()-1);
								peopleInCommunityCursor.moveToFirst();
								deleted = true;
								break;
							}
							
							peopleInCommunityCursor.moveToNext();
						}
						if (!deleted){
						
							while(!membershipCursor.isAfterLast()){
								
								if(membershipCursor.getString(0).equals(allPeople.getString(0)) || membershipCursor.getString(1).equals("eldre")){
									//TODO Mulig denne blir feil. Sjekk i morra. 
									peopleToShow.remove(peopleToShow.size()-1);
									membershipCursor.moveToFirst();
									break;
								}
								
								
								membershipCursor.moveToNext();
							}
						//PROBLEMET er at man får People som er eldre med. 
						}
						//LØSNING Må søke igjennom alle Memberships og se om Personens globale ID henger sammen med eldre. 
						
						
						peopleInCommunityCursor.moveToFirst();
						allPeople.moveToNext();
					}
					
					
					
					final ArrayList<CheckBoxItem> checkBoxes = new ArrayList<CheckBoxItem>();
					for (int i=0;i<peopleToShow.size();i++){
						
						TableRow tableRow = new TableRow(context);
						CheckBox checkBox = new CheckBox(context);
						final EditText editText = new EditText(context);
						editText.setWidth(300);
						editText.setPadding(15, 0, 15, 0);
						editText.setBackgroundColor(Color.LTGRAY);
						editText.setFocusable(false);
						
						checkBox.setText(peopleToShow.get(i).getName());
						checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								
								if (isChecked){
									editText.setBackgroundColor(Color.WHITE);
									editText.setFocusableInTouchMode(true);
								}
								else{
									editText.setBackgroundColor(Color.LTGRAY);
									editText.setFocusableInTouchMode(false);
								}
								
							}
						});
						
						tableRow.addView(checkBox);
						tableRow.addView(editText);
						checkBoxes.add(new CheckBoxItem(checkBox, peopleToShow.get(i).getGlobalID(), editText));
						tableLayout.addView(tableRow);
						
					}
					
					
					addPersonDialog.setPositiveButton("Bekreft", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							
							
							for (int i=0;i<checkBoxes.size();i++){
								
								if (checkBoxes.get(i).getCheckBox().isChecked()){
									
									ContentValues values = new ContentValues();
									values.put(SocialContract.Membership.GLOBAL_ID_COMMUNITY, activeCommunityGlobalID);
									values.put(SocialContract.Membership.GLOBAL_ID, Math.random());
									values.put(SocialContract.Membership.GLOBAL_ID_MEMBER, checkBoxes.get(i).getGlobal_ID());
									values.put(SocialContract.Membership.ORIGIN, "box.com");
									values.put(SocialContract.Membership.TYPE, checkBoxes.get(i).getType().getText().toString());
									
									
									resolver.insert(SocialContract.Membership.CONTENT_URI, values);
									
								}
								
							}
							
							resolver.notifyChange(SocialContract.Membership.CONTENT_URI, membershipObserver);
						}
					});
					
					addPersonDialog.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel(); 
						}
					});
					
					addNewPerson.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							
							addPersonDialogBox.dismiss();
							AlertDialog.Builder addPersonDialog = new AlertDialog.Builder(context);
							final View addPersonView = LayoutInflater.from(context).inflate(R.layout.add_person_menu, null, false);
							addPersonDialogName = (EditText)addPersonView.findViewById(R.id.addPersonDialogName);
							addPersonDialogGlobalID = (EditText)addPersonView.findViewById(R.id.addPersonDialogGlobalID);
							addPersonDialogMail = (EditText)addPersonView.findViewById(R.id.addPersonDialogMail);
							addPersonDialogDescription = (EditText)addPersonView.findViewById(R.id.addPersonDialogDescription);
							addPersonDialogOrigin = (EditText)addPersonView.findViewById(R.id.addPersonDialogOrigin);
							addPersonDialogType = (EditText)addPersonView.findViewById(R.id.addPersonDialogType);
							
							
							
							//addCommunityDialogID.setText("");
							addPersonDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {  
								   public void onClick(DialogInterface dialog, int id) {  
									    String addPersonDialogNameString = addPersonDialogName.getText().toString();
									    String addPersonDialogGlobalIDString = addPersonDialogGlobalID.getText().toString();
									    String addPersonDialogMailString = addPersonDialogMail.getText().toString();
									    String addPersonDialogDescriptionString = addPersonDialogDescription.getText().toString();
									    String addPersonDialogOriginString = addPersonDialogOrigin.getText().toString();
									    String addPersonDialogTypeString = addPersonDialogOrigin.getText().toString();
										   
	
	
									   // GLOBAL_ID (needed temporally until we have a working sync adapter)
									   // NAME
									   // DESCRIPTION (Optional)
									   // EMAIL (Optional)
									   // ORIGIN 
									    
									   
									   //Inserting the Person
									     ContentValues personValues = new ContentValues();
									     personValues.put(SocialContract.People.NAME, addPersonDialogNameString);
									     personValues.put(SocialContract.People.GLOBAL_ID, addPersonDialogGlobalIDString);
									     personValues.put(SocialContract.People.EMAIL, addPersonDialogMailString);
									     personValues.put(SocialContract.People.DESCRIPTION, addPersonDialogDescriptionString);
									     personValues.put(SocialContract.People.ORIGIN, addPersonDialogOriginString);
										 resolver.insert(SocialContract.People.CONTENT_URI, personValues);
										 
									   
										 //Inserting the Membership to the current selected Community
										 
	
									//    GLOBAL_ID (needed temporally until we have a working sync adapter)
									//    GLOBAL_ID_MEMBER
									//    GLOBAL_ID_COMMUNITY
									//    TYPE
									//    ORIGIN 
										 
										 ContentValues membershipValues = new ContentValues();
										 membershipValues.put(SocialContract.Membership.GLOBAL_ID, (int)Math.random());
										 membershipValues.put(SocialContract.Membership.GLOBAL_ID_MEMBER, addPersonDialogGlobalIDString);
										 membershipValues.put(SocialContract.Membership.GLOBAL_ID_COMMUNITY, activeCommunityGlobalID);
										 membershipValues.put(SocialContract.Membership.TYPE, addPersonDialogTypeString);
										 membershipValues.put(SocialContract.Membership.ORIGIN, addPersonDialogOriginString);
										 resolver.insert(SocialContract.Membership.CONTENT_URI, membershipValues);
										 
										 setupAssociate();
										 
										 resolver.notifyChange(SocialContract.Membership.CONTENT_URI, membershipObserver);
									}  
									}); 
							addPersonDialog.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {  
								   public void onClick(DialogInterface dialog, int id) {  
									     dialog.cancel(); 
									}  
									});  
							
					
							
							addPersonDialog.setView(addPersonView);
							addPersonDialog.setTitle("Legg til ny person");
							addPersonDialog.show();
							
						}
					});
					
					addPersonDialog.setView(addPersonView);
					addPersonDialog.setTitle("Legg til brukere");
				    addPersonDialogBox = addPersonDialog.create();
					addPersonDialogBox.show();
					
				}
			}
        });
			
       
        
        deleteCommunity.setOnClickListener(new OnClickListener() {
        	
        	
		
        	
        	
			
			public void onClick(View v) {

				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
				
				if (selectedCommunityIndex != -1){
					
					saveCommunity.setVisibility(View.INVISIBLE);
					cancelCommunity.setVisibility(View.INVISIBLE);
					name.setFocusableInTouchMode(false);
					name.setText("");
					type.setFocusableInTouchMode(false);
					type.setText("");
					description.setFocusableInTouchMode(false);
					description.setText("");
					aboutPerson.setFocusableInTouchMode(false);
					aboutPerson.setText("");
					phone.setFocusableInTouchMode(false);
					phone.setText("");
					
					
					AlertDialog.Builder deleteCommunityDialog = new AlertDialog.Builder(context);
					final View deleteCommunityView = LayoutInflater.from(context).inflate(R.layout.delete_community_dialog, null, false);
					deleteCommunityDialog.setTitle("Slett gruppe");
					deleteCommunityDialog.setPositiveButton("Bekreft", new DialogInterface.OnClickListener() {
	
						public void onClick(DialogInterface dialog, int which) {
							
							dialog.dismiss();
							resolver.delete(SocialContract.Communities.CONTENT_URI, SocialContract.Communities.GLOBAL_ID + "='" + activeCommunityGlobalID + "'", null);
							resolver.delete(SocialContract.People.CONTENT_URI, SocialContract.People.GLOBAL_ID +"='"+tempElderPesonGlobalID+"'", null);
							tempElderPesonGlobalID = "";
							selectedCommunityIndex = -1;
							name.setText("");
							name.setFocusableInTouchMode(false);
							type.setFocusableInTouchMode(false);
							type.setText("");
							description.setFocusableInTouchMode(false);
							description.setText("");
							aboutPerson.setFocusableInTouchMode(false);
							aboutPerson.setText("");
							phone.setFocusableInTouchMode(false);
							phone.setText("");
							
							
							setEditBoxesNotFocusableBackground();
							
							
						}
						
	
					});
					
					deleteCommunityDialog.setNegativeButton("Avbryt", new DialogInterface.OnClickListener(){
						
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							
						}
						
	
					});
					deleteCommunityDialog.setView(deleteCommunityView);
					deleteCommunityDialog.create();
					deleteCommunityDialog.show();
				}	
			}
		});
        
        editCommunity.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
				
				
				
				if (selectedCommunityIndex != -1){
					
						
						saveCommunity.setVisibility(View.VISIBLE);
						cancelCommunity.setVisibility(View.VISIBLE);
						name.setFocusableInTouchMode(true);
						type.setFocusableInTouchMode(true);
						description.setFocusableInTouchMode(true);
						aboutPerson.setFocusableInTouchMode(true);
						phone.setFocusableInTouchMode(true);
						
				//TODO lagre verdier lokalt. Sparer ressurser for en spørring
						
						tempCommunityName = name.getText().toString();
						tempType = type.getText().toString();
						tempDescription = description.getText().toString();
						tempAboutPerson = aboutPerson.getText().toString();
				
						
						 setEditBoxesFocusableBackground();
				}
			}
		});
        
        cancelCommunity.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
				
				
				
				saveCommunity.setVisibility(View.INVISIBLE);
				cancelCommunity.setVisibility(View.INVISIBLE);
				name.setFocusableInTouchMode(false);
				name.setText(tempCommunityName);
				type.setFocusableInTouchMode(false);
				type.setText(tempType);
				description.setFocusableInTouchMode(false);
				description.setText(tempDescription);
				aboutPerson.setFocusableInTouchMode(false);
				aboutPerson.setText(tempAboutPerson);
				
				phone.setFocusableInTouchMode(false);
				
				name.clearFocus();
				type.clearFocus();
				description.clearFocus();
				aboutPerson.clearFocus();
				phone.clearFocus();
				
				
				setEditBoxesNotFocusableBackground();
			}
		});
        
        saveCommunity.setOnClickListener(new OnClickListener() {
        	
        	
			
			public void onClick(View v) {
				
				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
				
				
				saveCommunity.setVisibility(View.INVISIBLE);
				cancelCommunity.setVisibility(View.INVISIBLE);
				name.setFocusableInTouchMode(false);
				name.clearFocus();
				type.setFocusableInTouchMode(false);
				type.clearFocus();
				description.setFocusableInTouchMode(false);
				description.clearFocus();
				aboutPerson.setFocusableInTouchMode(false);
				aboutPerson.clearFocus();
				phone.setFocusableInTouchMode(false);
				phone.clearFocus();
				
				setEditBoxesNotFocusableBackground();
				
				
				ContentValues values = new ContentValues();
				
				if (name.getText().toString().length() > 0)
					values.put(SocialContract.Communities.NAME, name.getText().toString());
				if (type.getText().toString().length() > 0)
					values.put(SocialContract.Communities.TYPE, type.getText().toString());
				values.put(SocialContract.Communities.DESCRIPTION, description.getText().toString());
				resolver.update(SocialContract.Communities.CONTENT_URI, values, SocialContract.Communities._ID + "='" + selectedCommunityIndex + "'", null);
				
				values.clear();
				values.put(SocialContract.People.DESCRIPTION, aboutPerson.getText().toString());
				resolver.update(SocialContract.People.CONTENT_URI, values, SocialContract.People.GLOBAL_ID +"='" + tempElderPesonGlobalID + "'", null);
			}
		});
        
        //ThirdCollumnstuff
        
       
        tableLayout = (TableLayout) findViewById(R.id.messagesTableLayout);
        scroll.post(new Runnable() {            
            public void run() {
                   scroll.fullScroll(View.FOCUS_DOWN);              
            }
        });
   
        userOkButton.setVisibility(View.INVISIBLE);
        //associates.setVisibility(View.INVISIBLE);
      
      //  setupAssociatesOne();
        
        
        //THIRD COLLUMN
        
        
        
        
        
       
        Cursor feedCursor = resolver.query(SocialContract.CommunityActivity.CONTENT_URI,
        		new String []{SocialContract.CommunityActivity.OBJECT}, null, null, null);
       
        
        Cursor comCursor = resolver.query(SocialContract.Communities.CONTENT_URI,
        		new String []{SocialContract.Communities.GLOBAL_ID}, null, null, null);
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
        
        
        
        
        
        
        
        
        
        
        
        
        sendMessageButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
				
				
				
				if (selectedCommunityIndex != -1 && inputMessageDialog.getText().toString().length() > 0){
					
					String input = inputMessageDialog.getText().toString();
					
					if (input.length() > 0){
					
						Cursor activeCommunityCursor = resolver.query(SocialContract.Communities.CONTENT_URI,
								new String []{SocialContract.Communities.OWNER_ID}, 
								SocialContract.Communities.GLOBAL_ID +"='"+activeCommunityGlobalID+"'", null, null);
						activeCommunityCursor.moveToFirst();
						
						
						ContentValues values = new ContentValues();
						values.put(SocialContract.CommunityActivity.GLOBAL_ID, Math.random());
						values.put(SocialContract.CommunityActivity.ACTOR, myGlobalID);      //SenderGlobalID
						values.put(SocialContract.CommunityActivity.GLOBAL_ID_FEED_OWNER, activeCommunityCursor.getString(0)); //owner ID
						values.put(SocialContract.CommunityActivity.OBJECT, activeCommunityGlobalID);	  //Community Global ID
						values.put(SocialContract.CommunityActivity.TARGET, 1);     //Type melding: Vanlig (1), Warning (2) eller Alarm (3)
						values.put(SocialContract.CommunityActivity.VERB, input);       //Teksten
						values.put(SocialContract.CommunityActivity.ORIGIN, "box.com");
						resolver.insert(SocialContract.CommunityActivity.CONTENT_URI, values);
						
						inputMessageDialog.setText("");
						resolver.notifyChange(SocialContract.CommunityActivity.CONTENT_URI, communityActivityObserver);
						
						
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
        
        showNotificationOrange(3);
        
        }

        
        
  
        
    }
    
    
    public void drawNotificationOranges(int number){
    	
    	 RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(45,45);
         LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(45,45);
         
         incommingMessageTablelayout.removeAllViews();
         
         
    	for (int i=0;i<number;i++){
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
    
    
    void registerContentObserver(Cursor cursor, ContentObserver observer) {
        cursor.registerContentObserver(observer);
    }
    
    
    public void setupCommunityMessages(){
    	
    	
    	tableLayout.removeAllViews();

    	  drawUserOKMessage("Brukeren er OK.", System.currentTimeMillis());
    	  drawAlarm("Alarm! Systemet har oppdaget et fall!", System.currentTimeMillis());
    	  drawWarning("Advarsel! Litt usikker på hva denne skal si", System.currentTimeMillis());
    	  
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
    
    public void setupAssociate(){
   
    	
    	
    	
    	//POPUP START
        popUp = new PopupWindow(this);
        LinearLayout imagesRow = new LinearLayout(this);
        LinearLayout textRow = new LinearLayout(this);
        
        
        layout = new LinearLayout(this);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.WHITE);
        
        
        ImageView deleteImage = new ImageView(context);
        deleteImage.setImageResource(R.drawable.edit_delete_mail);
        deleteImage.setPadding(0, 0, 25, 0);
        
        deleteImage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
				
				
				AlertDialog.Builder  deletePersonMembership = new AlertDialog.Builder(context);
				final View removePersonView = LayoutInflater.from(context).inflate(R.layout.popup_layout, null, false);
				deletePersonMembership.setTitle("Fjerne person fra listen?");
				deletePersonMembership.setView(removePersonView);
				deletePersonMembership.setPositiveButton("Bekreft", new DialogInterface.OnClickListener()  {
					
					public void onClick(DialogInterface dialog, int which) {
					
						dialog.dismiss();
						
						
						//Fjerne den valgte personen fra gruppen ved å slette medlemskapet
						// 1) Hent inn Personen sin Global_ID
						// 2) Hent inn det aktive Communitiet sin GLOBAL_ID
						// 3) Finn medlemskapet hvor begge disse eksisterer
						// 4) Slett medlemskapet
						
						
						resolver.delete(SocialContract.Membership.CONTENT_URI, SocialContract.Membership.GLOBAL_ID_COMMUNITY + "='" +
						activeCommunityGlobalID +"' AND " + SocialContract.Membership.GLOBAL_ID_MEMBER + "='" + associateListViewItemClickedGlobalID+"'", null);
						
						
						
					}
				});
				
				
				deletePersonMembership.setNegativeButton("Avbryt", new DialogInterface.OnClickListener()  {
			
					public void onClick(DialogInterface dialog, int which) {
					
						dialog.dismiss();
						
					}
				});
				
				deletePersonMembership.create();
				 
				// show it
				deletePersonMembership.show();
				
				
			}
		});
        
        ImageView editImage = new ImageView(context);
        editImage.setImageResource(R.drawable.edit);
        editImage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
			
				if (popupIsShowing) {
					popUp.dismiss();
					popupIsShowing = false;
				}
				
				AlertDialog.Builder  editPersonMembership = new AlertDialog.Builder(context);
				final View editPersonView = LayoutInflater.from(context).inflate(R.layout.edit_people_dialog, null, false);
				
				Log.d("TAG", "%ass GlobalID : "+ associateListViewItemClickedGlobalID);
				final Cursor associateCursor = resolver.query(SocialContract.People.CONTENT_URI, null, SocialContract.People.GLOBAL_ID + "='"+ associateListViewItemClickedGlobalID+"'", null, null);
				Cursor membershipCursor = resolver.query(SocialContract.Membership.CONTENT_URI, new String []{SocialContract.Membership.TYPE}, 
						SocialContract.Membership.GLOBAL_ID_COMMUNITY +"='"+activeCommunityGlobalID+"' AND "+
				SocialContract.Membership.GLOBAL_ID_MEMBER +"='"+associateListViewItemClickedGlobalID+"'", null, null);
				
				String tempAssociateName = "";
				String tempAssociateGlobalID = "";
				String tempAssociateDescription = "";
				String tempAssociateEmail = "";
				String tempAssociateOrigin = "";
				String tempAssociateType = "";
				
				
				
				
				if (associateCursor != null){
					associateCursor.moveToFirst();
					tempAssociateGlobalID = associateCursor.getString(1);
					tempAssociateName = associateCursor.getString(2);
					tempAssociateEmail = associateCursor.getString(3);
					tempAssociateOrigin = associateCursor.getString(4);
					tempAssociateDescription = associateCursor.getString(5);
				}
				
				if (membershipCursor != null){
					membershipCursor.moveToFirst();
					tempAssociateType = membershipCursor.getString(0);
				}
				 associateEditName = (EditText)editPersonView.findViewById(R.id.editPersonName);
				 associateEditGlobalID = (EditText)editPersonView.findViewById(R.id.editPersonID);
				 associateEditDescription = (EditText)editPersonView.findViewById(R.id.editPersonDescription);
				 associateEditEmail = (EditText)editPersonView.findViewById(R.id.editPersonEmail);
				 associateEditOrigin = (EditText)editPersonView.findViewById(R.id.editPersonOrigin);
				 associateEditType = (EditText)editPersonView.findViewById(R.id.editPersonType);
				
				associateEditName.setText(tempAssociateName);
				associateEditGlobalID.setText(tempAssociateGlobalID);
				associateEditDescription.setText(tempAssociateDescription);
				associateEditEmail.setText(tempAssociateEmail);
				associateEditOrigin.setText(tempAssociateOrigin);
				associateEditType.setText(tempAssociateType);
				
				
				
				
				
				editPersonMembership.setTitle("Endre personinformasjon");
				editPersonMembership.setView(editPersonView);
				editPersonMembership.setPositiveButton("Lagre", new DialogInterface.OnClickListener()  {
					
					public void onClick(DialogInterface dialog, int which) {
					
					
						dialog.dismiss();
						
						if (associateEditName.getText().toString().length() > 0 && associateEditGlobalID.getText().toString().length() > 0 && associateEditOrigin.getText().toString().length() > 0){
							
							ContentValues values = new ContentValues();
							values.put(SocialContract.People.NAME, associateEditName.getText().toString());
							values.put(SocialContract.People.GLOBAL_ID, associateEditGlobalID.getText().toString());
							values.put(SocialContract.People.DESCRIPTION, associateEditDescription.getText().toString());
							values.put(SocialContract.People.EMAIL, associateEditEmail.getText().toString());
							values.put(SocialContract.People.ORIGIN, associateEditOrigin.getText().toString());
							resolver.update(SocialContract.People.CONTENT_URI, values, SocialContract.People.GLOBAL_ID + "='"+associateCursor.getString(1)+"'", null);
							
							values.clear();
							
							values.put(SocialContract.Membership.TYPE, associateEditType.getText().toString());
							values.put(SocialContract.Membership.GLOBAL_ID_MEMBER, associateEditGlobalID.getText().toString());
							resolver.update(SocialContract.Membership.CONTENT_URI, values, SocialContract.Membership.GLOBAL_ID_MEMBER + "='" + associateCursor.getString(1)+"'", null);
							
							
						}
						
					}
				});
				
				editPersonMembership.setNegativeButton("Avbryt", new DialogInterface.OnClickListener()  {
					
					public void onClick(DialogInterface dialog, int which) {
					
					
						dialog.dismiss();
						
						
						
					}
				});
				
				editPersonMembership.create();
				editPersonMembership.show();
				
			}
		});
     
      
        imagesRow.addView(deleteImage);
        imagesRow.addView(editImage);
        
        
        TextView textDelete = new TextView(this);
        textDelete.setText("Slett");
        textDelete.setPadding(5, 0, 35, 0);
        
        TextView textEdit = new TextView(this);
        textEdit.setText("Endre");
        
        
        
        textRow.addView(textDelete);
        textRow.addView(textEdit);
        
      //  layout.addView(tv, params);
        layout.addView(imagesRow, params);
        layout.addView(textRow, params);
        popUp.setContentView(layout);
        ll = (LinearLayout)findViewById(R.id.ll);
        layoutParams = new LinearLayout.LayoutParams
        (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        
        //POPUP END
        
        
   	  String[] from = new String[] {  SocialContract.People.NAME};

      associateListAdapter = new SimpleCursorAdapter(this,  R.layout.associate_listview_item_layout, null,
              from, new int[]{R.id.textview},
              0);
      associateListView = (ListView) findViewById(R.id.associateList);
      associateListView.setVisibility(View.INVISIBLE);
      associateListView.setAdapter(associateListAdapter);
      associateListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				

				if (popupIsShowing) {
					popUp.dismiss();
				}
				
				popupIsShowing = true;
				popUp.showAtLocation(view, Gravity.TOP | Gravity.LEFT, 0, 0);
				popUp.update((int)secondCollumn.getX() + ((int)view.getMeasuredWidth()/2), (int)associateListView.getY() + (int)view.getY()- (int)view.getMeasuredHeight(), 120, 80);
				
				
				
				activeAssociateCursor.moveToFirst();
				activeAssociateCursor.move(position);
				associateListViewItemClickedGlobalID = activeAssociateCursor.getString(0);
				
				return true;
				
			}

		});
      
      
      
    
      
      
      
    }
    
    
    
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_central_main, menu);
        return true;
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
        messageText.setWidth(220);
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
   	
   	 TableRow signedRow = new TableRow(context);
        TextView signedText = new TextView(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yy, HH:mm");
        Date date = new Date(dateInput);
        signedText.setText(from + " - " + dateFormat.format(date));
        signedText.setTextSize(12);
        signedText.setBackgroundColor(Color.LTGRAY);
        signedText.setPadding(5, 5, 5, 5);
        
        TableRow messageRow = new TableRow(context);
        TextView messageText = new TextView(context);
        messageText.setText(message);
        messageText.setBackgroundColor(Color.LTGRAY);
        messageText.setPadding(5, 5, 5, 5);
        messageText.setWidth(220);
        messageText.setTextSize(18);
        
        signedRow.setGravity(Gravity.RIGHT | Gravity.TOP);
        messageRow.setGravity(Gravity.RIGHT | Gravity.TOP);
        messageRow.setPadding(5, 15, 5, 0);
        signedRow.setPadding(5, 0, 5, 0);
        
        signedRow.addView(signedText);
        messageRow.addView(messageText);
        tableLayout.addView(messageRow);
        tableLayout.addView(signedRow);
        
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
	   messageText.setWidth(220);
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
	   messageText.setWidth(220);
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
   messageText.setWidth(220);
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
	   messageText.setWidth(220);
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
   

   
   	 public void queryMyself(){
   		 
   		 Cursor meCursor = resolver.query(SocialContract.Me.CONTENT_URI, null, null, null, null);
   		 meCursor.moveToFirst();
   		 myGlobalID = meCursor.getString(1);
   		 
   		 Log.d("MEG", "%me size: " + meCursor.getCount());
   		 Log.d("MEG", "%me 0: " + meCursor.getString(0));
   		 Log.d("MEG", "%me 1: " + meCursor.getString(1));
   		 Log.d("MEG", "%me 2: " + meCursor.getString(2));
   		 Log.d("MEG", "%me 3: " + meCursor.getString(3));
   		 Log.d("MEG", "%me 4: " + meCursor.getString(4));
   		 
   		 
   	 }

	 public void insertMyself(){
		 
/*
		    GLOBAL_ID
		    NAME
		    DISPLAY_NAME (Optional, set to "NA" if not provided)
		    USER_NAME
		    PASSWORD
		    ORIGIN 
		    */
		 resolver.delete(SocialContract.Me.CONTENT_URI, null, null);
		 
		    
		     ContentValues memberValues = new ContentValues();
		     memberValues.put(SocialContract.Me.GLOBAL_ID, "12345678");
			 memberValues.put(SocialContract.Me.NAME, "Andreas");
			 memberValues.put(SocialContract.Me.ORIGIN, "box.com");
			 memberValues.put(SocialContract.Me.USER_NAME, "andlru");
			 memberValues.put(SocialContract.Me.PASSWORD, "passord");
			 resolver.insert(SocialContract.Me.CONTENT_URI, memberValues);
			 
			 
	 }
	 


		public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
			String[] from;
			switch (id){
	        case 0:
			
			
	        case 1:
	        	
	        	from = new String[] { SocialContract.Communities.NAME,  SocialContract.Communities._ID};
	        	
	        	Cursor communities = resolver.query(SocialContract.Communities.CONTENT_URI,
	        			new String[]{SocialContract.Communities.GLOBAL_ID}, null, null, null);
	        	communities.moveToFirst();
	        	
	        	
				   return new CursorLoader(this, SocialContract.Communities.CONTENT_URI,
						   from, null, null, null);
				   
				   
	        case 2:
	        	from = new String[] { SocialContract.People.NAME };
	        	
	        	
	        	
	        
	        	
	        	Cursor membershipCursor = resolver.query(SocialContract.Membership.CONTENT_URI,  
	       	    		 new String[] { SocialContract.Membership.GLOBAL_ID_MEMBER, SocialContract.Membership.TYPE},
	       	    		 SocialContract.Membership.GLOBAL_ID_COMMUNITY +"='"+ activeCommunityGlobalID + "'", null, null);
	       	     
	       	     membershipCursor.moveToFirst();
	        	StringBuilder selectiontest = new StringBuilder();
	        	
	        	if (!membershipCursor.isAfterLast() && membershipCursor.getString(1).equals("eldre"))
	        		membershipCursor.moveToNext();
	        	
	        	

	        	 if (!membershipCursor.isAfterLast()){
	       	    	 selectiontest.append( SocialContract.People.GLOBAL_ID+"='"+ membershipCursor.getString(0)+"'");
	       	    	 membershipCursor.moveToNext();
	       	     }
	        	 
	        	 
	        	
	        	//Bygger opp selectionen ved å legge inn alt av People som er knyttet til disse medlemskapene. 
	        	while (!membershipCursor.isAfterLast()){
	        		if (!membershipCursor.getString(1).equals("eldre")){
		        		selectiontest.append(" OR ");
		        		selectiontest.append( SocialContract.People.GLOBAL_ID+"='"+ membershipCursor.getString(0)+"'");
	        		}
	        		membershipCursor.moveToNext();
	        	}
	        	
	        	
	        	if (selectiontest.length()==0){
	        		selectiontest.append(SocialContract.People.GLOBAL_ID+"=''");
	        	}
	        	
	        	activeAssociateCursor = resolver.query(SocialContract.People.CONTENT_URI,
	        			new String[] {SocialContract.People.GLOBAL_ID, SocialContract.People.NAME},
	        			selectiontest.toString()
	        	, null, null);
	        	
	        	return new CursorLoader(this, SocialContract.People.CONTENT_URI,
	        			new String[] {SocialContract.People._ID, SocialContract.People.NAME},
	        			selectiontest.toString()
	        	, null, null);
		
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
			case 2:
				associateListAdapter.swapCursor(cursor);
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
		        case 2:
		        	associateListAdapter.swapCursor(null);
		        	break;
		        default:
		            break;
		    }

		}
	  
	  
		private class People{
			String name;
			String global_id;
			
			public People(String name, String global_id){
				this.name = name;
				this.global_id = global_id;
			}
			
			public String getName(){
				return name;
			}
			public String getGlobalID(){
				return global_id;
			}
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

		
		public void setEditBoxesFocusableBackground(){
			
			 name.setBackgroundResource(R.drawable.edittext_boxes_selector);
			 type.setBackgroundResource(R.drawable.edittext_boxes_selector);
			 description.setBackgroundResource(R.drawable.edittext_boxes_selector);
			 aboutPerson.setBackgroundResource(R.drawable.edittext_boxes_selector);
			 phone.setBackgroundResource(R.drawable.edittext_boxes_selector);
		}
		
		
		public void setEditBoxesNotFocusableBackground(){
			
			 name.setBackgroundResource(R.layout.borders_gray_editbox);
			 type.setBackgroundResource(R.layout.borders_gray_editbox);
			 description.setBackgroundResource(R.layout.borders_gray_editbox);
			 aboutPerson.setBackgroundResource(R.layout.borders_gray_editbox);
			 phone.setBackgroundResource(R.layout.borders_gray_editbox);
		}

}
