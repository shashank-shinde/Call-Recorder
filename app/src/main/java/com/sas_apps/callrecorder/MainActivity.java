package com.sas_apps.callrecorder;

import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aykuttasil.callrecord.CallRecord;
import com.sas_apps.callrecorder.receiver.MyReceiver;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    CallRecord callRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callRecord = new CallRecord.Builder(this)
                .setRecordFileName("CallRecorderTestFile")
                .setRecordDirName("CallRecorderTest")
                .setAudioSource(MediaRecorder.AudioSource.VOICE_CALL)
                .setShowSeed(true)
                .build();
        callRecord.changeReceiver(new MyReceiver(callRecord));

        String path = Environment.getExternalStorageDirectory().toString() + "/CALL_REC";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (File file : files) {
            Log.d("Files", "FileName:" + file.getName());
        }
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
