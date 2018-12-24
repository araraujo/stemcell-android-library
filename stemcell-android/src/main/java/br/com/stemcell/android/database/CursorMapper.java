package br.com.stemcell.android.database;

import android.database.Cursor;

/**
 * Created by melti on 12/07/15.
 */
public abstract class CursorMapper {

    private Cursor mCursor;

    protected abstract void bindColumns(Cursor cursor);

    protected abstract Object bind(Cursor cursor);

    public Object convert(Cursor cursor) {
        if (cursor != mCursor) {
            mCursor = cursor;
            bindColumns(mCursor);
        }
        return bind(mCursor);
    }

}
