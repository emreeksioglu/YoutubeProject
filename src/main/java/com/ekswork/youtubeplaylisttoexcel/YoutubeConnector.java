package com.ekswork.youtubeplaylisttoexcel;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class YoutubeConnector {

    private YouTube youtube;
    private YouTube.Search.List query;
    public static final String KEY
            = "AIzaSyAjT-ysSojv3RPWlexrHvD9-GF_HQqZ7zw";
    private Long MAX_RESULT= 50L;

    public YoutubeConnector(Context context) {

        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {}
        }).setApplicationName(context.getString(R.string.app_name)).build();

    }


    public List<VideoItem> GetPlayListItem(String playlistId) {
        List<VideoItem> items = new ArrayList<VideoItem>();

        try {
            YouTube.PlaylistItems.List q = youtube.playlistItems().list("snippet");
            q.setKey(KEY);
            q.setPlaylistId(playlistId);
            q.setMaxResults(MAX_RESULT);

            PlaylistItemListResponse response = q.execute();
            List<PlaylistItem> results = response.getItems();

            int pageCount = (int)Math.ceil(response.getPageInfo().getTotalResults() / 50.0);
            int pageIndex = 0;
            String thumbUrl="";
            do {
                for (PlaylistItem result : results) {
                    VideoItem item = new VideoItem();
                    item.setTitle(result.getSnippet().getTitle());
                    item.setDescription(result.getSnippet().getDescription());

                    if (result.getSnippet().getThumbnails() != null)
                        thumbUrl = result.getSnippet().getThumbnails().getDefault().getUrl();

                    item.setThumbnailURL(thumbUrl);
                    item.setId(result.getSnippet().getResourceId().getVideoId());

                    items.add(item);
                }

                q.setPageToken(response.getNextPageToken());
                response = q.execute();
                results = response.getItems();

               pageIndex++;

            }while (pageIndex<pageCount);

            return items;

        } catch (IOException e) {
            e.printStackTrace();
            Log.w("wsd", "Listeyi alamıyorum");
        }

        return items;
    }

}
