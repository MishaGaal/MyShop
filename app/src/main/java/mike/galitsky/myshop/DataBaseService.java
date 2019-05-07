package mike.galitsky.myshop;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;


//insert, update, delete, getItems, getCategories, getCategoryitems, backup, incertbakupitems, incertcategories,

public class DataBaseService extends IntentService {

    public static final String ACTION_INSERT = "mike.galitsky.myshop.action.INSERT";
    public static final String ACTION_UPDATE = "mike.galitsky.myshop.action.UPDATE";
    public static final String ACTION_DELETE = "mike.galitsky.myshop.action.DELETE";
    public static final String ACTION_GET_ITEMS = "mike.galitsky.myshop.action.GET_ITEMS";
    public static final String ACTION_GET_CATEGORIES = "mike.galitsky.myshop.action.GET_CATEGORIES";
    public static final String ACTION_GET_CATEGORY_ITEMS = "mike.galitsky.myshop.action.GET_CATEGORY_ITEMS";
    public static final String ACTION_BACKUP = "mike.galitsky.myshop.action.BACKUP";
    public static final String ACTION_INSERT_BACKUP_ITEMS = "mike.galitsky.myshop.action.INSERT_BACKUP_ITEMS";
    public static final String ACTION_INSERT_CATEGORIES = "mike.galitsky.myshop.action.INSERT_CATEGORIES";


    public static final String EXTRA_PENDING_INTENT = "mike.galitsky.myshop.PENDING_INTENT";
    public static final int REQUEST_CODE_INSERT_ITEM = 5;
    public static final int REQUEST_CODE_UPDATE_ITEM = 6;
    public static final int REQUEST_CODE_DELETE_ITEM = 7;
    public static final int REQUEST_GET_ITEMS = 8;
    public static final int REQUEST_GET_CATEGORIES = 9;
    public static final int REQUEST_GET_CATEGORY_ITEMS = 10;
    public static final int REQUEST_BACKUP = 11;
    public static final int REQUEST_INSERT_BACKUP_ITEMS = 12;
    public static final int REQUEST_INSERT_CATEGORIES = 14;
    public static final String EXTRA_ID = "mike.galitsky.myshop.ID";
    public static final String EXTRA_UPDATED = "mike.galitsky.myshop.UPDATED";
    public static final String EXTRA_DELETED = "mike.galitsky.myshop.DELETED";
    public static final String EXTRA_ITEM = "mike.galitsky.myshop.ITEM";
    public static final String EXTRA_CATEGORY = "mike.galitsky.myshop.CATEGORY";
    public static final String EXTRA_ITEMS = "mike.galitsky.myshop.ITEMS";
    public static final String EXTRA_CATEGORIES = "mike.galitsky.myshop.CATEHORIES";
    public static final String EXTRA_FOLDER = "mike.galitsky.myshop.FOLDER";
    public static final String EXTRA_FILE = "mike.galitsky.myshop.FILE";




    public DataBaseService() {
        super("DataBaseService");
    }


    public static void insertItem (Context context, Items item) {
        Intent intent = new Intent(context, DataBaseService.class);

        PendingIntent pendingIntent =((AppCompatActivity)context).createPendingResult(REQUEST_CODE_INSERT_ITEM, intent, 0);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_ITEM, item);
        context.startService(intent);
    }


    public static void updateItem (Context context, Items item) {
        Intent intent = new Intent(context, DataBaseService.class);
        PendingIntent pendingIntent =((AppCompatActivity)context).createPendingResult(REQUEST_CODE_UPDATE_ITEM, intent, 0);
        intent.setAction(ACTION_UPDATE);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_ITEM, item);
        context.startService(intent);
    }

    public static void deleteItem (Context context, Items item) {
        Intent intent = new Intent(context, DataBaseService.class);
        PendingIntent pendingIntent =((AppCompatActivity)context).createPendingResult(REQUEST_CODE_DELETE_ITEM, intent, 0);
        intent.setAction(ACTION_DELETE);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_ITEM, item);
        context.startService(intent);
    }

    public static void getAllItems (Context context) {

        Intent intent = new Intent(context, DataBaseService.class);
        PendingIntent pendingIntent =((AppCompatActivity)context).createPendingResult(REQUEST_GET_ITEMS, intent, 0);
        intent.setAction(ACTION_GET_ITEMS);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        context.startService(intent);
    }

    public static void getAllCategories (Context context) {

        Intent intent = new Intent(context, DataBaseService.class);
        PendingIntent pendingIntent =((AppCompatActivity)context).createPendingResult(REQUEST_GET_CATEGORIES, intent, 0);
        intent.setAction(ACTION_GET_CATEGORIES);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        context.startService(intent);
    }
    public static void getCategoryItems (Context context, Categories category) {

        Intent intent = new Intent(context, DataBaseService.class);
        PendingIntent pendingIntent =((AppCompatActivity)context).createPendingResult(REQUEST_GET_CATEGORY_ITEMS, intent, 0);
        intent.setAction(ACTION_GET_CATEGORY_ITEMS);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_CATEGORY, category);
        context.startService(intent);
    }

    public static void BackUp (Context context, File folder, String FileName) {

        Intent intent = new Intent(context, DataBaseService.class);
        PendingIntent pendingIntent =((AppCompatActivity)context).createPendingResult(REQUEST_BACKUP, intent, 0);
        intent.setAction(ACTION_BACKUP);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_FOLDER, folder);
        intent.putExtra(EXTRA_FILE, FileName);
        context.startService(intent);
    }
    public static void Upload (Context context, File folder, String FileName) {

        Intent intent = new Intent(context, DataBaseService.class);
        PendingIntent pendingIntent =((AppCompatActivity)context).createPendingResult(REQUEST_INSERT_BACKUP_ITEMS, intent, 0);
        intent.setAction(ACTION_INSERT_BACKUP_ITEMS);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_FOLDER, folder);
        intent.putExtra(EXTRA_FILE, FileName);
        context.startService(intent);
    }
    public static void insertCategory (Context context, Categories category) {
        Intent intent = new Intent(context, DataBaseService.class);

        PendingIntent pendingIntent =((AppCompatActivity)context).createPendingResult(REQUEST_INSERT_CATEGORIES, intent, 0);
        intent.setAction(ACTION_INSERT_CATEGORIES);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_CATEGORY, category);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            PendingIntent pendingIntent = intent.getParcelableExtra(EXTRA_PENDING_INTENT);
            Intent result = new Intent();
            DataBaseHelper helper = new DataBaseHelper(this);
            final String action = intent.getAction();
            if (ACTION_INSERT.equals(action)) {

                Items item = intent.getParcelableExtra(EXTRA_ITEM);
                long id = helper.insert(item);
                result.putExtra(EXTRA_ID, id);

            } else if (ACTION_UPDATE.equals(action)) {

                Items item = intent.getParcelableExtra(EXTRA_ITEM);
                boolean updated = helper.update(item);
                result.putExtra(EXTRA_UPDATED, updated);

            }else if (ACTION_DELETE.equals(action)) {
                Items item = intent.getParcelableExtra(EXTRA_ITEM);
                boolean deleted = helper.delete(item);
                result.putExtra(EXTRA_DELETED, deleted);

            }else if (ACTION_GET_ITEMS.equals(action)) {
                ArrayList<Items> items = helper.getItems();
                result.putExtra(EXTRA_ITEMS, items);

            }else if (ACTION_GET_CATEGORIES.equals(action)) {
                ArrayList<Categories> categories = helper.getCategories();
                result.putExtra(EXTRA_CATEGORIES, categories);

            }else if (ACTION_GET_CATEGORY_ITEMS.equals(action)) {
                Categories category = intent.getParcelableExtra(EXTRA_CATEGORY);
                ArrayList<Items> items = helper.getCategoryItems(category);
                result.putExtra(EXTRA_ITEMS, items);

            }else if (ACTION_BACKUP.equals(action)) {
                File folder = intent.getParcelableExtra(EXTRA_FOLDER);
                String FileName = intent.getStringExtra(EXTRA_FILE);
                helper.backUp(folder, FileName);
            }else if (ACTION_INSERT_BACKUP_ITEMS.equals(action)) {
                File folder = intent.getParcelableExtra(EXTRA_FOLDER);
                String FileName = intent.getStringExtra(EXTRA_FILE);
                helper.InsertBackUpItems(folder, FileName);
            }else if (ACTION_INSERT_CATEGORIES.equals(action)) {
            Categories category = intent.getParcelableExtra(EXTRA_CATEGORY);
            long id = helper.insertCategories(category);
            result.putExtra(EXTRA_ID, id);
              }

            try {
                pendingIntent.send(this, Activity.RESULT_OK, result);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

        }
    }

}
