package me.horzwxy.wordservant;

import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;

/**
 * Word set sorted by in-time.
 */
public class TimeOrderedWordSet extends TreeSet<Word> {

    TimeOrderedWordSet() {
        super(new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                if( word1 == null && word2 == null ) {
                    return 0;
                }
                else if( word1 == null ) {
                    return word2.getIn_time().compareTo( new Date() );
                }
                else {
                    int result = word1.getIn_time().compareTo( word2.getIn_time() );
                    if( result == 0 ) {
                        result = word1.compareTo( word2 );
                    }
                    return result;
                }
            }
        });
    }
}
