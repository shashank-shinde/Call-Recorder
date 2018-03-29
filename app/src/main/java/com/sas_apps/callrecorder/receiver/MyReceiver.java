package com.sas_apps.callrecorder.receiver;
/*
 * Created by Shashank Shinde.
 */


import android.content.Context;
import android.widget.Toast;

import com.sas_apps.callrecorder.record.CallRecord;

import java.io.File;

public class MyReceiver extends CallRecordReceiver {

    public MyReceiver(CallRecord callRecord) {
        super(callRecord);
    }


    @Override
    protected void onRecordingStarted(Context context, CallRecord callRecord, File audioFile) {
        super.onRecordingStarted(context, callRecord, audioFile);
        Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRecordingFinished(Context context, CallRecord callRecord, File audioFile) {
        super.onRecordingFinished(context, callRecord, audioFile);
        Toast.makeText(context, "Recording finished", Toast.LENGTH_SHORT).show();

    }


}
