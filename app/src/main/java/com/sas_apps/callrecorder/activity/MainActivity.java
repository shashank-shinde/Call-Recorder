package com.sas_apps.callrecorder.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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

import com.sas_apps.callrecorder.R;
import com.sas_apps.callrecorder.adaptor.RecordingsAdaptor;
import com.sas_apps.callrecorder.receiver.MyReceiver;
import com.sas_apps.callrecorder.record.CallRecord;
import com.sas_apps.callrecorder.service.CallRecordService;
import static com.sas_apps.callrecorder.utils.Utils.*;


public class MainActivity extends AppCompatActivity implements Switch.OnCheckedChangeListener {

    private static final String TAG = "MainActivity";
    private Switch switchRec;
    private CallRecord callRecord;
    private RecyclerView recyclerView;
    private TextView textNoRecordings;
    private RecordingsAdaptor adaptor;
    File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        textNoRecordings = findViewById(R.id.text_noRecordings);
        switchRec = findViewById(R.id.switchRec);
        switchRec.setOnCheckedChangeListener(this);
        callRecord = new CallRecord.Builder(this)
                .setRecordFileName(FILE_NAME)
                .setAudioSource(MediaRecorder.AudioSource.VOICE_CALL)
                .setShowSeed(true)
                .build();
        callRecord.changeReceiver(new MyReceiver(callRecord));


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        String path = Environment.getExternalStorageDirectory().toString() + "/" + FOLDER_NAME;
//        Log.d("Files", "Path: " + path);
        try {
            File directory = new File(path);
            files = directory.listFiles();
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
        }
        adaptor = new RecordingsAdaptor(files, this);
        recyclerView.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();
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
        callRecord.changeRecordDirName(FOLDER_NAME);
    }

    public void stopCallRecord() {
        Toast.makeText(this, "Call recorder stopped", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "StopCallRecord");
        callRecord.stopCallReceiver();
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
