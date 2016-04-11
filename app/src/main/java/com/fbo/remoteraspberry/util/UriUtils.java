package com.fbo.remoteraspberry.util;

/**
 * Created by Fred on 28/06/2015.
 */
public class UriUtils {

    public static final int SERVICE_PORT = 8080;
    public static final String SERVICES_URI_PATTERN = "http://%s:%d/services";

    public static final String getServiceUri(String ipAddress) {
        return String.format(SERVICES_URI_PATTERN, ipAddress, SERVICE_PORT);
    }
}
