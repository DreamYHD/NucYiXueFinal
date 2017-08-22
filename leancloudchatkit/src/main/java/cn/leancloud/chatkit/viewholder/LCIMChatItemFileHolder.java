package cn.leancloud.chatkit.viewholder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.io.File;
import java.util.Objects;

import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.cache.LCIMLocalCacheUtils;
import cn.leancloud.chatkit.utils.FileUtils;
import cn.leancloud.chatkit.utils.LCIMPathUtils;


public class LCIMChatItemFileHolder extends LCIMChatItemHolder {

    private TextView fileName;
    private TextView fileSize;
    private ImageView download;

    private Context context;

    private static double itemMaxWidth = 0;

    public LCIMChatItemFileHolder(Context context, ViewGroup root, boolean isLeft) {
        super(context, root, isLeft);
        this.context = context;
    }

    @Override
    public void initView() {
        super.initView();
        if (isLeft) {
            conventLayout.addView(View.inflate(getContext(), R.layout.lcim_chat_item_file_left_layout, null));
        } else {
            conventLayout.addView(View.inflate(getContext(), R.layout.lcim_chat_item_file_right_layout, null));
        }
        fileName =  itemView.findViewById(R.id.chat_item_file_name_view);
        fileSize =  itemView.findViewById(R.id.size);
        download = itemView.findViewById(R.id.download);

        if (itemMaxWidth <= 0) {
            itemMaxWidth = itemView.getResources().getDisplayMetrics().widthPixels * 0.6;
        }

    }

    @Override
    public void bindData(Object o) {
        super.bindData(o);
        if (o instanceof AVIMTextMessage) {
            AVIMTextMessage msg = (AVIMTextMessage) o;
            fileName.setText(msg.getText());

            Object t = msg.getAttrs().get("size");
            if(t != null){
                int len = (int) t;
                String size = FileUtils.byte2MemorySize(len);
                fileSize.setText(size);
            }


            final String url = (String) msg.getAttrs().get("url");
            final String path = LCIMPathUtils.getAudioCachePath(context, FileUtils.getFileName(url));
            Log.i("File", "url:"+url+" path:"+path);
            if(path != null){
                final File file = new File(path);
                if(file.exists()){
                    download.setImageResource(R.drawable.ic_check_black_24dp);
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openFile(file);
                        }
                    });
                }else{
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LCIMLocalCacheUtils.downloadFile(url, path, true, new LCIMLocalCacheUtils.DownLoadCallback() {
                                @Override
                                public void done(Exception e) {
                                    if(e == null){
                                        Toast.makeText(context, "下载完成", Toast.LENGTH_LONG).show();
                                        download.setImageResource(R.drawable.ic_check_black_24dp);
                                        download.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                openFile(file);
                                            }
                                        });
                                    }else{
                                        Log.i("File",e.toString());
                                    }
                                }
                            });
                        }
                    });
                }
            }

        }
    }

    private void openFile(File file){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        context.startActivity(intent);
    }

    private String getMIMEType(File file) {

        String type="*/*";
        String fName = file.getName();
        int dotIndex = fName.lastIndexOf(".");  //获取后缀名前的分隔符"."在fName中的位置。
        if(dotIndex < 0){
            return type;
        }

        String end=fName.substring(dotIndex,fName.length()).toLowerCase();  /* 获取文件的后缀名*/
        if(Objects.equals(end, ""))return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if(end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private final String[][] MIME_MapTable={
            //{后缀名，MIME类型}
            {".3gp",    "video/3gpp"},
            {".apk",    "application/vnd.android.package-archive"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".bin",    "application/octet-stream"},
            {".bmp",    "image/bmp"},
            {".c",      "text/plain"},
            {".class",  "application/octet-stream"},
            {".conf",   "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls",    "application/vnd.ms-excel"},
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe",    "application/octet-stream"},
            {".gif",    "image/gif"},
            {".gtar",   "application/x-gtar"},
            {".gz",     "application/x-gzip"},
            {".h",      "text/plain"},
            {".htm",    "text/html"},
            {".html",   "text/html"},
            {".jar",    "application/java-archive"},
            {".java",   "text/plain"},
            {".jpeg",   "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".js",     "application/x-javascript"},
            {".log",    "text/plain"},
            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".mpc",    "application/vnd.mpohun.certificate"},
            {".mpe",    "video/mpeg"},
            {".mpeg",   "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",   "video/mp4"},
            {".mpga",   "audio/mpeg"},
            {".msg",    "application/vnd.ms-outlook"},
            {".ogg",    "audio/ogg"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop",   "text/plain"},
            {".rc",     "text/plain"},
            {".rmvb",   "audio/x-pn-realaudio"},
            {".rtf",    "application/rtf"},
            {".sh",     "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".wps",    "application/vnd.ms-works"},
            {".xml",    "text/plain"},
            {".z",      "application/x-compress"},
            {".zip",    "application/x-zip-compressed"},
            {"",        "*/*"}
    };


}