package com.amap.safone.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amap.safone.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 设备列表类
 * @author zk
 *
 */
public class ListViewChat  extends  android.widget.ListView{	
	MyAdapter listItemAdapter; 	
    Context m_context;
    List<Map<String, Object>> listItems;
    View footerView;
    private LayoutInflater mInflater;
    
    public ListViewChat(Context context) {
		super(context);
		this.mInflater = LayoutInflater.from(context);
		m_context= context;
	}
    public ListViewChat(Context context, AttributeSet attrs) {
		super(context,attrs);
		m_context= context;
	}   
    public void Bulid(){
    	listItems = null;
    	listItemAdapter = null;
    	listItems = new ArrayList<Map<String, Object>>();
		listItemAdapter = new MyAdapter(m_context,listItems);		
        this.setAdapter(listItemAdapter);	    	
    }
    public void reBulid(){
    	listItems = null;
    	listItemAdapter = null;
    	listItems = new ArrayList<Map<String, Object>>();
		listItemAdapter = new MyAdapter(m_context,listItems);
        this.setAdapter(listItemAdapter);	    	
    }    
   
    public void addItem(int id,String addr,double lat,double lng)
    {
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	map.put("id", String.valueOf(id));
    	map.put("addr", addr);
    	map.put("lat", String.valueOf(lat));
    	map.put("lng", String.valueOf(lng));
    	listItems.add(map);
    	listItemAdapter.notifyDataSetChanged();
    }  
    
    public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);
	}

    
    public void clear(){
    	listItems.clear();
    	listItemAdapter.notifyDataSetChanged();
    }
    
    public void notifyData(){
    	listItemAdapter.notifyDataSetInvalidated();	
    }
    
	public class MyAdapter extends BaseAdapter{
		private Context context;                        
		private List<Map<String, Object>> listItems;    
		private LayoutInflater listContainer;          
		private int  selectItem=-1;
		
		public MyAdapter(Context context, List<Map<String, Object>> listItems) {   
			this.context = context;            
			listContainer = LayoutInflater.from(context);  
			this.listItems = listItems;  
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return listItems.size();
		}
 
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listItems.get(position);
		}
 
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public  void setSelectItem(int selectItem) {   
            this.selectItem = selectItem;   
        }  
		
		public int getSelectItem(){
        	return this.selectItem;
        }		
	    public View getView(int position, View convertView, ViewGroup parent) {
	        final int selectID = position; 
			ListViewItem  listItemView = null;
			if (convertView == null) {  
				listItemView = new ListViewItem();
			 	        	
	            convertView = listContainer.inflate(R.layout.listviewchat, null);
				listItemView.ItemTitle = (TextView)convertView.findViewById(R.id.tv_addr); 			
				convertView.setTag(listItemView);  
			}
			else{
				listItemView = (ListViewItem)convertView.getTag();
			}

				listItemView.ItemTitle.setText((String) listItems.get(position)   
						.get("addr")); 
			
				return convertView;
	    }
 
	}   
	
	public class ListViewItem{
		public TextView ItemTitle;
	}    
	
	
}
