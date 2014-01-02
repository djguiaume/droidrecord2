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
import android.view.View;
import android.widget.ListView;

public class FileChooser extends ListActivity {

    private File currentDir;
    private FileArrayAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File(Environment.getExternalStorageDirectory()+File.separator+"DroidRecorder");;
        fill(currentDir);
    }
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
                 this.setTitle("Current Dir: "+f.getName());
                 List<Item>dir = new ArrayList<Item>();
                 List<Item>fls = new ArrayList<Item>();
                 try{
                         for(File ff: dirs)
                         {
                                Date lastModDate = new Date(ff.lastModified());
                                DateFormat formater = DateFormat.getDateTimeInstance();
                                String date_modify = formater.format(lastModDate);
                                if(ff.isDirectory()){
                                       
                                       
                                        File[] fbuf = ff.listFiles();
                                        int buf = 0;
                                        if(fbuf != null){
                                                buf = fbuf.length;
                                        }
                                        else buf = 0;
                                        String num_item = String.valueOf(buf);
                                        if(buf == 0) num_item = num_item + " item";
                                        else num_item = num_item + " items";
                                       
                                        //String formated = lastModDate.toString();
                                        dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon"));
                                }
                                else
                                {
                                        fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon"));
                                }
                         }
                 }catch(Exception e)
                 {   
                         
                 }
                 Collections.sort(dir);
                 Collections.sort(fls);
                 dir.addAll(fls);
                 adapter = new FileArrayAdapter(FileChooser.this,R.layout.file_view,dir);
                 this.setListAdapter(adapter);
    }
    @Override
        protected void onListItemClick(ListView l, View v, int position, long id) {
                // TODO Auto-generated method stub
                super.onListItemClick(l, v, position, id);
                Item o = adapter.getItem(position);
                onFileClick(o);
        }
    private void onFileClick(Item o)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
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
}