package com.fbo.remoteraspberry.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Fred on 28/06/2015.
 */
public class SimpleArrayAdapter<T> extends ArrayAdapter<T> {

    public SimpleArrayAdapter(Context context, List<T> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }


}
