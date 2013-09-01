package me.horzwxy.wordmaster;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.horzwxy.wordservant.TimeOrderedWordSet;
import me.horzwxy.wordservant.Word;
import me.horzwxy.wordservant.WordLibrary;

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

public class ShowNewlyAddedActivity extends WMActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_word_list);
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		
		activities.add( this );
		TimeOrderedWordSet wordSet = tempStorage.getUnfamiliarSet();
		
		final ArrayList< String > items = new ArrayList< String >();
        Word word = wordSet.pollLast();
        while ( word != null ) {
            items.add( word.getContent() );
            word = wordSet.pollLast();
		}
		
        ListView listView = ( ListView )findViewById( R.id.word_list );
		listView.setAdapter( new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, items ) );
		listView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				Intent intent = new Intent( ShowNewlyAddedActivity.this, WordInfoActivity.class );
                intent.putExtra( "word", items.get( position ) );
				startActivityForResult( intent, position );
			}
		} );
	}
}
