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

public class WordInfoActivity extends WMActivity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_wordinfo );
		activities.add( this );

        String wordContent = "" + getIntent().getCharSequenceExtra( "word" );
		Word word = wordLib.getWord( wordContent );
		
		TextView wordContentView = ( TextView )findViewById( R.id.wordinfo_word );
		wordContentView.setText( wordContent );
		
		TextView wordIntimeView = ( TextView )findViewById( R.id.wordinfo_intime );
		wordIntimeView.setText( word.getIn_time().toString() );
		
		ListView listView = ( ListView )findViewById( R.id.wordinfo_sentences );
		ArrayList< String > items = word.getSentences();
		
		listView.setAdapter( new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, items ) );
	}
}
