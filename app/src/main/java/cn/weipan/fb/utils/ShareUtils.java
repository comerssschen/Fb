package cn.weipan.fb.utils;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by cc on 2016/10/9.
 * 分享到第三方
 */
public class ShareUtils {

    //这些都是分享时，对应的包名和类名。怎么获取到的？嘿嘿。。
    // 微信好友
    // package = com.tencent.mm,
    // activity = com.tencent.mm.ui.tools.ShareImgUI
    // 微信朋友圈
    // package = com.tencent.mm,
    // activity = com.tencent.mm.ui.tools.ShareToTimeLineUI
    //
    // package = com.tencent.mobileqq,
    // activity = com.tencent.mobileqq.activity.JumpActivity
    // package = com.tencent.mobileqq,
    // activity = com.tencent.mobileqq.activity.qfileJumpActivity
    // QQ空间
    // package = com.qzone, activity =
    // com.qzone.ui.operation.QZonePublishMoodActivity
    // 人人
    // package = com.renren.mobile.android,
    // activity = com.renren.mobile.android.publisher.UploadPhotoEffect
    // 陌陌
    // package = com.immomo.momo, activity =
    // com.immomo.momo.android.activity.feed.SharePublishFeedActivity
    // 新浪微博
    // package = com.sina.weibo, activity = com.sina.weibo.EditActivity
    // 腾讯微博
    // package = com.tencent.WBlog, activity =
    // com.tencent.WBlog.intentproxy.TencentWeiboIntent

//packageName = im.yixin, name = im.yixin.activity.share.ShareToSnsActivity
//packageName = im.yixin, name = im.yixin.activity.share.ShareToSessionActivity
//packageName = com.alibaba.android.babylon, name = com.alibaba.android.babylon.biz.im.activity.RecentIMListActivity
//packageName = com.alibaba.android.babylon, name = com.alibaba.android.babylon.biz.home.activity.CreateFeedActivity

    /**
     * 分享到QQ好友
     *
     */
    private void shareToQQFriend() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT, "这是分享内容");
//        startActivity(intent);
    }
    /**
     * 分享信息到朋友
     *
     */
    private void shareToWxFriend() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT, "这是分享内容");
        intent.putExtra(Intent.EXTRA_STREAM, "http://www.weixin.com");
//        startActivity(intent);
    }
    /**
     * 分享信息到朋友圈
     *
     * @param file
     *            ，假如图片的路径为path，那么file = new File(path);
     */
    private void shareToTimeLine(File file) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(componentName);

        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        // intent.setAction(android.content.Intent.ACTION_SEND_MULTIPLE);
        // ArrayList<Uri> uris = new ArrayList<Uri>();
        // for (int i = 0; i < images.size(); i++) {
        // Uri data = http://blog.csdn.net/HMYANG314/article/details/Uri.fromFile(new File(thumbPaths.get(i)));
        // uris.add(data);
        // }
        // intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        intent.setType("image/*");

//        startActivity(intent);
    }
    /**
     * 分享图片给微信好友
     *
     * @param file
     */
    private void shareToWxFriend(File file) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//        startActivity(intent);
    }


    /**
     * 分享多图到朋友圈，多张图片加文字
     *
     * @param uris
     */
    private void shareToTimeLine(String title, ArrayList<Uri> uris) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");

        intent.putExtra("Kdescription", title);

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//        startActivity(intent);
    }
}
