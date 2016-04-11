package com.fbo.remoteraspberry.adapter.specific;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.springframework.hateoas.Link;

import java.util.List;

import com.fbo.remoteraspberry.adapter.SimpleArrayAdapter;
import com.fbo.remoteraspberry.util.ResourceUtils;

/**
 * Created by Fred on 28/06/2015.
 */
public class LinkArrayAdapter extends SimpleArrayAdapter<Link> {

    public LinkArrayAdapter(Context context, List<Link> links) {
        super(context, links);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Link link = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_activated_1, null);
        }

        final TextView linkRelView = (TextView) convertView.findViewById(android.R.id.text1);
        linkRelView.setText(ResourceUtils.getDisplayableRel(link));

        return convertView;
    }
}
