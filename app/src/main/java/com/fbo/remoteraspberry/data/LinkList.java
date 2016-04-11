package com.fbo.remoteraspberry.data;

import org.springframework.hateoas.Link;

import java.util.ArrayList;

/**
 * Created by Fred on 28/06/2015.
 */
public class LinkList extends ArrayList<Link> {

    public LinkList() {
    }

    public LinkList(String[] rels, String[] hrefs) {
        super();
        for (int i = 0; i < rels.length; i++) {
            add(new Link(hrefs[i], rels[i]));
        }
    }


}
