package www.r886.top.mobileplayer.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import www.r886.top.mobileplayer.R;


public class AudioFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      //  Log.e("Mess", getClass().getSimpleName()+"=onCreateView: " );
        return inflater.inflate(R.layout.fragment_audio, container, false);
    }

    @Override
    public void onStart() {
     //   Log.e("Mess", getClass().getSimpleName()+"=onStart: " );
        super.onStart();
    }

    @Override
    public void onResume() {
     //   Log.e("Mess", getClass().getSimpleName()+"=onResume: " );
        super.onResume();
    }

    @Override
    public void onPause() {
       // Log.e("Mess", getClass().getSimpleName()+"=onPause: " );
        super.onPause();
    }

    @Override
    public void onDestroy() {
     //   Log.e("Mess", getClass().getSimpleName()+"=onDestroy: " );
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) return;
        //Log.e("Mess", getClass().getSimpleName()+"=onHiddenChanged: ");
    }
}
