package me.horzwxy.wordmaster;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.horzwxy.wordservant.Word;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
		
		SharedMethods.activities.add( ShowNewlyAddedActivity.this );
		list = new ArrayList< Word >( SharedMethods.wordLib.getNewWordList().values() );
		sortByDate( list );
		
		ArrayList< String > items = new ArrayList< String >();
		for( int i = 0; i < list.size(); i++ ) {
			items.add( list.get( i ).getEnglishContent() );
		}
		
        ListView listView = ( ListView )findViewById( R.id.word_list );
		listView.setAdapter( new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, items ) );
		listView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				SharedMethods.word = list.get( position );
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
    	return SharedMethods.sharedMenuEventHandler( item, this );
    }
    
    private static void sortByDate( List< Word > list ) {
    	for( int i = 0; i < list.size() - 1; i++ ) {
    		for( int j = 0; j < list.size() - 1 - i; j++ ) {
    			Word w1 = list.get( j );
    			Word w2 = list.get( j + 1 );
    			// if w1 comes into word list before w2
    			if( w1.getIn_time().compareTo( w2.getIn_time() ) < 0 ) {
    				// swap w2 and w1
    				list.remove( j );
    				list.remove( j );
    				list.add( j, w1 );
    				list.add( j, w2 );
    			}
    		}
    	} 
    }
}
