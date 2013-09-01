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

public class MainActivity extends WMActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activities.add( this );
        
        Button articleButton = ( Button )findViewById( R.id.main_article );
        articleButton.setOnClickListener( new OnClickListener() {
    		public void onClick( View v ) {
    			Intent intent = new Intent( getApplicationContext(), InputArticleActivity.class );
    			startActivity( intent );
    		} } );

        loadWords();
    }
}
