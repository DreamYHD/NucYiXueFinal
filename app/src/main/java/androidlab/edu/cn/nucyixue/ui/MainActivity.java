package androidlab.edu.cn.nucyixue.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import androidlab.edu.cn.nucyixue.data.bean.Book;
import androidlab.edu.cn.nucyixue.data.bean.Keyword;
import androidlab.edu.cn.nucyixue.data.bean.OCRResult;
import androidlab.edu.cn.nucyixue.net.KeywordService;
import androidlab.edu.cn.nucyixue.net.Service;
import androidlab.edu.cn.nucyixue.ocr.FileUtil;
import androidlab.edu.cn.nucyixue.ocr.RecognizeService;
import androidlab.edu.cn.nucyixue.ui.findPack.FindFragment;
import androidlab.edu.cn.nucyixue.ui.map.TeachMapFragment;
import androidlab.edu.cn.nucyixue.ui.mePack.MeFragment;
import androidlab.edu.cn.nucyixue.ui.teachPack.TeachFragment;
import androidlab.edu.cn.nucyixue.ui.teachPack.live.CommonLiveFragment;
import androidlab.edu.cn.nucyixue.ui.teachPack.xuanshangPack.XuanshangFragment;
import androidlab.edu.cn.nucyixue.utils.ActivityUtils;
import androidlab.edu.cn.nucyixue.utils.BottomNavigationViewHelper;
import butterknife.BindView;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * requestCode 错误
 * Fragment 单例 问题
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.bottom_menu)
    BottomNavigationView mBottomMenu;

    private static final int SCANNIN_GREQUEST_CODE = 1;
    private static final int REQUEST_CODE_GENERAL = 105;

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        if (mSavedInstanceState==null){
            ActivityUtils.replaceFragmentToActivity(mFragmentManager,FindFragment.getInstance(),R.id.content_main);
        }
        BottomNavigationViewHelper.disableShiftMode(mBottomMenu);
        mBottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.find_item:
                        ActivityUtils.replaceFragmentToActivity(mFragmentManager,FindFragment.getInstance(),R.id.content_main);
                        break;
                    case R.id.teach_item:
                        ActivityUtils.replaceFragmentToActivity(mFragmentManager, TeachFragment.getInstance(),R.id.content_main);
                        break;
                    case R.id.addque_item:
                        ActivityUtils.replaceFragmentToActivity(mFragmentManager, TeachMapFragment.INSTANCE,R.id.content_main);
                        break;
                    case R.id.me_item:
                        ActivityUtils.replaceFragmentToActivity(mFragmentManager, MeFragment.getInstance(),R.id.content_main);
                        break;
                }

                return true;
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("temp", mBottomMenu.getSelectedItemId()+"");
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult" + resultCode +" "+ requestCode);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_GENERAL:
                    RecognizeService.recGeneral(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                            new RecognizeService.ServiceListener() {
                                @Override
                                public void onResult(String result) {
                                    Log.i(TAG, result);
                                    OCRResult ocr = new Gson().fromJson(result, OCRResult.class);
                                    List<OCRResult.WordsResult> wordsResult = ocr.getWords_result();
                                    StringBuilder sb = new StringBuilder();
                                    for (OCRResult.WordsResult word : wordsResult){
                                       sb.append(word.getWords());
                                    }

                                    Service.INSTANCE.getApi_keyword()
                                            .getKeyword(sb.toString(), 3)
                                            .observeOn(Schedulers.io())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(
                                                    new Consumer<Keyword>() {
                                                        @Override
                                                        public void accept(Keyword keyword) throws Exception {
                                                            Log.i(TAG, "keys : " + keyword.getShowapi_res_body().getList().get(0));
                                                            ArrayList<String> keys = new ArrayList<>();
                                                            keys.addAll(keyword.getShowapi_res_body().getList());
                                                            Bundle b = new Bundle();
                                                            b.putSerializable("keys", keys);
                                                            CommonLiveFragment fragment = new CommonLiveFragment();
                                                            fragment.setArguments(b);
                                                            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.content_main);
                                                        }
                                                    },
                                                    new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {
                                                            Log.i(TAG, "Get Keyword error:" + throwable.toString());
                                                        }
                                                    }
                                            );
                                }
                            });
                    break;
                case SCANNIN_GREQUEST_CODE:
                    Bundle bundle = data.getExtras();
                    String result = bundle.getString("result");
                    if(bundle.getString("result") != null){
                        Log.i(TAG, "result:" + result);
                        Service.INSTANCE.getApi_douban().getBookInfo(result)
                                .observeOn(Schedulers.io())
                                .subscribeOn(Schedulers.io())
                                .subscribe(
                                        new Consumer<Book>() {
                                            @Override
                                            public void accept(Book book) throws Exception {
                                                searchKeyword(book);
                                            }
                                        },
                                        new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                Log.i(TAG, "get Book Fail :" + throwable.toString());
                                            }
                                        }
                                );
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void searchKeyword(Book book){
        Log.i(TAG, "book : " + book.toString());
        List<Book.Tags> tags = book.getTags();
        ArrayList<String> keys = new ArrayList<>();
        for(Book.Tags tag : tags){
            keys.add(tag.getName());
            Log.i(TAG,"tag:"+ tag.getName());
        }

        Bundle b = new Bundle();
        b.putSerializable("keys", keys);
        CommonLiveFragment fragment = new CommonLiveFragment();
        fragment.setArguments(b);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.content_main);
    }

}
