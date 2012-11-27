package com.example.falldetectioncentral;

import java.io.Serializable;
import java.util.ArrayList;

//This class is currently not in use. Need the Create time from the SyncAdapter on the Feed to use this. 

public class SettingsObject implements Serializable {


	private static final long serialVersionUID = 2L;
	private ArrayList<ClickedCommunity> communitiesTimeClicked = new ArrayList<ClickedCommunity>();
	private long lastEnteredApplicationTime = System.currentTimeMillis();
	
	
	
	public void updateTimeClicked(String Global_ID){
		
		for (int i=0;i<communitiesTimeClicked.size();i++){
			if (communitiesTimeClicked.get(i).getGlobal_ID().equals(Global_ID)){
				communitiesTimeClicked.get(i).updateTimeClicked();
				break;
			}
		}
		
	}
	
	private void updateEnteredApplicationTime(){
		lastEnteredApplicationTime = System.currentTimeMillis();
	}

	private class ClickedCommunity{
		
		private String Global_ID;
		private long lastTimeClicked;
		
		
		public ClickedCommunity(String Global_ID, long lastTimeClicked){
			
			this.Global_ID = Global_ID;
			this.lastTimeClicked = lastTimeClicked;
			
		}
		
		public String getGlobal_ID(){
			return Global_ID;
		}
		
		public void updateTimeClicked(){
			lastTimeClicked = System.currentTimeMillis();
		
		}
		public long getTimeClicked(){
			return lastTimeClicked;
		}
		
	}
	
}
