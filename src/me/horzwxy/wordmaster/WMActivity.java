package me.horzwxy.wordmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
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
import me.horzwxy.wordservant.WordState;
import me.horzwxy.wordservant.WordTempStorage;

/**
 * Created by horz on 9/1/13.
 */
public class WMActivity extends Activity {
    protected static WordTempStorage tempStorage;
    protected static WordLibrary wordLib = null;
    protected static ArrayList< Activity > activities = new ArrayList< Activity >();

    static {
        Locale.setDefault(new Locale("hello"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set English Locale
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch( item.getItemId() ) {
            case R.id.menu_newlyAdded: { Intent intent = new Intent( this, ShowNewlyAddedActivity.class );
                startActivity(intent);
                break;
            }
            case R.id.menu_quit: {
                for( int i = 0; i < activities.size(); i++ ) {
                    activities.get( i ).finish();
                }
            }
            case R.id.menu_action_settings: break;
            case R.id.menu_saveLibraries: saveWords();
                break;
            default: break;
        }
        return true;
    }

    protected void loadWords() {
        loadWordsFromTxtFile( "basicwords" );
        loadWordsFromTxtFile( "ignoredwords" );
        loadWordsFromXMLFile( "xmlwords.xml" );
    }

    private void loadWordsFromTxtFile( String filename ) {
        File directory = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "wordmaster" );
        if( !directory.exists() ) {
            return;
        }

        File wordFile = new File( directory.getAbsolutePath() + filename );
        if( !wordFile.exists() ) {
            return;
        }

        try {
            BufferedReader br = new BufferedReader( new FileReader( wordFile ) );
            while( br.ready() ) {
                String wordContent = br.readLine();
                Word basicWord = new Word( wordContent, WordState.Basic );
                wordLib.addWord( wordContent, basicWord );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWordsFromXMLFile( String filename ) {
        File directory = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "wordmaster" );
        if( !directory.exists() ) {
            return;
        }

        File wordFile = new File( directory.getAbsolutePath() + filename );
        if( !wordFile.exists() ) {
            return;
        }

        try {
            // set up XML parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document xmlDoc = db.parse( new FileInputStream( wordFile ) );

            // get the word node list
            NodeList list = xmlDoc.getElementsByTagName( "word" );
            for( int i = 0; i < list.getLength(); i++ ) {
                Node e = list.item( i );
                NodeList childList = e.getChildNodes();

                Word word = new Word();

                // initialize the words
                for( int j = 0; j < childList.getLength(); j++ ) {
                    String nodeName = childList.item( j ).getNodeName();
                    if( nodeName.equals( "english" ) ) {
                        word.setContent( childList.item( j ).getTextContent().toLowerCase() );
                    }
                    else if( nodeName.equals( "sentences" ) ) {
                        NodeList sNodeList = childList.item( j ).getChildNodes();	// children of <sentences>
                        for( int k = 0; k < sNodeList.getLength(); k++ ) {
                            if( sNodeList.item( k ).getNodeName().equals( "sentence" ) ) {
                                String s = sNodeList.item( k ).getTextContent();
                                word.addSentence( s );
                            }
                        }
                    }
                    else if( nodeName.equals( "in_time" ) ) {
                        word.setIn_time( DateFormat.getInstance().parse( childList.item( j ).getTextContent() ) );
                    }
                    else if( nodeName.equals( "#text" ) ) {
                        continue;
                    }
                    else {
                        throw new RuntimeException( "XML parsing error: unknown element " + nodeName );
                    }
                }

                // what did android suggest?
                wordLib.addWord( word.getContent(), word );
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
    }

    private void saveWordsIntoTxtFile( Collection< String > wordCollection, String filename ) {
        File directory = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "wordmaster" );
        if( !directory.exists() ) {
            directory.mkdir();
        }

        File wordFile = new File( directory.getAbsolutePath() + filename );
        if( wordFile.exists() ) {
            wordFile.delete();
        }

        try {
            PrintWriter writer = new PrintWriter( new FileOutputStream( wordFile ) );
            for( String wordContent : wordCollection ) {
                writer.write( wordContent + "\n" );
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveWordsIntoXMLFile( Collection< Word > wordCollection, String filename ) {
        try {
            // create new word list file
            File directory = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "wordmaster" );
            if( !directory.exists() ) {
                directory.mkdir();
            }
            File file = new File( directory.getAbsolutePath() + filename );
            if( file.exists() ) {
                file.delete();
            }

            // if there is no words
            if( wordCollection.size() == 0 ) {
                return;
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
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

            for( Word wordInstance : wordCollection ) {
                String wordContent = wordInstance.getContent();

                // set the elements
                Element word = xmlDoc.createElement( "word" );
                Element english = xmlDoc.createElement( "english" );
                english.setTextContent( wordContent );
                word.appendChild( english );
                // set the sentences
                Element sentences = xmlDoc.createElement( "sentences" );
                word.appendChild( sentences );
                for( int j = 0; j < wordInstance.getSentences().size(); j++) {
                    Element sentence = xmlDoc.createElement( "sentence" );
                    sentence.setTextContent( wordInstance.getSentences().get( j ) );
                    sentences.appendChild( sentence );
                }
                // set in_time
                Element in_time = xmlDoc.createElement( "in_time" );
                in_time.setTextContent( DateFormat.getInstance().format( wordInstance.getIn_time() ) );
                word.appendChild( in_time );

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

    private void saveWords() {
        saveWordsIntoTxtFile( wordLib.getBasicWords(), "basicwords.txt" );
        saveWordsIntoTxtFile( wordLib.getIgnoredWords(), "ignoredwords.txt" );
        Collection< Word > wordCollection = wordLib.getUnfamiliarWords();
        wordCollection.addAll( wordLib.getUnrecognizedWords() );
        saveWordsIntoXMLFile( wordCollection, "xmlwords.xml" );
    }
}
