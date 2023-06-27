package com.example.gallery;

import android.net.Uri;

public class ImageData {
    private Uri imageUri;
    private int id;
    private int dateAdded;
    private String bucketName;

    public ImageData(Uri imageUri, int id, int dateAdded, String bucketName) {
        this.imageUri = imageUri;
        this.id = id;
        this.dateAdded = dateAdded;
        this.bucketName = bucketName;
    }
}
