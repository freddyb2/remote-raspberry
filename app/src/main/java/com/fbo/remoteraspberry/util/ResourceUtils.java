package com.fbo.remoteraspberry.util;

import com.fbo.remoteraspberry.data.LinkList;
import com.fbo.services.core.model.GenericParameter;
import com.fbo.services.core.resource.LinksResource;

import org.springframework.hateoas.Link;

/**
 * Created by Fred on 28/06/2015.
 */
public class ResourceUtils {

    public static LinkList getDisplayedLinks(LinksResource linksResource) {
        LinkList links = new LinkList();
        for (Link link : linksResource.getLinks()) {
            switch (link.getRel()) {
                case Link.REL_SELF:
                case Link.REL_FIRST:
                case Link.REL_PREVIOUS:
                case Link.REL_NEXT:
                case Link.REL_LAST:
                    // Do not
                    break;
                default:
                    links.add(link);
            }
        }
        return links;
    }

    public static String getDisplayableRel(Link link) {
        return link.getRel().replace(GenericParameter.PARAMETER_PREFIX_NAME, "");
    }

    public static Link getPreviousLink(LinksResource linksResource) {
        return linksResource.getLink(Link.REL_PREVIOUS);
    }

    public static boolean hasPreviousLink(LinksResource linksResource) {
        return getPreviousLink(linksResource) != null;
    }
}
