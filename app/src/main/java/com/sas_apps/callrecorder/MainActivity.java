package com.sas_apps.callrecorder;

import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aykuttasil.callrecord.CallRecord;

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

        String path = Environment.getExternalStorageDirectory().toString()+"/CALL_REC";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }
    }

    public void StartCallRecordClick(View view) {
        Log.i("CallRecord", "StartCallRecordClick");
        callRecord.startCallReceiver();
        callRecord.enableSaveFile();
        callRecord.changeRecordDirName("CALL_REC");
    }

    public void StopCallRecordClick(View view) {
        Log.i("CallRecord", "StopCallRecordClick");
        callRecord.stopCallReceiver();

        //callRecord.disableSaveFile();
        //callRecord.changeRecordFileName("NewFileName");
    }
}
