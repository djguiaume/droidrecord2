package com.persil.droidrecorder;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.DateFormat;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.persil.droidrecorder2.R;

public class FileChooser extends ListActivity {

    private File currentDir;
    private FileArrayAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File(Environment.getExternalStorageDirectory()+File.separator+"DroidRecorder");;
        fill(currentDir);
        Log.d("filechooser", "orientation = "+ this.getResources().getConfiguration().orientation);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        	getWindow().setBackgroundDrawableResource(R.drawable.back_l);
        else
        	getWindow().setBackgroundDrawableResource(R.drawable.back);
    }
    
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
                 List<Item>fls = new ArrayList<Item>();
                 try{
                         for(File ff: dirs)
                         {
                                Date lastModDate = new Date(ff.lastModified());
                                DateFormat formater = DateFormat.getDateTimeInstance();
                                String date_modify = formater.format(lastModDate);
                                fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"note"));
                         }
                 }catch(Exception e)
                 {   
                         
                 }
                 Collections.sort(fls);
                 adapter = new FileArrayAdapter(FileChooser.this,R.layout.file_view,fls);
                 this.setListAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            Item o = adapter.getItem(position);
            onFileClick(o);
    }
    
    private void onFileClick(Item o)
    {
        Intent intent = new Intent(this, PlayerView.class);
        intent.putExtra("GetFileName",o.getName());
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
	
    @SuppressLint("NewApi") @Override
    public boolean	onNavigateUp() {
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
	    	boolean ret = super.onNavigateUp();
	        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			return ret;
    	}
       return false;
    }
    
	@Override
	public void onResume()
	{
		fill(currentDir);
        getWindow().setBackgroundDrawableResource(R.drawable.back);
        super.onResume();
	}
}