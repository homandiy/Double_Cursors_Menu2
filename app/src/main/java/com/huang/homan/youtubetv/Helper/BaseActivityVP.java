package com.huang.homan.youtubetv.Helper;

public interface BaseActivityVP {
    interface View{
        void showMessage(String msg);
    }

    // Handle broadcast receiver
    interface Presenter {
        void sendMessage(String msg);
    }
}
