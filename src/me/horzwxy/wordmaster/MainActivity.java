package me.horzwxy.wordmaster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import me.horzwxy.wordservant.Word.WordType;
import me.horzwxy.wordservant.WordLibrary;
import me.horzwxy.wordservant.GlobalInstance;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		GlobalInstance.activities.add( MainActivity.this );
        
        Button articleButton = ( Button )findViewById( R.id.main_article );
        articleButton.setOnClickListener( new OnClickListener() {
    		public void onClick( View v ) {
    			Intent intent = new Intent( getApplicationContext(), InputArticleActivity.class );
    			startActivity( intent );
    		} } );
        
        // parse xml db file
        WordLibrary lib = new WordLibrary( loadBaseWords(), loadIgnoreWords(), loadNewWords(), loadKnownWords() );
		GlobalInstance.wordLib = lib;
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
    
    private HashMap< String, Word > loadBaseWords() {
    	HashMap< String, Word > words = new HashMap< String, Word >();
    	try {
			// set up XML parser
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmlDoc = db.parse( getResources().openRawResource( R.raw.words ) );
			
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
						if( typeString.equals( "spot name" ) ) {
							w.setType( WordType.SPOT_NAME );
						}
						else if( typeString.equals( "person name" ) ) {
							w.setType( WordType.PERSON_NAME );
						}
						else if( typeString.equals( "normal" ) ) {
							w.setType( WordType.NORMAL );
						}
					}
					else if( nodeName.equals( "sentences" ) ) {
						NodeList sNodeList = childList.item( j ).getChildNodes();	// children of <sentences>
						for( int k = 0; k < sNodeList.getLength(); k++ ) {
							String s = sNodeList.item( k ).getTextContent();
							w.addSentence( s );
						}
					}
					else if( nodeName.equals( "in_time" ) ) {
						w.setIn_time( childList.item( j ).getTextContent() );
					}
					else if( nodeName.equals( "last_time" ) ) {
						w.setLast_time( childList.item( j ).getTextContent() );
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
		}
    	
    	return words;
    }
    
    private HashMap< String, Word > loadNewWords() {
    	File directory = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "wordmaster" );
    	if( !directory.exists() ) {
    		try {
				directory.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	File newWordFile = new File( directory.getAbsolutePath() + "/news.xml" );
    	if( !newWordFile.exists() ) {
    		return new HashMap< String, Word >();
    	}
    	
    	HashMap< String, Word > words = new HashMap< String, Word >();
    	try {
			// set up XML parser
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmlDoc = db.parse( new FileInputStream( newWordFile ) );
			
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
						if( typeString.equals( "spot name" ) ) {
							w.setType( WordType.SPOT_NAME );
						}
						else if( typeString.equals( "person name" ) ) {
							w.setType( WordType.PERSON_NAME );
						}
						else if( typeString.equals( "normal" ) ) {
							w.setType( WordType.NORMAL );
						}
					}
					else if( nodeName.equals( "sentences" ) ) {
						NodeList sNodeList = childList.item( j ).getChildNodes();	// children of <sentences>
						for( int k = 0; k < sNodeList.getLength(); k++ ) {
							String s = sNodeList.item( k ).getTextContent();
							w.addSentence( s );
						}
					}
					else if( nodeName.equals( "in_time" ) ) {
						w.setIn_time( childList.item( j ).getTextContent() );
					}
					else if( nodeName.equals( "last_time" ) ) {
						w.setLast_time( childList.item( j ).getTextContent() );
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
		}
    	
    	return words;
    }
    
    private HashMap< String, Word > loadIgnoreWords() {
    	File directory = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "wordmaster" );
    	if( !directory.exists() ) {
    		try {
				directory.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	File newWordFile = new File( directory.getAbsolutePath() + "/ignores.xml" );
    	if( !newWordFile.exists() ) {
    		return new HashMap< String, Word >();
    	}
    	
    	HashMap< String, Word > words = new HashMap< String, Word >();
    	try {
			// set up XML parser
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmlDoc = db.parse( new FileInputStream( newWordFile ) );
			
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
		}
    	
    	return words;
    }
    
    private HashMap< String, Word > loadKnownWords() {
    	File directory = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "wordmaster" );
    	if( !directory.exists() ) {
    		try {
				directory.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	File newWordFile = new File( directory.getAbsolutePath() + "/knowns.xml" );
    	if( !newWordFile.exists() ) {
    		return new HashMap< String, Word >();
    	}
    	
    	HashMap< String, Word > words = new HashMap< String, Word >();
    	try {
			// set up XML parser
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmlDoc = db.parse( new FileInputStream( newWordFile ) );
			
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
		}
    	
    	return words;
    }
}
