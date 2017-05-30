package com.jskhaleel.myflickgallery.activities.home;

import java.io.Serializable;
import java.util.ArrayList;

public class FlickrGalleryParser implements Serializable {

    public FPhotos photos;
    public class FPhotos {
        public ArrayList<ListPhoto> photo;
        public class ListPhoto {
            public String id, secret, server, title;
            public int farm;
        }
    }
}
