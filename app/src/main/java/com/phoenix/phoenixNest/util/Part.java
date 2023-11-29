package com.phoenix.phoenixNest.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Part {
    public MultipartBody.Part getPart(Context c, Uri uri, String parameterName) throws Exception {
        InputStream fileInputStream = c.getContentResolver().openInputStream(uri);
        Cursor returnCursor =
                c.getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String fileName = returnCursor.getString(nameIndex);

        byte appIconByte[] = new byte[fileInputStream.available()];
        fileInputStream.read(appIconByte);

        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), appIconByte);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(parameterName, fileName, fileRequestBody);
        return filePart;
    }

    public List<MultipartBody.Part> getParts(Context c, List<Uri> u, String parameterName) throws Exception {
        List<MultipartBody.Part> parts = new LinkedList();

        for (Uri uri : u) {


            InputStream fileInputStream = c.getContentResolver().openInputStream(uri);
            Cursor returnCursor =
                    c.getContentResolver().query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String fileName = returnCursor.getString(nameIndex);

            byte appIconByte[] = new byte[fileInputStream.available()];
            fileInputStream.read(appIconByte);

            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), appIconByte);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData(parameterName, fileName, fileRequestBody);


            parts.add(filePart);

        }
        return parts;
    }
}
