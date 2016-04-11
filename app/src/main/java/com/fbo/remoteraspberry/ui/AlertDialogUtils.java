package com.fbo.remoteraspberry.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.fbo.remoteraspberry.R;

/**
 * Created by Fred on 09/11/2014.
 */
public class AlertDialogUtils {

    public static void showAlertDialog(Context context, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.OK_button, null);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    public static void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.OK_button, null);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }


    public static void showActionDialog(Context context, String title, String message, DialogInterface.OnClickListener yesAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.YES_button, yesAction);
        builder.setNegativeButton(R.string.NO_button, null);
        builder.setIcon(android.R.drawable.ic_dialog_dialer);
        builder.show();
    }

    public static void showIntegerChoiceDialog(Context context, String title, int min, int max, int value, final Consumer<Integer> valueConsumer) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        final NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(value);
        builder.setView(numberPicker);
        builder.setPositiveButton(R.string.OK_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newValue = numberPicker.getValue();
                valueConsumer.consume(newValue);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void showSingleChoiceDialog(Context context, String title, String[] choices, final Consumer<Integer> selectedIdConsumer) {
        showSingleChoiceDialog(context, title, choices, -1, selectedIdConsumer);
    }

    public static void showSingleChoiceDialog(Context context, String title, String[] choices, int selectedId, final Consumer<Integer> selectedIdConsumer) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setSingleChoiceItems(choices, selectedId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedIdConsumer.consume(which);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public interface Consumer<T> {
        public void consume(T object);
    }

    public interface OnValueValidatedListener<T> {
        public void validate(T value);
    }


}
