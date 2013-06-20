package me.horzwxy.wordmaster;

import java.util.ArrayList;

import me.horzwxy.wordmaster.ShowWordActivity.WordResult;
import me.horzwxy.wordservant.GlobalInstance;
import me.horzwxy.wordservant.Word;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WordInfoActivity extends Activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_wordinfo );
		GlobalInstance.activities.add( WordInfoActivity.this );
		
		Word word = GlobalInstance.word;
		
		TextView wordContentView = ( TextView )findViewById( R.id.wordinfo_word );
		wordContentView.setText( word.getEnglishContent() );
		
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
    	return GlobalInstance.sharedMenuEventHandler( item, this );
    }
}
