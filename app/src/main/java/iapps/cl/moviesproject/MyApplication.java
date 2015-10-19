package iapps.cl.moviesproject;

import android.app.Application;
import android.content.Context;

/**
 * Created by iSaias on 10/17/15.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        this.setAppContext(getApplicationContext());

    }

    public static MyApplication getInstance(){

        return mInstance;
    }

    public static Context getAppContext() {

        return mAppContext;
    }

    public void setAppContext(Context mAppContext) {

        this.mAppContext = mAppContext;
    }
}
