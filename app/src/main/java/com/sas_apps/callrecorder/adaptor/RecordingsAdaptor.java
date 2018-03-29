package com.sas_apps.callrecorder.adaptor;
/*
 * Created by Shashank Shinde.
 */

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sas_apps.callrecorder.R;
import com.sas_apps.callrecorder.utils.Utils;

import java.io.File;

public class RecordingsAdaptor extends RecyclerView.Adapter<RecordingsAdaptor.RecordingHolder> {

    private File[] files;
    private Context context;
    MediaMetadataRetriever mmr;

    public RecordingsAdaptor(File[] files, Context context) {
        this.files = files;
        this.context = context;
        mmr = new MediaMetadataRetriever();
    }

    @Override
    public RecordingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new RecordingHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordingHolder holder, final int position) {
//        TODO
        String fileName = files[position].getName();
        holder.textTime.setText(Utils.getDate(files[position].lastModified()));
        holder.textName.setText(fileName);
        mmr.setDataSource(context,Uri.parse(files[position].getAbsolutePath()));
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        String duration=Utils.getDuration(durationStr);
        holder.textDuration.setText(duration);
        if (fileName.charAt(4) == 'i') {
            holder.imageCallType.setImageResource(R.drawable.ic_call_received);
        } else if (fileName.charAt(4) == 'o') {
            holder.imageCallType.setImageResource(R.drawable.ic_call_made);
        }


        holder.imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri fileUri = FileProvider.getUriForFile(
                        context,
                        context.getApplicationContext()
                                .getPackageName() + ".provider", files[position]);
                intent.setDataAndType(fileUri, "audio/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            }
        });


        holder.imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                intentShareFile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri fileUri = FileProvider.getUriForFile(
                        context,
                        context.getApplicationContext()
                                .getPackageName() + ".provider", files[position]);
                intentShareFile.setType("application/amr");
                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(files[position].getAbsolutePath()));
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                        "Sharing File...");
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                context.startActivity(intentShareFile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    class RecordingHolder extends RecyclerView.ViewHolder {

        TextView textName, textTime, textDuration;
        ImageView imagePlay, imageShare, imageCallType;

        RecordingHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textDuration = itemView.findViewById(R.id.text_duration);
            textTime = itemView.findViewById(R.id.text_time);
            imageShare = itemView.findViewById(R.id.image_send);
            imagePlay = itemView.findViewById(R.id.image_play);
            imageCallType = itemView.findViewById(R.id.image_callType);
        }
    }
}
