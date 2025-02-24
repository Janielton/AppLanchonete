package com.jhonjhon.lanches;

import android.app.Application;

import com.jhonjhon.lanches.classes.RecebeNotificacao;
import com.onesignal.OneSignal;


public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)

                .setNotificationOpenedHandler(new RecebeNotificacao(getApplicationContext()))
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
