package com.sas_apps.callrecorder;

import android.media.MediaRecorder;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.io.File;
import com.sas_apps.callrecorder.adaptor.ViewPagerAdaptor;
import com.sas_apps.callrecorder.fragments.RecordFragment;
import com.sas_apps.callrecorder.fragments.RecordingsFragment;
import com.sas_apps.callrecorder.receiver.MyReceiver;
import com.sas_apps.callrecorder.record.CallRecord;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdaptor adaptor;

    CallRecord callRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        adaptor = new ViewPagerAdaptor(getSupportFragmentManager());
        adaptor.addFragment(new RecordFragment(), "Record");
        adaptor.addFragment(new RecordingsFragment(), "Recordings");
        viewPager.setAdapter(adaptor);

        tabLayout.setupWithViewPager(viewPager);

        callRecord = new CallRecord.Builder(this)
                .setRecordFileName("CallRecorderTestFile")
                .setRecordDirName("CallRecorderTest")
                .setAudioSource(MediaRecorder.AudioSource.VOICE_CALL)
                .setShowSeed(true)
                .build();
        callRecord.changeReceiver(new MyReceiver(callRecord));

     /*   String path = Environment.getExternalStorageDirectory().toString() + "/CALL_REC";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        try {
            for (File file : files) {
                Log.d("Files", "FileName:" + file.getName());
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }*/
    }

    public void StartCallRecordClick(View view) {
        Log.i(TAG, "StartCallRecordClick");
        callRecord.startCallReceiver();
        callRecord.enableSaveFile();
        callRecord.changeRecordDirName("CALL_REC");
    }

    public void StopCallRecordClick(View view) {
        Log.i(TAG, "StopCallRecordClick");
        callRecord.stopCallReceiver();
    }
}
