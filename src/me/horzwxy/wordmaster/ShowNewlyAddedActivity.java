package me.horzwxy.wordmaster;

import java.util.ArrayList;
import java.util.TreeSet;

import me.horzwxy.wordservant.GlobalInstance;
import me.horzwxy.wordservant.Word;
import me.horzwxy.wordservant.WordLibrary;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowNewlyAddedActivity extends Activity {

	private ArrayList< Word > list;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_word_list);
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		
		GlobalInstance.activities.add( ShowNewlyAddedActivity.this );
		list = new ArrayList< Word >( GlobalInstance.wordLib.getNewWordList().values() );
		ArrayList< String > items = new ArrayList< String >();
		for( int i = 0; i < list.size(); i++ ) {
			items.add( list.get( i ).getEnglishContent() );
		}
		
        ListView listView = ( ListView )findViewById( R.id.word_list );
		listView.setAdapter( new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, items ) );
		listView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				GlobalInstance.word = list.get( position );
				Intent intent = new Intent( ShowNewlyAddedActivity.this, WordInfoActivity.class );
				startActivityForResult( intent, position );
			}
		} );
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    	return GlobalInstance.sharedMenuEventHandler( item, this );
    }
}
