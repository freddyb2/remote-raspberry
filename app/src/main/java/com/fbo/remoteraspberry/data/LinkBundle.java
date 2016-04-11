package com.fbo.remoteraspberry.data;

import com.fbo.services.core.resource.LinksResource;

import org.springframework.hateoas.Link;

/**
 * Created by Fred on 28/06/2015.
 */
public class LinkBundle {

    private final String[] rels;
    private final String[] hrefs;

    public LinkBundle(String[] rels, String[] hrefs) {
        this.rels = rels;
        this.hrefs = hrefs;
    }

    public LinkBundle(LinkList linkList) {
        this.rels = new String[linkList.size()];
        this.hrefs = new String[linkList.size()];
        int i = 0;
        for (Link l : linkList) {
            rels[i] = l.getRel();
            hrefs[i] = l.getHref();
            i++;
        }
    }

    public String[] getRels() {
        return rels;
    }

    public String[] getHrefs() {
        return hrefs;
    }
}
