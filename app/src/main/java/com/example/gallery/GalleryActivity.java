package com.example.gallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private final ImagesAdapter imagesAdapter = new ImagesAdapter();

    ActivityResultLauncher<String> permissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            granted -> {
                if (granted) loadAndDisplayImages();
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((RecyclerView) findViewById(R.id.imagesRecyclerView)).setAdapter(imagesAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermissions(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            checkPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void checkPermissions(String permission) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
            permissionResultLauncher.launch(permission);
        } else {
            loadAndDisplayImages();
        }
    }

    private void loadAndDisplayImages() {
        ArrayList<Uri> images = fetchImages();
        imagesAdapter.submitList(images);
    }

    /*
    getContentResolver().query(
    uri,
    columns,
    selection,
    selectionArgs,
    orderBy
    )
    uri - таблиця з якої будуть братись дані, наприклад:
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI - картинки
    MediaStore.Videos.Media.EXTERNAL_CONTENT_URI - відео
    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI - аудіо
    Contacts.Phones.CONTENT_URI - контакти

    columns - які колонки з таблиці будуть діставатися з бази даних, наприклад:
    MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_CREATED, MediaStore.Images.Media._ID,
    Contacts.Phones.NAME

    selection - фільтр, або умова як в SQL. Можна написати якусь умову наприклад - "_id = ? OR name = ?",
    замість знаків питання будуть пісдтавлятися значення з *selectionArgs* масиву, наприклад new String[]{"michael", "1"}.
    В такому випадку дістануться з бази даних тільки ті значення в яких або айді дорівнює 1 або імя - michael.

    orderBy - порядок по якому діставати дані, схоже як в SQL. Можна написати по якому параметру та як саме сортувати,
    з низу вверх чи з верху вниз. Наприклад  - MediaStore.Images.Media.DATE_ADDED + " DESC".
    В даному випадку дані будуть сортуватися по даті додавання, DESC означає що будуть іти по спаданню, спочатку будуть іти
    найновіші картинки. Якщо поставити параметр ASC то спочатку будуть іти найстаріші картинки.
    ASC - по зростанню - 1,2,3,4...
    DESC - по спаданню - 10, 9, 8, ...

     */
    public ArrayList<Uri> fetchImages() {
        ArrayList<Uri> imagesUri = new ArrayList<>();
        ArrayList<ImageData> imagesData = new ArrayList<>();
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
        final String selection = "_id = ? OR bucket_display_name = ?";
        //Stores all the images from the gallery in Cursor

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                orderBy);

        //Total number of images
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);

            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int bucketNameColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dateAddedColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);

            int id = cursor.getInt(idColumnIndex);
            int dateAdded = cursor.getInt(dateAddedColumnIndex);
            String bucketName = cursor.getString(bucketNameColumnIndex);

            //Store the path of the image
            String filePath = cursor.getString(dataColumnIndex);
            File file = new File(filePath);
            if (file.exists()) {
                imagesUri.add(Uri.fromFile(file));
//                imagesData.add(new ImageData(Uri.fromFile(file), id, dateAdded, bucketName));
            }
        }
        // The cursor should be freed up after use with close()
        cursor.close();
        return imagesUri;
    }
}
