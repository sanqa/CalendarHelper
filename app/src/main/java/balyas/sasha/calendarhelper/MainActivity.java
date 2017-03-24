package balyas.sasha.calendarhelper;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    public static final long eventID = 10001;

   /* public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;*/


    EditText mETChangeDescription;

//    String calendarID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mETChangeDescription = (EditText) findViewById(R.id.etChangeDescription);
    }

    public void create(View view) {

       /* Cursor curInfo = null;
        ContentResolver crInfo = getContentResolver();
        Uri uriInfo = CalendarContract.Calendars.CONTENT_URI;

// Submit the query and get a Cursor object back.
        curInfo = crInfo.query(uriInfo, EVENT_PROJECTION, null, null, null);

        if (null != curInfo) {
            curInfo.moveToFirst();

            do {
                long calID = 0;
                String displayName = null;
                String accountName = null;
                String ownerName = null;

                // Get the field values
                calID = curInfo.getLong(PROJECTION_ID_INDEX);
                displayName = curInfo.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = curInfo.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = curInfo.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                // Do something with the values...
                Log.d("Calendar Info", "Calendar ID = " + calID + " , Account Name = " + accountName + " , Owner Name = " + ownerName);

                calendarID = String.valueOf(calID);

            } while (curInfo.moveToNext());
        }*/

        long startMillis;
        long endMillis;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 2, 25, 11, 0);// set(int year, int month, int day, int hourOfDay, int minute)
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 2, 25, 14, 0);
        endMillis = endTime.getTimeInMillis();

        TimeZone tz = TimeZone.getDefault();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();


        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events._ID, eventID);
        values.put(CalendarContract.Events.TITLE, "Твоё ЗНО по математике");
        values.put(CalendarContract.Events.DESCRIPTION, "Сходи и сдай его на отлично");
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        cr.insert(CalendarContract.Calendars.CONTENT_URI, valuesCal);
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        long eventID = Long.parseLong(uri.getLastPathSegment());


        Toast.makeText(this, "Event was created", Toast.LENGTH_SHORT).show();
    }

    public void intentCreate(View view) {

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 2, 25, 10, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 2, 25, 13, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Yoga")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivity(intent);
    }

    public void change(View view) {
        ContentValues values = new ContentValues();
        Uri updateUri;

        values.put(CalendarContract.Events.DESCRIPTION, mETChangeDescription.getText().toString());
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = getContentResolver().update(updateUri, values, null, null);

        mETChangeDescription.setText("");

        Log.i("CHANGE EVENT", "Rows updated: " + rows);
        Toast.makeText(this, "Event was changed", Toast.LENGTH_SHORT).show();
    }

    public void delete(View view) {
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = getContentResolver().delete(deleteUri, null, null);

        Log.i("DELETE_EVENT", "Rows deleted: " + rows);
        Toast.makeText(this, "Event was deleted", Toast.LENGTH_SHORT).show();

    }
}
