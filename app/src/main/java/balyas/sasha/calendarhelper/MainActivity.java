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
    public static final long calendarID = 1; //Because 99% users have as least 1 calendar

    EditText mETChangeTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mETChangeTitle = (EditText) findViewById(R.id.etChangeTitle);
    }

    public void create(View view) {

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
        values.put(CalendarContract.Events.CALENDAR_ID, calendarID);
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
