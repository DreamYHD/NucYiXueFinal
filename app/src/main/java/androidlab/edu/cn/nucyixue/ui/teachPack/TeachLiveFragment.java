package androidlab.edu.cn.nucyixue.ui.teachPack;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidlab.edu.cn.nucyixue.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeachLiveFragment extends Fragment {

    private static final String TAG = "TeachLiveFragment";

    public static TeachLiveFragment getInstance() {
        // Required empty public constructor
        return new TeachLiveFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView: "+TAG);
        return inflater.inflate(R.layout.fragment_teach_live, container, false);
    }


}
