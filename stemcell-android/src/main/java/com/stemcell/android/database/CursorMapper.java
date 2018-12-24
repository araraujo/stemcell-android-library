package com.stemcell.android.database;

import android.database.Cursor;

/**
 *
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
