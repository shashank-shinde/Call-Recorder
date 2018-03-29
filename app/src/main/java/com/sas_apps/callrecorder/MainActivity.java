package com.sas_apps.callrecorder;

import android.app.ActivityManager;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import com.sas_apps.callrecorder.receiver.MyReceiver;
import com.sas_apps.callrecorder.record.CallRecord;
import com.sas_apps.callrecorder.service.CallRecordService;


public class MainActivity extends AppCompatActivity implements Switch.OnCheckedChangeListener {

    private static final String TAG = "MainActivity";
    private Switch switchRec;
    private CallRecord callRecord;
    private RecyclerView recyclerView;
    private TextView textNoRecordings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        textNoRecordings = findViewById(R.id.text_noRecordings);
        switchRec = findViewById(R.id.switchRec);
        switchRec.setOnCheckedChangeListener(this);
        callRecord = new CallRecord.Builder(this)
                .setRecordFileName("REC")
                .setAudioSource(MediaRecorder.AudioSource.VOICE_CALL)
                .setShowSeed(true)
                .build();
        callRecord.changeReceiver(new MyReceiver(callRecord));

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        String path = Environment.getExternalStorageDirectory().toString() + "/CALL_REC";
//        Log.d("Files", "Path: " + path);
/*        try {
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Size: " + files.length);

            for (File file : files) {
                Log.d("Files", "FileName:" + file.getName());
                Log.d("LastModified", getDate(file.lastModified()));
                mmr.setDataSource(this, Uri.parse(file.getAbsolutePath()));
                String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                Log.d("Duration", durationStr);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }*/

        Log.d(TAG, "onCreate: isMyServiceRunning " + isMyServiceRunning(CallRecordService.class));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            startCallRecord();
        } else {
            stopCallRecord();
        }
    }

    public void startCallRecord() {
        Toast.makeText(this, "Call recorder started", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "StartCallRecord");
        callRecord.startCallReceiver();
        callRecord.enableSaveFile();
        callRecord.changeRecordDirName("CALL_REC");
    }

    public void stopCallRecord() {
        Toast.makeText(this, "Call recorder stopped", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "StopCallRecord");
        callRecord.stopCallReceiver();
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yyyy hh:mm aaa", cal).toString();
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
