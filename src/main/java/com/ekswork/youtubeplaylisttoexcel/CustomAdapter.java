package com.ekswork.youtubeplaylisttoexcel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by user on 25.08.2016.
 */
public class CustomAdapter  extends ArrayAdapter<VideoItem> {

    public CustomAdapter(Context context, List<VideoItem> _videoList){ super (context, 0, _videoList);}

    public VideoItem yVideo;

    public View getView (int position, View convertView, final ViewGroup parent) {
        final ViewHolderItem viewHolder;
        yVideo = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolderItem();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rowvideoitem, parent, false);
            viewHolder.thumbs = (ImageView) convertView.findViewById(R.id.imgThumbs);
            viewHolder.title = (TextView) convertView.findViewById(R.id.txtTitle);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.txtDesc);
            viewHolder.title.setMaxLines(2);
            viewHolder.desc.setMaxLines(2);


            Picasso.with(getContext())
                    .load(yVideo.getThumbnailURL())
                    .error(R.drawable.nothumb)
                    .into(viewHolder.thumbs);
            viewHolder.title.setText(yVideo.getTitle());
            viewHolder.desc.setText(yVideo.getDescription());

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        return convertView;
    }

    final  static class ViewHolderItem{
        ImageView thumbs;
        TextView title;
        TextView desc;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
