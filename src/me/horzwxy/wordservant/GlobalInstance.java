package me.horzwxy.wordservant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import me.horzwxy.wordmaster.R;
import me.horzwxy.wordmaster.ShowNewlyAddedActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.view.MenuItem;

public final class GlobalInstance {
	
	public static Word word = null;
	public static WordLibrary wordLib = null;
	public static me.horzwxy.wordmaster.ShowWordActivity.WordResult wordResult = null;
	public static ArrayList< Activity > activities = new ArrayList< Activity >();
	
	private GlobalInstance() {}
	
	public static boolean sharedMenuEventHandler( MenuItem item, Activity activity ) {
		switch( item.getItemId() ) {
		case R.id.menu_newlyAdded: { Intent intent = new Intent( activity, ShowNewlyAddedActivity.class );
			activity.startActivity( intent );
			break;
		}
    	case R.id.menu_quit: {
    		for( int i = 0; i < activities.size(); i++ ) {
    			activities.get( i ).finish();
    		}
    	}
    	case R.id.menu_action_settings: break;
    	case R.id.menu_saveLibraries: GlobalInstance.saveLibraries();
    		break;
    	default: break;
    	}
    	return true;
	}
	
	public static void saveLibraries() {
		saveNewWords();
		saveIgnoreWords();
		saveKnownWords();
	}
	
	private static void saveNewWords() {
    	try {
    		// create new word list file
    		File directory = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "wordmaster" );
            if( !directory.exists() ) {
            	directory.mkdir();
            }
            File newWordFile = new File( directory.getAbsolutePath() + "/news.xml" );
            if( newWordFile.exists() ) {
            	newWordFile.delete();
            }
            try {
            	newWordFile.createNewFile();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            
            // if there is no new words
            if( GlobalInstance.wordLib.getNewWordList().size() == 0 ) {
            	return;
            }
            
			// set up xml doc factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmlDoc = db.newDocument();
			
			Element root = xmlDoc.createElement( "words" );
			xmlDoc.appendChild( root );
			
			// set up xml builder
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty( "indent", "yes" );
			DOMSource source = new DOMSource();
			source.setNode( xmlDoc );
			
			ArrayList< Word > wl = new ArrayList< Word >( GlobalInstance.wordLib.getNewWordList().values() );
			
			for( int i = 0; i < wl.size(); i++ ) {
				Word w = wl.get( i );
				String wordString = w.getEnglishContent();
				
				// set the elements
				Element word = xmlDoc.createElement( "word" );
				Element english = xmlDoc.createElement( "english" );
				english.setTextContent( wordString );
				word.appendChild( english );
				Element type = xmlDoc.createElement( "type" );
				type.setTextContent( "normal" );
				word.appendChild( type );
				// set the sentences
				Element sentences = xmlDoc.createElement( "sentences" );
				word.appendChild( sentences );
				for( int j = 0; j < w.getSentences().size(); j++) {
					Element sentence = xmlDoc.createElement( "sentence" );
					sentence.setTextContent( w.getSentences().get( j ) );
					sentences.appendChild( sentence );
				}
				// set in_time
				Element in_time = xmlDoc.createElement( "in_time" );
				in_time.setTextContent( w.getIn_time() );
				word.appendChild( in_time );
				// set last_time
				Element last_time = xmlDoc.createElement( "last_time" );
				last_time.setTextContent( w.getLast_time() );
				word.appendChild( last_time );
				// set total_times
				Element total_times = xmlDoc.createElement( "total_times" );
				total_times.setTextContent( w.getTotal_times() + "" );
				word.appendChild( total_times );
				// set peak_times
				Element peak_times = xmlDoc.createElement( "peak_times" );
				peak_times.setTextContent( w.getPeak_times() + "" );
				word.appendChild( peak_times );
				// set article_frequency
				Element article_frequency = xmlDoc.createElement( "article_frequency" );
				article_frequency.setTextContent( w.getArticle_frequency() + "" );
				word.appendChild( article_frequency );
				
				root.appendChild( word );
			}
			
			StreamResult result = new StreamResult();
			result.setOutputStream( new FileOutputStream( newWordFile ) );
			t.transform( source, result );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private static void saveIgnoreWords() {
    	try {
    		// create directory
    		File directory = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "wordmaster" );
            if( !directory.exists() ) {
            	directory.mkdir();
            }
            // create ignore word file
            File ignoreFile = new File( directory.getAbsolutePath() + "/ignores.xml" );
            if( ignoreFile.exists() ) {
            	ignoreFile.delete();
            }
            try {
            	ignoreFile.createNewFile();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            
            // if there is no ignored words
            if( GlobalInstance.wordLib.getIgnoreList().size() == 0 ) {
            	return;
            }
            
			// set up xml doc factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmlDoc = db.newDocument();
			
			Element root = xmlDoc.createElement( "words" );
			xmlDoc.appendChild( root );
			
			// set up xml builder
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty( "indent", "yes" );
			DOMSource source = new DOMSource();
			source.setNode( xmlDoc );
			
			ArrayList< Word > wl = new ArrayList< Word >( GlobalInstance.wordLib.getIgnoreList().values() );
			
			for( int i = 0; i < wl.size(); i++ ) {
				Word w = wl.get( i );
				String wordString = w.getEnglishContent();
				
				// set the elements
				Element word = xmlDoc.createElement( "word" );
				Element english = xmlDoc.createElement( "english" );
				english.setTextContent( wordString );
				word.appendChild( english );
				
				root.appendChild( word );
			}
			
			StreamResult result = new StreamResult();
			result.setOutputStream( new FileOutputStream( ignoreFile ) );
			t.transform( source, result );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private static void saveKnownWords() {
    	try {
    		// create directory
    		File directory = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "wordmaster" );
            if( !directory.exists() ) {
            	directory.mkdir();
            }
            // create ignore word file
            File ignoreFile = new File( directory.getAbsolutePath() + "/known.xml" );
            if( ignoreFile.exists() ) {
            	ignoreFile.delete();
            }
            try {
            	ignoreFile.createNewFile();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            
            // if there is no ignored words
            if( GlobalInstance.wordLib.getIgnoreList().size() == 0 ) {
            	return;
            }
            
			// set up xml doc factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmlDoc = db.newDocument();
			
			Element root = xmlDoc.createElement( "words" );
			xmlDoc.appendChild( root );
			
			// set up xml builder
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty( "indent", "yes" );
			DOMSource source = new DOMSource();
			source.setNode( xmlDoc );
			
			ArrayList< Word > wl = new ArrayList< Word >( GlobalInstance.wordLib.getKnownList().values() );
			
			for( int i = 0; i < wl.size(); i++ ) {
				Word w = wl.get( i );
				String wordString = w.getEnglishContent();
				
				// set the elements
				Element word = xmlDoc.createElement( "word" );
				Element english = xmlDoc.createElement( "english" );
				english.setTextContent( wordString );
				word.appendChild( english );
				
				root.appendChild( word );
			}
			
			StreamResult result = new StreamResult();
			result.setOutputStream( new FileOutputStream( ignoreFile ) );
			t.transform( source, result );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
