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

public class InputArticleActivity extends WMActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_input_article );
        activities.add( InputArticleActivity.this );
        
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
}
