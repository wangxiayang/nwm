package me.horzwxy.wordmaster;

import java.util.ArrayList;

import me.horzwxy.wordservant.Word;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WordInfoActivity extends Activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_wordinfo );
		SharedMethods.activities.add( WordInfoActivity.this );
		
		Word word = SharedMethods.word;
		
		TextView wordContentView = ( TextView )findViewById( R.id.wordinfo_word );
		wordContentView.setText( word.getEnglishContent() );
		
		TextView wordIntimeView = ( TextView )findViewById( R.id.wordinfo_intime );
		wordIntimeView.setText( word.getIn_time().toString() );
		
		ListView listView = ( ListView )findViewById( R.id.wordinfo_sentences );
		ArrayList< String > items = word.getSentences();
		
		listView.setAdapter( new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, items ) );
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
}
