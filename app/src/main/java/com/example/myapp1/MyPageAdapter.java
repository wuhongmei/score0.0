package com.example.myapp1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPageAdapter extends FragmentStateAdapter {

    public MyPageAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return new myFragment1();
        }else if(position==1){
            return new myFragment2();
        }else{
            return new myFragment3();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
