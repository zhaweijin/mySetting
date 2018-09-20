/**
 * 
 */
package com.hiveview.cloudtv.settings.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.hiveview.cloudtv.settings.SettingsApplication;
import com.hiveview.cloudtv.settings.imageprofile.TimeZoneEntity;

/**
 * @author dell-pc
 *
 */
public class LoadConfigTask extends  AsyncTask<Void,Void,String>{

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	final String CONTENT_URI = "content://HiveViewGuideAuthorities/activation";
	private ContentResolver contentResolver;
	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		SettingsApplication.getInstance().status = 100 ;
		Log.i("SettingsApplication", "---doInBackground--"+SettingsApplication.getInstance().status);
		//getEntity();
		getJson();
		return null;
	}
	
	
	private void getEntity() {
	       /* Cursor cursor = SettingsApplication.getInstance().getContentResolver().query(Uri.parse("content://HiveViewGuideAuthorities/TIME_ZONE"), null, null, null, null);

	        ArrayList<TimeZoneEntity> entities = new ArrayList<TimeZoneEntity>();


	        while (cursor.moveToNext()) {
	            TimeZoneEntity entity = new TimeZoneEntity();
	            entity.setId(cursor.getInt(cursor.getColumnIndex("id")));
	            entity.setName(cursor.getString(cursor.getColumnIndex("name")));
	            entities.add(entity);
	        }

	        cursor.close();
            SettingsApplication.getInstance().entities = entities;
	        Log.d("SettingsApplication", "getEntity---> " + entities.toString());*/
	        String str = "{'area':'usa','daylight':'true','pay':'0','state':'N000000','time_zone':[{'id':1,'name':'东八区'},{'id':2,'name':'东九区'},{'id':3,'name':'东十区'}]}";
	        try {
				JSONObject json = new JSONObject(str);
				JSONArray jsonArray = json.getJSONArray("time_zone");
				//for(int i=)
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	
	private void getJson() {
        Cursor cursor = SettingsApplication.getInstance().getContentResolver().query(Uri.parse("content://HiveViewGuideAuthorities/ACTIVATION"), null, null, null, null);
        if(null == cursor)
        	return;
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            buffer.append(cursor.getString(cursor.getColumnIndex("config")));
        }

        cursor.close();

        String configJSON = buffer.toString();
        
        
        JSONObject json;
		try {
			if(configJSON!=null&&!"".equals(configJSON)){
				    //TODO
			json = new JSONObject(configJSON);
			JSONArray jsonArray = json.getJSONArray("time_zone");
			 ArrayList<TimeZoneEntity> list =new ArrayList<TimeZoneEntity>();
			for(int i =0;i<jsonArray.length();i++){
				TimeZoneEntity entity = new TimeZoneEntity();
				JSONObject json2 = (JSONObject) jsonArray.get(i);
				entity.setId(json2.getString("id"));
				entity.setName(json2.getString("name"));
				list.add(entity);
			}
            SettingsApplication.getInstance().entities = list;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

        Log.d("SettingsApplication", "getJSON---> " + configJSON);
    }
}
