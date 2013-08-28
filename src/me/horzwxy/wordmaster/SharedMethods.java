package me.horzwxy.wordmaster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import me.horzwxy.wordservant.Word;
import me.horzwxy.wordservant.WordLibrary;
import me.horzwxy.wordservant.Word.WordType;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.view.MenuItem;

final class SharedMethods {
	
	static Word word = null;
	static WordLibrary wordLib = null;
	static me.horzwxy.wordmaster.ShowWordActivity.WordResult wordResult = null;
	static ArrayList< Activity > activities = new ArrayList< Activity >();
	
	private SharedMethods() {}
	
	static HashMap< String, Word > loadWords( InputStream fis ) {
    	// set English Locale
    	HashMap< String, Word > words = new HashMap< String, Word >();
    	try {
    		Locale.setDefault( new Locale( "hello" ) );
    		
			// set up XML parser
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmlDoc = db.parse( fis );
			
			// get the word node list
			NodeList list = xmlDoc.getElementsByTagName( "word" );
			for( int i = 0; i < list.getLength(); i++ ) {
				Node e = list.item( i );
				NodeList childList = e.getChildNodes();
				
				Word w = new Word( null );
				
				// initialize the words
				for( int j = 0; j < childList.getLength(); j++ ) {
					String nodeName = childList.item( j ).getNodeName();
					if( nodeName.equals( "english" ) ) {
						w.setEnglishContent( childList.item( j ).getTextContent().toLowerCase() );
					}
					else if( nodeName.equals( "type" ) ) {
						String typeString = childList.item( j ).getTextContent();
						w.setType( WordType.valueOf( typeString ) );
					}
					else if( nodeName.equals( "sentences" ) ) {
						NodeList sNodeList = childList.item( j ).getChildNodes();	// children of <sentences>
						for( int k = 0; k < sNodeList.getLength(); k++ ) {
							if( sNodeList.item( k ).getNodeName().equals( "sentence" ) ) {
								String s = sNodeList.item( k ).getTextContent();
								w.addSentence( s );
							}
						}
					}
					else if( nodeName.equals( "in_time" ) ) {
						w.setIn_time( DateFormat.getInstance().parse( childList.item( j ).getTextContent() ) );
					}
					else if( nodeName.equals( "last_time" ) ) {
						w.setLast_time( DateFormat.getInstance().parse( childList.item( j ).getTextContent() ) );
					}
					else if( nodeName.equals( "total_times" ) ) {
						w.setTotal_times( Integer.parseInt( childList.item( j ).getTextContent() ) );
					}
					else if( nodeName.equals( "peak_times" ) ) {
						w.setPeak_times( Integer.parseInt( childList.item( j ).getTextContent() ) );
					}
					else if( nodeName.equals( "article_frequency" ) ) {
						w.setArticle_frequency( Integer.parseInt( childList.item( j ).getTextContent() ) );
					}
					else if( nodeName.equals( "#text" ) ) {
						continue;
					}
					else {
						throw new RuntimeException( "XML parsing error: unknown element " + nodeName );
					}
				}
				
				// what did android suggest?
				words.put( w.getEnglishContent().toLowerCase(), w );
			}
		} catch (FileNotFoundException e) {
			System.out.println( "XML word list file not found" );
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	return words;
    }
	
	static boolean sharedMenuEventHandler( MenuItem item, Activity activity ) {
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
    	case R.id.menu_saveLibraries: saveWords( "/news.xml", wordLib.getNewWordList() );

			saveWords( "/knows.xml", wordLib.getKnownList() );
    		saveWords( "/ignores.xml", wordLib.getIgnoreList() );
    		break;
    	default: break;
    	}
    	return true;
	}
	
	private static void saveWords( String filename, HashMap< String, Word > list ) {
		Locale.setDefault( new Locale( "hello" ) );
		
    	try {
    		// create new word list file
    		File directory = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "wordmaster" );
            if( !directory.exists() ) {
            	directory.mkdir();
            }
            File file = new File( directory.getAbsolutePath() + filename );
            if( file.exists() ) {
            	file.delete();
            }
            
            // if there is no words
            if( list.size() == 0 ) {
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
			
			ArrayList< Word > wl = new ArrayList< Word >( list.values() );
			
			if( wl.size() == 0 ) {
				return;
			}
			else {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for( int i = 0; i < wl.size(); i++ ) {
				Word w = wl.get( i );
				String wordString = w.getEnglishContent();
				
				// set the elements
				Element word = xmlDoc.createElement( "word" );
				Element english = xmlDoc.createElement( "english" );
				english.setTextContent( wordString );
				word.appendChild( english );
				Element type = xmlDoc.createElement( "type" );
				type.setTextContent( w.getType().toString() );
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
				in_time.setTextContent( DateFormat.getInstance().format( w.getIn_time() ) );
				word.appendChild( in_time );
				// set last_time
				Element last_time = xmlDoc.createElement( "last_time" );
				last_time.setTextContent( DateFormat.getInstance().format( w.getLast_time() ) );
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
			result.setOutputStream( new FileOutputStream( file ) );
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
