package com.gabriel.hwman.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import java.lang.reflect.Field;
import java.util.HashMap;

public class UIManager {

    public static void createDeleteDialog(FragmentManager fm, Context ctx, String msg, String seq){
        DeleteDialog wdf = new DeleteDialog(ctx, msg, seq);
        wdf.show(fm,  "");
    }

    public static void createResetDialog(FragmentManager fm, Context ctx, String msg, String seq){
        ResetDialog wdf = new ResetDialog(ctx, msg, seq);
        wdf.show(fm,  "");
    }

    public static Activity getActivity(){
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            HashMap activities = (HashMap) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}
