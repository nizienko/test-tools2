package ru.yamoney.test.testtools2.teststand.resources;

/**
 * Created by def on 06.06.15.
 */
public class ResourceFactory {
    public Resource getResource(String type) {
        if (type.equals(ResourceType.HTTP.name())) {
            return new HttpResource();
        } else if (type.equals(ResourceType.SELECT.name())) {
            return new SelectResource();
        } else if (type.equals(ResourceType.CALYPSO_HTTP.name())) {
            return new CalypsoHttpResource();
        } else if (type.equals(ResourceType.PING.name())) {
            return new PingHttpResource();
        } else return null;
    }
}
