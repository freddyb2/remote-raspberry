package com.fbo.remoteraspberry.activity;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fbo.remoteraspberry.R;
import com.fbo.remoteraspberry.adapter.specific.LinkArrayAdapter;
import com.fbo.remoteraspberry.data.LinkList;
import com.fbo.remoteraspberry.internet.Sustainable;
import com.fbo.remoteraspberry.util.ProgressUtils;

import org.springframework.hateoas.Link;
import static com.fbo.services.core.model.GenericParameter.PARAMETER_PREFIX_NAME;

/**
 * Created by Fred on 28/06/2015.
 */
public class LinkListFragment extends ListFragment implements Sustainable {

    public LinkListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_linklist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] rels = getArguments().getStringArray(ServiceActivity.REL_LINKS_EXTRA_ID);
        String[] refs = getArguments().getStringArray(ServiceActivity.HREF_LINKS_EXTRA_ID);
        if (rels != null && refs != null) {
            //TODO Previous....
            setListAdapter(new LinkArrayAdapter(getActivity(), new LinkList(rels, refs)));
        }
    }

    @Override
    public void setSustained(boolean b) {
        ProgressUtils.showProgress(b, getActivity(), getActivity().findViewById(android.R.id.list));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Link link = (Link) getListAdapter().getItem(position);

        if (link.getRel().startsWith(PARAMETER_PREFIX_NAME)) {
            ServiceActivity.followToEditor(link.getHref(), getActivity(), this);
        } else {
            ServiceActivity.followToLinkList(link.getHref(), getActivity(), this);
        }

    }


}
