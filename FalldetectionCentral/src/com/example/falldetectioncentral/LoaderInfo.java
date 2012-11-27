package com.example.falldetectioncentral;

public class LoaderInfo {

	
	private String query;
	private int loader_id;
	
	
		public LoaderInfo(String query, int loader_id){
			this.setQuery(query);
			this.setLoader_id(loader_id);
		}


		public int getLoader_id() {
			return loader_id;
		}


		public void setLoader_id(int loader_id) {
			this.loader_id = loader_id;
		}


		public String getQuery() {
			return query;
		}


		public void setQuery(String query) {
			this.query = query;
		}

}
