package androidlab.edu.cn.nucyixue.ui.findPack.subject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidlab.edu.cn.nucyixue.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectLiveFragment extends Fragment {


    public SubjectLiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subject_live, container, false);
    }

}
