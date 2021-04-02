package main.java.com.google.sps.servlets;


import com.google.appengine.repackaged.com.google.type.LatLng;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.gson.Gson;
import com.google.gwt.thirdparty.guava.common.collect.Maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.*; 
import java.time.format.DateTimeFormatter;


@WebServlet("/MatchingAlgoLocation")
public class MatchingAlgoLocation extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
        //get objects in datastore
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        Query<Entity> query = Query.newEntityQueryBuilder().setKind("Task").build();
        QueryResults<Entity> results = datastore.run(query);

        //this will effectively create a perimiter around the locations.
        //from there it is easy to calculate middle
        //https://developers.google.com/maps/documentation/javascript/reference/coordinates#LatLngBounds
        var rectangleFromPoints = new com.google.maps.LatLngBounds();

        //the week 3 maps tutorial only used javascript, maybe thats why imports are funky
        //check out https://github.com/googlemaps/google-maps-services-java

        //get the objects from the query, and add to rectangle
        while (results.hasNext()) {
            Entity entity = results.next();
            LatLng temp = entity.getLatLng("Location");
            rectangleFromPoints.extend(temp);

        }

        //now we have our rectangle, so we need to find the center
        //https://developers.google.com/maps/documentation/javascript/reference/map#Map.getCenter
        LatLng Center = rectangleFromPoints.getCenter();
        
        //now we can return that point
        //return Center
        response.setContentType("application/json;");
        String toReturn = convertToJsonUsingGson(Center);
        response.getWriter().println(toReturn);
    }

    private String convertToJsonUsingGson(LatLng x) {
        //for info on gson's and latlng objects
        //https://sites.google.com/site/gson/gson-user-guide
        Gson gson = new Gson();
        String newt = gson.toJson(x);
        return newt;
    }
}