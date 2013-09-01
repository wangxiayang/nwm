package me.horzwxy.wordservant;

/**
 * Created by horz on 9/1/13.
 */
public class WordTempStorage {

    private TimeOrderedWordSet unfamiliarSet;
    private TimeOrderedWordSet unrecognizedSet;

    public void addToUnfamiliarSet( Word word ) {
        unfamiliarSet.add( word );
    }

    public TimeOrderedWordSet getUnfamiliarSet() {
        return unfamiliarSet;
    }

    public void addToUnrecognizedSet( Word word ) {
        unrecognizedSet.add( word );
    }

    public TimeOrderedWordSet getUnrecognizedSet() {
        return unrecognizedSet;
    }
}
