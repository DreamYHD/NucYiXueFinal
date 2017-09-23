package androidlab.edu.cn.nucyixue.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidlab.edu.cn.nucyixue.R;

/**
 * Created by dreamY on 2017/9/9.
 */

public class BigImageDialog extends AlertDialog {
    private static final String TAG = "BigImageDialog";
    private ImageView mImageView;
    private Context mContext;
    private String mString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.big_image, null);
        setContentView(view);
        mImageView = (ImageView) view.findViewById(R.id.image_big);
        Log.e(TAG, "onCreate: " + mString.toString());
        Glide.with(mContext)
                .load(mString)
                .placeholder(R.drawable.hold)
                .override(1400, 1400)
                .into(mImageView);

    }

    protected BigImageDialog(Context context) {
        super(context);

    }

    protected BigImageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public BigImageDialog(Context context, @StyleRes int themeResId, String imageView) {
        super(context, themeResId);
        this.mContext = context;
        mString = imageView;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
        }


        return true;
    }


}