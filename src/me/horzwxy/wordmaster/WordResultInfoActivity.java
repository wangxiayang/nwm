package me.horzwxy.wordmaster;

import java.util.ArrayList;
import java.util.List;

import me.horzwxy.wordmaster.ShowWordActivity.WordResult;
import me.horzwxy.wordservant.GlobalInstance;
import me.horzwxy.wordservant.Word;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class WordResultInfoActivity extends Activity {
	
	private RadioGroup rbg;
	private List< String > forms;
	private ArrayList< String > sentences;
	private ArrayList< String > backstore;
	private boolean submitted = false;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_wordresultinfo );
		GlobalInstance.activities.add( WordResultInfoActivity.this );
		
		setResult( 0, null );
		
		WordResult wordresultinfo = GlobalInstance.wordResult;
		
		TextView wordContentView = ( TextView )findViewById( R.id.wordresultinfo_word );
		wordContentView.setText( wordresultinfo.getContent() );
		
		rbg = ( RadioGroup )findViewById( R.id.wordresultinfo_forms );
		
		forms = wordresultinfo.getForms();
		for( int i = 0; i < forms.size(); i++ ) {
			RadioButton button = new RadioButton( this );
			button.setText( forms.get( i ) );
			button.setTag( ( Object )( "" + i ) );
			rbg.addView( button );
		}
		
		sentences = wordresultinfo.getSentences();
		backstore = new ArrayList< String >();
		final ListView listView = ( ListView )findViewById( R.id.wordresultinfo_sentences );
		listView.setAdapter( new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, sentences ) );
		
		listView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				//sentences.remove( position );
				backstore.add( sentences.get( position ) );
				sentences.remove( position );
				listView.setAdapter( new ArrayAdapter< String >( WordResultInfoActivity.this, android.R.layout.simple_list_item_1, sentences ) );
				listView.invalidate();
				
			}
		} );
		
		Button submit = ( Button )findViewById( R.id.wordresultinfo_submit );
		submit.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				int id = rbg.getCheckedRadioButtonId();
				
				if( id == -1 ) {
					Toast.makeText( WordResultInfoActivity.this, "You must choose one of the forms!", Toast.LENGTH_SHORT );
					return;
				}
				String indexString = ( String )( ( ( Button )findViewById( id ) ).getTag() );
				int index = Integer.parseInt( indexString );
				if( index == -1 ) {
					return;
				}
				String form = forms.get( index );
				Word w = new Word( form );
				w.setSentences( GlobalInstance.wordResult.getSentences() );
				GlobalInstance.wordLib.addNewWord( w );

				Toast.makeText( WordResultInfoActivity.this, form + " added successfully!", Toast.LENGTH_SHORT ).show();
				setResult( 1, null );
				submitted = true;
				finish();
			}
		} );
		
		Button ignore = ( Button )findViewById( R.id.wordresultinfo_ignore );
		ignore.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				int id = rbg.getCheckedRadioButtonId();
				if( id == -1 ) {
					Toast.makeText( WordResultInfoActivity.this, "You must choose one of the forms!", Toast.LENGTH_SHORT ).show();
					return;
				}
				String indexString = ( String )( ( ( Button )findViewById( id ) ).getTag() );
				int index = Integer.parseInt( indexString );
				String form = forms.get( index );
				Word w = new Word( form );
				GlobalInstance.wordLib.addIgnoredWord( w );
				Toast.makeText( WordResultInfoActivity.this, form + " ignored successfully!", Toast.LENGTH_SHORT ).show();
				setResult( 1, null );
				finish();
			}
		} );
		
		Button known = ( Button )findViewById( R.id.wordresultinfo_alreadyKnown );
		known.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				int id = rbg.getCheckedRadioButtonId();
				if( id == -1 ) {
					Toast.makeText( WordResultInfoActivity.this, "You must choose one of the forms!", Toast.LENGTH_SHORT ).show();
					return;
				}
				String indexString = ( String )( ( ( Button )findViewById( id ) ).getTag() );
				int index = Integer.parseInt( indexString );
				String form = forms.get( index );
				Word w = new Word( form );
				GlobalInstance.wordLib.addIgnoredWord( w );
				Toast.makeText( WordResultInfoActivity.this, form + " known successfully!", Toast.LENGTH_SHORT ).show();
				setResult( 1, null );
				finish();
			}
		} );
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if( !submitted ){
			sentences.addAll( backstore );
		}
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
