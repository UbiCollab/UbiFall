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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class SecondCollumnActivity extends FragmentActivity implements
LoaderManager.LoaderCallbacks<Cursor>{
	
	
	private TableLayout secondCollumn;
	private Button saveCommunity, cancelCommunity, addPerson, editCommunity, deleteCommunity;
	private EditText addCommunityDialogName, addCommunityDialogID, addCommunityDialogType, addCommunityDialogDescription, addCommunityDialogOrigin,
	addPersonDialogName, addPersonDialogOrigin, addPersonDialogGlobalID, addPersonDialogDescription, addPersonDialogMail, addPersonDialogType,
	associateEditName, associateEditGlobalID, associateEditType, associateEditDescription, associateEditEmail, associateEditOrigin, inputMessageDialog, 
	addCommunityDialogPersonDescription, addCommunityDialogPersonEmail;
	private OnLongPressCommunityLayout onLongPressCommunityLayout;
	private String activeCommunityGlobalID="";
	  private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
	private CursorAdapter associateListAdapter;
	private ContentResolver resolver;
	private PopupWindow popUp;
	private LinearLayout layout;
	private LayoutParams params;
	private Context context;
	private boolean popupIsShowing = false;
	private String associateListViewItemClickedGlobalID;
	private LinearLayout.LayoutParams layoutParams;
	private LinearLayout ll;
	private ListView associateListView; 
	private Cursor activeAssociateCursor;
	private int selectedCommunityIndex = -1;
	private AlertDialog addPersonDialogBox;
	private EditText name, description, phone;
	// private EditText type, aboutPerson;
	private String tempCommunityName, tempType, tempDescription, tempAboutPerson, tempElderPesonGlobalID;
	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_central_second_mobile);
	        context = this;
	        addPerson = (Button)findViewById(R.id.addPerson);
	        editCommunity = (Button)findViewById(R.id.button_change_community_info);
	        deleteCommunity = (Button)findViewById(R.id.button_delete_community);
	        onLongPressCommunityLayout = new OnLongPressCommunityLayout(this);
	        saveCommunity = (Button)findViewById(R.id.change_community_save);
	        cancelCommunity = (Button)findViewById(R.id.change_community_cancel);
	        saveCommunity.setVisibility(View.INVISIBLE);
	        cancelCommunity.setVisibility(View.INVISIBLE);
	        secondCollumn = (TableLayout)findViewById(R.id.secondCollumn);
	        name = (EditText)findViewById(R.id.name);
	        name.setFocusable(false);
	  //      type = (EditText)findViewById(R.id.type);
	 //       type.setFocusable(false);
	        description = (EditText)findViewById(R.id.description);
	        description.setFocusable(false);
	  //      aboutPerson = (EditText)findViewById(R.id.personDescription);
	  //      aboutPerson.setFocusable(false);
	        phone = (EditText)findViewById(R.id.phone);
	        phone.setFocusable(false);
	        resolver = getContentResolver();
	        setEditBoxesNotFocusableBackground();
	        
	        secondCollumn.setOnTouchListener(new OnTouchListener() {
				
				public boolean onTouch(View v, MotionEvent event) {
					if (popupIsShowing) {
						popUp.dismiss();
						popupIsShowing = false;
					}
					return true;
				}
			});
	        
	        
	        
	        final TabHost tabs = (TabHost) this.findViewById(R.id.my_tabhost);
	           tabs.setup();
	           
	           activeCommunityGlobalID = getIntent().getStringExtra("ActiveCommunityGlobalID");
	           if (activeCommunityGlobalID == null)
	        	   activeCommunityGlobalID="";
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
	                	   
	                	  if (collumn.equals("firstCollumn")){
	                		Intent i = new Intent(getApplicationContext(), FirstCollumnActivity.class);
	                		i.putExtra("TabNumber", 0); 
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
	               mCallbacks = this;
	               setupAssociate();
	               
	               
	               
	               getSupportLoaderManager().initLoader(2, null, this);  
	               
	               
	               
	               
	               addPerson.setOnClickListener(new OnClickListener() {

	       			public void onClick(View v) {

	       				if (popupIsShowing) {
	       					popUp.dismiss();
	       					popupIsShowing = false;
	       				}
	       			
	       				if (activeCommunityGlobalID.length() > 0){
	       				
	       					AlertDialog.Builder addPersonDialog = new AlertDialog.Builder(context);
	       					final View addPersonView = LayoutInflater.from(context).inflate(R.layout.add_person_first_menu, null, false);
	       					Button addNewPerson = (Button)addPersonView.findViewById(R.id.addNewPerson);
	       					TableLayout tableLayout = (TableLayout)addPersonView.findViewById(R.id.addPersonTable);
	       				
	       					
	       					// 1) Hente xml skjema
	       					// 2) dynamisk legge inn checkboxer med alle eksisterende personer som ikke er i den aktive Communitien
	       					
	       					
	       					//henter folk i den aktive communitien
	       					Cursor peopleInCommunityCursor = resolver.query(SocialContract.Membership.CONTENT_URI,
	       							new String []{SocialContract.Membership._ID_MEMBER, SocialContract.Membership.TYPE}, 
	       							SocialContract.Membership._ID_COMMUNITY +"='"+activeCommunityGlobalID+"'", null, null);
	       					peopleInCommunityCursor.moveToFirst();
	       					
	       					
	       					Cursor allPeople = resolver.query(SocialContract.People.CONTENT_URI, new String []{SocialContract.People._ID, SocialContract.People.NAME}, 
	       							null, null, null);
	       					allPeople.moveToFirst();
	       					
	       					//Alle memberships
	       					Cursor membershipCursor = resolver.query(SocialContract.Membership.CONTENT_URI, 
	       							new String[]{SocialContract.Membership._ID_MEMBER, SocialContract.Membership.TYPE}, null, null, null);
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
	       						//Altså personen er ikke i den aktive communitien
	       						if (!deleted){
	       						
	       							//Fjerne eldre
	       							while(!membershipCursor.isAfterLast()){
	       								
	       								//Hvis personen Membershipet har samme ID som personen og den har type = eldre skal den fjernes
	       								if(membershipCursor.getString(0).equals(allPeople.getString(0)) && membershipCursor.getString(1).equals("eldre")){
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
	       								
	       								//TODO Bytte ut dette med litt style. Gjort, se om dette fungerer
	       								
	       								
	       								if (isChecked){
	       									//editText.setBackgroundColor(Color.WHITE);
	       									editText.setBackgroundResource(R.drawable.edittext_boxes_selector);
	       									editText.setFocusableInTouchMode(true);
	       								}
	       								else{
	       									//editText.setBackgroundColor(Color.LTGRAY);
	       									editText.setBackgroundResource(R.layout.borders_gray_editbox);
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
	       									values.put(SocialContract.Membership._ID_COMMUNITY, activeCommunityGlobalID);
	       									values.put(SocialContract.Membership._ID, Math.random());
	       									values.put(SocialContract.Membership._ID_MEMBER, checkBoxes.get(i).getGlobal_ID());
	       									values.put(SocialContract.Membership.ACCOUNT_TYPE, "box.com");
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
	       							final View addPersonView = LayoutInflater.from(context).inflate(R.layout.add_person_menu_mobile, null, false);
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
	       									     personValues.put(SocialContract.People._ID, addPersonDialogGlobalIDString);
	       									     personValues.put(SocialContract.People.EMAIL, addPersonDialogMailString);
	       									     personValues.put(SocialContract.People.DESCRIPTION, addPersonDialogDescriptionString);
	       									     personValues.put(SocialContract.People.ACCOUNT_TYPE, addPersonDialogOriginString);
	       										 resolver.insert(SocialContract.People.CONTENT_URI, personValues);
	       										 
	       									   
	       										 //Inserting the Membership to the current selected Community
	       										 
	       	
	       									//    GLOBAL_ID (needed temporally until we have a working sync adapter)
	       									//    GLOBAL_ID_MEMBER
	       									//    GLOBAL_ID_COMMUNITY
	       									//    TYPE
	       									//    ORIGIN 
	       										 
	       										 ContentValues membershipValues = new ContentValues();
	       										 membershipValues.put(SocialContract.Membership._ID, (int)Math.random());
	       										 membershipValues.put(SocialContract.Membership._ID_MEMBER, addPersonDialogGlobalIDString);
	       										 membershipValues.put(SocialContract.Membership._ID_COMMUNITY, activeCommunityGlobalID);
	       										 membershipValues.put(SocialContract.Membership.TYPE, addPersonDialogTypeString);
	       										 membershipValues.put(SocialContract.Membership.ACCOUNT_TYPE, addPersonDialogOriginString);
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
	       				//	type.setFocusableInTouchMode(false);
	       				//	type.setText("");
	       					description.setFocusableInTouchMode(false);
	       					description.setText("");
	       				//	aboutPerson.setFocusableInTouchMode(false);
	       				//	aboutPerson.setText("");
	       					phone.setFocusableInTouchMode(false);
	       					phone.setText("");
	       					
	       					
	       					AlertDialog.Builder deleteCommunityDialog = new AlertDialog.Builder(context);
	       					final View deleteCommunityView = LayoutInflater.from(context).inflate(R.layout.delete_community_dialog, null, false);
	       					deleteCommunityDialog.setTitle("Slett gruppe");
	       					deleteCommunityDialog.setPositiveButton("Bekreft", new DialogInterface.OnClickListener() {
	       	
	       						public void onClick(DialogInterface dialog, int which) {
	       							
	       							dialog.dismiss();
	       							resolver.delete(SocialContract.Communities.CONTENT_URI, SocialContract.Communities._ID + "='" + activeCommunityGlobalID + "'", null);
	       							resolver.delete(SocialContract.People.CONTENT_URI, SocialContract.People._ID +"='"+tempElderPesonGlobalID+"'", null);
	       							tempElderPesonGlobalID = "";
	       							selectedCommunityIndex = -1;
	       							name.setText("");
	       							name.setFocusableInTouchMode(false);
	       						//	type.setFocusableInTouchMode(false);
	       						//	type.setText("");
	       							description.setFocusableInTouchMode(false);
	       							description.setText("");
	       						//	aboutPerson.setFocusableInTouchMode(false);
	       						//	aboutPerson.setText("");
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
	       				
	       				
	       				
	       				if (activeCommunityGlobalID.length() > 0){
	       					
	       						
	       						saveCommunity.setVisibility(View.VISIBLE);
	       						cancelCommunity.setVisibility(View.VISIBLE);
	       						name.setFocusableInTouchMode(true);
	       					//	type.setFocusableInTouchMode(true);
	       						description.setFocusableInTouchMode(true);
	       					//	aboutPerson.setFocusableInTouchMode(true);
	       						phone.setFocusableInTouchMode(true);
	       						
	       				//TODO lagre verdier lokalt. Sparer ressurser for en spørring
	       						
	       						tempCommunityName = name.getText().toString();
	       					//	tempType = type.getText().toString();
	       						tempDescription = description.getText().toString();
	       					//	tempAboutPerson = aboutPerson.getText().toString();
	       				
	       						
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
	       		//		type.setFocusableInTouchMode(false);
	       		//		type.setText(tempType);
	       				description.setFocusableInTouchMode(false);
	       				description.setText(tempDescription);
	       		//		aboutPerson.setFocusableInTouchMode(false);
	       		//		aboutPerson.setText(tempAboutPerson);
	       				phone.setFocusableInTouchMode(false);
	       				
	       				name.clearFocus();
	       		//		type.clearFocus();
	       				description.clearFocus();
	       		//		aboutPerson.clearFocus();
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
	       		//		type.setFocusableInTouchMode(false);
	       		//		type.clearFocus();
	       				description.setFocusableInTouchMode(false);
	       				description.clearFocus();
	       		//		aboutPerson.setFocusableInTouchMode(false);
	       		//		aboutPerson.clearFocus();
	       				phone.setFocusableInTouchMode(false);
	       				phone.clearFocus();
	       				
	       				
	       				setEditBoxesNotFocusableBackground();
	       				
	       				
	       				ContentValues values = new ContentValues();
	       				
	       				if (name.getText().toString().length() > 0)
	       					values.put(SocialContract.Communities.NAME, name.getText().toString());
	       		//		if (type.getText().toString().length() > 0)
	       			//		values.put(SocialContract.Communities.TYPE, type.getText().toString());
	       				values.put(SocialContract.Communities.DESCRIPTION, description.getText().toString());
	       				resolver.update(SocialContract.Communities.CONTENT_URI, values, SocialContract.Communities._ID + "='" + selectedCommunityIndex + "'", null);
	       				
	       				values.clear();
	       		//		values.put(SocialContract.People.DESCRIPTION, aboutPerson.getText().toString());
	       		//		resolver.update(SocialContract.People.CONTENT_URI, values, SocialContract.People._ID +"='" + tempElderPesonGlobalID + "'", null);
	       			}
	       		});
	               
	               
	               
	               
	               
	        
	 }
	 
	 @Override
		protected void onPause()
		{
		 	super.onPause();
			Log.d("TAG", "%app onPause()");
	        startService(new Intent(this, ServiceClass.class));
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
							
							
							resolver.delete(SocialContract.Membership.CONTENT_URI, SocialContract.Membership._ID_COMMUNITY + "='" +
							activeCommunityGlobalID +"' AND " + SocialContract.Membership._ID_MEMBER + "='" + associateListViewItemClickedGlobalID+"'", null);
							
							
							
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
					final Cursor associateCursor = resolver.query(SocialContract.People.CONTENT_URI, null, SocialContract.People._ID + "='"+ associateListViewItemClickedGlobalID+"'", null, null);
					Cursor membershipCursor = resolver.query(SocialContract.Membership.CONTENT_URI, new String []{SocialContract.Membership.TYPE}, 
							SocialContract.Membership._ID_COMMUNITY +"='"+activeCommunityGlobalID+"' AND "+
					SocialContract.Membership._ID_MEMBER +"='"+associateListViewItemClickedGlobalID+"'", null, null);
					
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
								values.put(SocialContract.People._ID, associateEditGlobalID.getText().toString());
								values.put(SocialContract.People.DESCRIPTION, associateEditDescription.getText().toString());
								values.put(SocialContract.People.EMAIL, associateEditEmail.getText().toString());
								values.put(SocialContract.People.ACCOUNT_TYPE, associateEditOrigin.getText().toString());
								resolver.update(SocialContract.People.CONTENT_URI, values, SocialContract.People._ID + "='"+associateCursor.getString(1)+"'", null);
								
								values.clear();
								
								values.put(SocialContract.Membership.TYPE, associateEditType.getText().toString());
								values.put(SocialContract.Membership._ID_MEMBER, associateEditGlobalID.getText().toString());
								resolver.update(SocialContract.Membership.CONTENT_URI, values, SocialContract.Membership._ID_MEMBER + "='" + associateCursor.getString(1)+"'", null);
								
								
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
	        textDelete.setPadding(5, 0, 45, 0);
	        
	        TextView textEdit = new TextView(this);
	        textEdit.setText("Endre");
	        
	        
	        
	        textRow.addView(textDelete);
	        textRow.addView(textEdit);
	        
	      //  layout.addView(tv, params);
	        layout.addView(imagesRow, params);
	        layout.addView(textRow, params);
	        popUp.setContentView(layout);
	        
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
					popUp.update((int)secondCollumn.getX() + ((int)view.getMeasuredWidth()/2), (int)associateListView.getY() + (int)view.getY()- (int)view.getMeasuredHeight(), 200, 150);
					
					
					
					activeAssociateCursor.moveToFirst();
					activeAssociateCursor.move(position);
					associateListViewItemClickedGlobalID = activeAssociateCursor.getString(0);
					
					return true;
					
				}

			});
	      
	      
	      if (activeCommunityGlobalID.length() > 0){
	    
		      Cursor communityCursor = resolver.query(SocialContract.Communities.CONTENT_URI,
						new String[] {SocialContract.Communities.NAME, SocialContract.Communities.TYPE, 
						SocialContract.Communities.DESCRIPTION}, SocialContract.Communities._ID+"='"+activeCommunityGlobalID+"'", null, null);
				
				communityCursor.moveToFirst();
				name.setFocusable(false);
				name.setText(communityCursor.getString(0));
			//	type.setFocusable(false);
			//	type.setText(communityCursor.getString(1));
				description.setFocusable(false);
				description.setText(communityCursor.getString(2));
		      
				
				Cursor membershipCursor = resolver.query(SocialContract.Membership.CONTENT_URI, 
						new String []{SocialContract.Membership._ID_MEMBER}, 
						SocialContract.Membership._ID_COMMUNITY +"='"+  activeCommunityGlobalID  + "'" + " AND "
						+ SocialContract.Membership.TYPE +"='eldre'", null, null );
				membershipCursor.moveToFirst();
				
				if (membershipCursor.getCount()>0){
					associateListView.setVisibility(View.VISIBLE);
					tempElderPesonGlobalID = membershipCursor.getString(0);
					Cursor peopleCursor = resolver.query(SocialContract.People.CONTENT_URI,
							new String []{SocialContract.People.DESCRIPTION}, SocialContract.People._ID +"='"+tempElderPesonGlobalID+"'", null,	null);
					peopleCursor.moveToFirst();
					
			//		aboutPerson.setFocusable(false);
			//		aboutPerson.setText(peopleCursor.getString(0));
				}
	      }
	    }
	    

		public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
			switch (id){
	        case 2:
	        	
	        	
	        	
	        
	        	
	        	Cursor membershipCursor = resolver.query(SocialContract.Membership.CONTENT_URI,  
	       	    		 new String[] { SocialContract.Membership._ID_MEMBER, SocialContract.Membership.TYPE},
	       	    		 SocialContract.Membership._ID_COMMUNITY +"='"+ activeCommunityGlobalID + "'", null, null);
	       	     
	       	     membershipCursor.moveToFirst();
	        	StringBuilder selectiontest = new StringBuilder();
	        	
	        	if (!membershipCursor.isAfterLast() && membershipCursor.getString(1).equals("eldre"))
	        		membershipCursor.moveToNext();
	        	
	        	

	        	 if (!membershipCursor.isAfterLast()){
	       	    	 selectiontest.append( SocialContract.People._ID+"='"+ membershipCursor.getString(0)+"'");
	       	    	 membershipCursor.moveToNext();
	       	     }
	        	 
	        	 
	        	
	        	//Bygger opp selectionen ved å legge inn alt av People som er knyttet til disse medlemskapene. 
	        	while (!membershipCursor.isAfterLast()){
	        		if (!membershipCursor.getString(1).equals("eldre")){
		        		selectiontest.append(" OR ");
		        		selectiontest.append( SocialContract.People._ID+"='"+ membershipCursor.getString(0)+"'");
	        		}
	        		membershipCursor.moveToNext();
	        	}
	        	
	        	
	        	if (selectiontest.length()==0){
	        		selectiontest.append(SocialContract.People._ID+"=''");
	        	}
	        	
	        	activeAssociateCursor = resolver.query(SocialContract.People.CONTENT_URI,
	        			new String[] {SocialContract.People._ID, SocialContract.People.NAME},
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
		
		public void setEditBoxesFocusableBackground(){
			
			 name.setBackgroundResource(R.drawable.edittext_boxes_selector);
		//	 type.setBackgroundResource(R.drawable.edittext_boxes_selector);
			 description.setBackgroundResource(R.drawable.edittext_boxes_selector);
		//	 aboutPerson.setBackgroundResource(R.drawable.edittext_boxes_selector);
			 phone.setBackgroundResource(R.drawable.edittext_boxes_selector);
		}
		
		
		public void setEditBoxesNotFocusableBackground(){
			
			 name.setBackgroundResource(R.layout.borders_gray_editbox);
		//	 type.setBackgroundResource(R.layout.borders_gray_editbox);
			 description.setBackgroundResource(R.layout.borders_gray_editbox);
		//	 aboutPerson.setBackgroundResource(R.layout.borders_gray_editbox);
			 phone.setBackgroundResource(R.layout.borders_gray_editbox);
			 
		}

}
