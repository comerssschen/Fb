package cn.weipan.fb.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Administrator on 2016/11/1.
 */
public class BitmapUtils {

//    private File uri2File(Uri uri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
//        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        actualimagecursor.moveToFirst();
//        String img_path = actualimagecursor.getString(actual_image_column_index);
//        File file = new File(img_path);
//        return file;
//    }
public Bitmap stringtoBitmap(String string) {
    //将字符串转换成Bitmap类型
    Bitmap bitmap = null;
    try {
        byte[] bitmapArray;
        bitmapArray = Base64.decode(string, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return bitmap;
}
}
