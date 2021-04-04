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
import static com.google.sps.Constants.*;

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
public class MatchingLocationAlgo extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
        //get objects in datastore
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        String currEvent = request.getParameter("text-input");
        Query<Entity> query = Query.newEntityQueryBuilder().setKind("Event").setFilter(PropertyFilter.eq("event-id", currEvent)).build();
        QueryResults<Entity> results = datastore.run(query);

        //this will effectively create a perimiter around the locations.
        //from there it is easy to calculate middle
        //https://developers.google.com/maps/documentation/javascript/reference/coordinates#LatLngBounds
        LatLngBounds rectangleFromPoints = new com.google.maps.LatLngBounds();

        //the week 3 maps tutorial only used javascript, maybe thats why imports are funky
        //check out https://github.com/googlemaps/google-maps-services-java

        //get the objects from the query, and add to rectangle
        while (results.hasNext()) {
            Entity entity = results.next();
            LatLng temp = entity.getGeoPointValue("location");
            rectangleFromPoints.extend(temp);

        }

        //now we have our rectangle, so we need to find the center
        //https://developers.google.com/maps/documentation/javascript/reference/map#Map.getCenter
        LatLng Center = rectangleFromPoints.getCenter();
        
        //now we can return that point
        //return Center
        Gson bson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(bson.toJson(Center));
    }
}