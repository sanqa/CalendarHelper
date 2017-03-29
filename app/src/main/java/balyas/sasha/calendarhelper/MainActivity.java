package balyas.sasha.calendarhelper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    public static final long eventID = 10001;
    public static long calendarID = 1; //Because 99% users have as least 1 calendar

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    EditText mETChangeTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mETChangeTitle = (EditText) findViewById(R.id.etChangeTitle);
    }

    public void create(View view) {

        final ArrayAdapter<String> calendarEmails = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);

        Cursor curInfo = null;
        ContentResolver crInfo = getContentResolver();
        Uri uriInfo = CalendarContract.Calendars.CONTENT_URI;

        // Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CALENDAR)) {
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
//                alertBuilder.setCancelable(true);
//                alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
//                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                    public void onClick(DialogInterface dialog, int which) {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALENDAR}, 1002);
//                    }
//                });
//            } else {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALENDAR}, 1002);
//            }
        }
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
                Log.d("Calendar Info", "Calendar ID = " + calID + " , Display Name = " + displayName + " , Account Name = " + accountName + " , Owner Name = " + ownerName);
                if (null != accountName) {
                    calendarEmails.add(accountName);
                }
                calendarID = calID;

            } while (curInfo.moveToNext());


            if (curInfo.getCount() > 1) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                builderSingle.setTitle("Select One Calendar:-");


                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(calendarEmails, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        add(position + 1);
                    }
                });
                builderSingle.show();
            }
        }
    }

    private void add(int calendar) {
        long startMillis;
        long endMillis;

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 2, 26, 11, 0);// set(int year, int month, int day, int hourOfDay, int minute)
        startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 2, 26, 14, 0);
        endMillis = endTime.getTimeInMillis();

        TimeZone tz = TimeZone.getDefault();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();


        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events._ID, eventID);
        values.put(CalendarContract.Events.TITLE, "Твоё ЗНО по математике");
        values.put(CalendarContract.Events.DESCRIPTION, "Сходи и сдай его на отлично");
        values.put(CalendarContract.Events.CALENDAR_ID, calendar);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_CALENDAR)) {
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
//                alertBuilder.setCancelable(true);
//                alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
//                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                    public void onClick(DialogInterface dialog, int which) {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_CALENDAR}, 1001);
//                    }
//                });
//            } else {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_CALENDAR}, 1001);
//            }
        }

        cr.insert(CalendarContract.Events.CONTENT_URI, values);

        ContentResolver cr2 = getContentResolver();
        ContentValues values2 = new ContentValues();

        values2.put(CalendarContract.Reminders.MINUTES, 30);
        values2.put(CalendarContract.Reminders.EVENT_ID, eventID);
        values2.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        cr2.insert(CalendarContract.Reminders.CONTENT_URI, values2);


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

        values.put(CalendarContract.Events.TITLE, mETChangeTitle.getText().toString());
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = getContentResolver().update(updateUri, values, null, null);

        mETChangeTitle.setText("");

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
