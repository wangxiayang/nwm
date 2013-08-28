package me.horzwxy.wordmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InputArticleActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_input_article );
        SharedMethods.activities.add( InputArticleActivity.this );
        
        Button submitButton = ( Button )findViewById( R.id.input_article_submit );
        submitButton.setOnClickListener( new OnClickListener() {
        	public void onClick( View v ) {
        		EditText e = ( EditText )findViewById( R.id.input_article_edittext );
        		String article = e.getText().toString();
        		
        		Intent intent = new Intent( getApplicationContext(), ShowWordActivity.class );
        		Bundle extras = new Bundle();
        		extras.putString( "article", article );
        		intent.putExtras( extras );
        		startActivity( intent );
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
}
