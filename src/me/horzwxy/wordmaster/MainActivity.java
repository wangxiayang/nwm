package me.horzwxy.wordmaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import me.horzwxy.wordservant.Word;
import me.horzwxy.wordservant.WordLibrary;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedMethods.activities.add( MainActivity.this );
        
        Button articleButton = ( Button )findViewById( R.id.main_article );
        articleButton.setOnClickListener( new OnClickListener() {
    		public void onClick( View v ) {
    			Intent intent = new Intent( getApplicationContext(), InputArticleActivity.class );
    			startActivity( intent );
    		} } );
        
        // parse xml db file
        HashMap< String, Word > baseWords = SharedMethods.loadWords( getResources().openRawResource( R.raw.words ) );
        
        File directory = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "wordmaster" );
    	if( !directory.exists() ) {
    		try {
				directory.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	// ignored words list
    	HashMap< String, Word > ignoreWords = null;
    	File ignoreFile = new File( directory.getAbsolutePath() + "/ignores.xml" );
    	if( !ignoreFile.exists() ) {
    		ignoreWords = new HashMap< String, Word >();
    	}
    	else {
    		try {
				ignoreWords = SharedMethods.loadWords( new FileInputStream( ignoreFile ) );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	// newly added words list
        HashMap< String, Word > newWords = null;
        File newFile = new File( directory.getAbsolutePath() + "/news.xml" );
    	if( !newFile.exists() ) {
    		newWords = new HashMap< String, Word >();
    	}
    	else {
    		try {
				newWords = SharedMethods.loadWords( new FileInputStream( newFile ) );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	// already known words list
        HashMap< String, Word > knownWords = null;
        File knownFile = new File( directory.getAbsolutePath() + "/knows.xml" );
    	if( !knownFile.exists() ) {
    		knownWords = new HashMap< String, Word >();
    	}
    	else {
    		try {
				knownWords = SharedMethods.loadWords( new FileInputStream( knownFile ) );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	WordLibrary lib = new WordLibrary( baseWords, ignoreWords, newWords, knownWords );
        SharedMethods.wordLib = lib;
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
