package mike.galitsky.myshop;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ItemsContentProvider extends ContentProvider {

    private DataBaseHelper helper;
    private SQLiteDatabase db;

    static final String AUTHORITY = "mike.galitsky.myshop";
    static final String ITEMS_PATH = "items";

    public static final Uri ITEMS_URI = Uri.parse("content://" + AUTHORITY + "/" + ITEMS_PATH);

    static final String ITEMS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + ITEMS_PATH;
    static final String ITEMS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + ITEMS_PATH;


    static final int URI_ITEMS = 1;
    static final int URI_ITEM_ID = 2;

    private static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ITEMS_PATH, URI_ITEMS);
        uriMatcher.addURI(AUTHORITY, ITEMS_PATH + "/#", URI_ITEM_ID);
    }



    @Override
    public boolean onCreate() {
        helper = new DataBaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        db = helper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Items.TABLE_NAME);

        switch (uriMatcher.match(uri)){
            case URI_ITEM_ID:
                queryBuilder.appendWhere(Items.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
        }
        Cursor cursor = queryBuilder.query(db, strings, s, strings1, null, null, s1);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)) {
            case URI_ITEMS:
                return ITEMS_CONTENT_TYPE;
            case URI_ITEM_ID:
                return ITEMS_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        db =  helper.getWritableDatabase();
        long id = 0;

        switch (uriMatcher.match(uri)){
            case URI_ITEMS:
                db.insert(Items.TABLE_NAME, null, contentValues);
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(ITEMS_URI, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        db = helper.getWritableDatabase();
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case URI_ITEMS:
                count = db.delete(Items.TABLE_NAME, s, strings);
                break;
            case URI_ITEM_ID:
                String id = uri.getLastPathSegment();
                if (s == null || s.isEmpty()) {
                    count = db.delete(Items.TABLE_NAME, Items.COLUMN_ID + "=" + id, null);
                } else {
                    count = db.delete(Items.TABLE_NAME, Items.COLUMN_ID + "=" + id + " and " + s, strings);
                }
                break;
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        db = helper.getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case URI_ITEMS:
                count = db.update(Items.TABLE_NAME, contentValues, s, strings);
                break;
            case URI_ITEM_ID:
                String id = uri.getLastPathSegment();
                if (s == null || s.isEmpty()) {
                    count = db.update(Items.TABLE_NAME, contentValues, Items.COLUMN_ID + "=" + id, null);
                } else {
                    count = db.update(Items.TABLE_NAME, contentValues, Items.COLUMN_ID + "=" + id + " and " + s, strings);
                }
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }


}
