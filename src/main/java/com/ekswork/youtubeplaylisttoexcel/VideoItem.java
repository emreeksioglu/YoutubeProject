package com.ekswork.youtubeplaylisttoexcel;

/**
 * Created by user on 25.08.2016.
 */
public class VideoItem {
    private String title;
    private String description;
    private String thumbnailURL;
    private String id;
    private String videoUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;
    }

    public String getVideoUrl(){return AppHelper.YOUTUBE_VIDEO_URL_PREFIX  +  id;}

}
