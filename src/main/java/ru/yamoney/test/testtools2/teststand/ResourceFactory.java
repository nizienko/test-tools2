package ru.yamoney.test.testtools2.teststand;

/**
 * Created by def on 06.06.15.
 */
public class ResourceFactory {
    public Resource getResource(String type){
        if (type.equals(ResourceType.HTTP.name())) {
            return new HttpResource();
        }
        else if (type.equals(ResourceType.SELECT.name())){
            return new SelectResource();
        }
        else return null;
    }
}
