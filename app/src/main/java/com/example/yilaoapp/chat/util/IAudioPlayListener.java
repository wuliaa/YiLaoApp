package com.example.yilaoapp.chat.util;

import android.net.Uri;

public interface IAudioPlayListener {
    void onStart(Uri var1);

    void onStop(Uri var1);

    void onComplete(Uri var1);
}