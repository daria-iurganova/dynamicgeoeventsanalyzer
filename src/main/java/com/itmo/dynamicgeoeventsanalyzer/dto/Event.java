package com.itmo.dynamicgeoeventsanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {
    public Long takenAt;
    public Long pk;
    public String id;
    public Long deviceTimestamp;
    public Long mediaType;
    public String code;
    public String clientCacheKey;
    public Long filterType;
    public ImageVersions2 imageVersions2;
    public Long originalWidth;
    public Long originalHeight;
    public Location location;
    public Double lat;
    public Double lng;
    public User user;
    public String organicTrackingToken;
    public Long likeCount;
    public Boolean hasLiked;
    public Boolean hasMoreComments;
    public Long maxNumVisiblePreviewComments;
    public Long commentCount;
    public Caption caption;
    public Boolean captionIsEdited;
    public Boolean photoOfYou;
    public Long nextMaxId;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Candidate {
        public String url;
        public Long width;
        public Long height;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Caption {
        public String status;
        public Long userId;
        public Long createdAtUtc;
        public Long createdAt;
        public Long bitFlags;
        public User user;
        public String contentType;
        public String text;
        public Long mediaId;
        public Long pk;
        public Long type;
        public Boolean hasTranslation;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ImageVersions2 {
        public List<Candidate> candidates = null;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Location {
        public String externalSource;
        public String name;
        public Long facebookPlacesId;
        public String address;
        public Double lat;
        public Double lng;
        public Long pk;

    }

    public static class User {
        public String username;
        public Boolean hasAnonymouseProfilePicture;
        public String profilePicId;
        public String profilePicUrl;
        public String fullName;
        public Long pk;
        public Boolean isVerified;
        public Boolean isPrivate;
        public Boolean isFavorite;
        public Boolean isUnpublished;
    }
}