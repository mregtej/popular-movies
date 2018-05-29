package com.udacity.pmovies.ui.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

/**
 * Helper library for creating AlertDialogs
 */
public class AlertDialogHelper {

    public static AlertDialog createMessage(Context context, String title,
                                            String message, String okbutton, boolean cancelable){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton(okbutton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
        });
        builder.setCancelable(cancelable);
        return builder.create();
    }

    public static  AlertDialog createMessage(Context context, String title,
                                             String message, String okbutton, String cancelbutton,
                                             boolean cancelable){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton(okbutton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
        });
        builder.setNegativeButton(cancelbutton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
        });
        builder.setCancelable(cancelable);
        return builder.create();
    }

    public static  AlertDialog createMessage(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title);
        return builder.create();
    }

    public static  AlertDialog createMessage(Context context, String title,
                                             LayoutInflater inflater, int view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        inflater.inflate(view,null);
        builder.setTitle(title);
        builder.setView(inflater.inflate(view,null));
        return builder.create();
    }

    public static AlertDialog createMessage(Context context, String title,
                                            String message, String okbutton,
                                            DialogInterface.OnClickListener oklistener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton(okbutton, oklistener);
        return builder.create();

    }

    public static AlertDialog createMessage(Context context, String title,
                                            String message, String okbutton, String cancelbutton,
                                            String neutralbutton,
                                            DialogInterface.OnClickListener oklistener,
                                            DialogInterface.OnClickListener cancellistener,
                                            DialogInterface.OnClickListener neutrallistener,
                                            DialogInterface.OnDismissListener dismissListener,
                                            boolean cancelable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(cancelbutton, cancellistener);
        builder.setNeutralButton(neutralbutton, neutrallistener);
        builder.setPositiveButton(okbutton, oklistener);
        builder.setOnDismissListener(dismissListener);
        builder.setCancelable(cancelable);
        return builder.create();
    }

    public static AlertDialog createMessage(Context context, String title,
                                            String message, String okbutton, String cancelbutton,
                                            String neutralbutton, boolean cancelable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(cancelbutton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
        });
        builder.setNeutralButton(neutralbutton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
        });
        builder.setPositiveButton(okbutton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
        });
        builder.setCancelable(cancelable);
        return builder.create();
    }

}
