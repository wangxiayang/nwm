package me.horzwxy.wordservant;

/**
 * Created by horz on 8/28/13.
 */
public class SentenceAnalyzer {

    private WordLibrary wordLib;
    private WordRecognizer recognizer;
    private WordTempStorage tempStorage;

    public SentenceAnalyzer( WordLibrary wordLib,
                             WordRecognizer recognizer,
                             WordTempStorage tempStorage ) {
        this.wordLib = wordLib;
        this.recognizer = recognizer;
        this.tempStorage = tempStorage;
    }

    public void analyzeSentence( String sentence ) {
        String[] rawWords = sentence.split( " " );	// split by blanks
        for( int i = 0; i < rawWords.length; i++ ) {
            String rawWord = rawWords[ i ];

            String pw = getPurifiedWord(rawWord);
            if( pw == null )
            {
                // if it's full of symbols
                continue;
            }

            WordState state = recognizer.analyze( pw );

            if( state == WordState.Unfamiliar ||
                    state == WordState.Unrecognized ) {
                Word wordInstance = wordLib.getWord( pw );
                wordInstance.addSentence( sentence );
                if( state == WordState.Unfamiliar ) {
                    tempStorage.addToUnfamiliarSet( wordInstance );
                }
                else {
                    tempStorage.addToUnrecognizedSet( wordInstance );
                }
            }
            else if( state == WordState.Untracked ) {
                Word wordInstance = wordLib.createAndAdd( pw );
                wordInstance.addSentence( sentence );
                tempStorage.addToUnrecognizedSet( wordInstance );
            }
        }
    }

    private String getPurifiedWord( String rawWord ) {
        int prefixLength = 0;
        for( int j = 0; j < rawWord.length(); j++ ) {
            // skip the heading symbols
            if( rawWord.charAt( j ) < 'A' || rawWord.charAt( j ) > 'z' ) {
                prefixLength++;
            }
            else break;
        }
        int suffixLength = 0;
        for( int j = rawWord.length() - 1; j >= 0; j-- ) {
            // skip the ending symbols
            if( rawWord.charAt( j ) < 'A' || rawWord.charAt( j ) > 'z' ) {
                suffixLength++;
            }
            else break;
        }
        // If the rawWord is full of symbols, skip to the next
        if( prefixLength == rawWord.length() ) {
            return null;
        }
        // get the purified word
        return rawWord.substring( prefixLength, rawWord.length() - suffixLength );
    }
}
