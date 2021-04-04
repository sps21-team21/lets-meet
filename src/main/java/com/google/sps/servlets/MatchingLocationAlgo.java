package com.google.sps.servlets;


import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.*; 
import java.time.format.DateTimeFormatter;
import com.google.maps.*;


@WebServlet("/MatchingAlgoLocation")
public class MatchingLocationAlgo extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
        //get objects in datastore
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        String currEvent = request.getParameter("text-input");
        Query<Entity> query = Query.newEntityQueryBuilder().setKind("Event").setFilter(PropertyFilter.eq("event-id", currEvent)).build();
        QueryResults<Entity> results = datastore.run(query);

        //this will effectively create a perimiter around the locations. From there it is easy to calculate middle
        //https://developers.google.com/maps/documentation/javascript/reference/coordinates#LatLngBounds
        //apparently the java google maps package doesn't support LatLngBounds, only javascript ヽ༼ ಠ益ಠ ༽ﾉ
        LatLngBounds rectangleFromPoints = new LatLngBounds();

        //the week 3 maps tutorial only used javascript, maybe thats why imports are funky
        //check out https://github.com/googlemaps/google-maps-services-java

        //get the objects from the query, and add to rectangle
        while (results.hasNext()) {
            Entity entity = results.next();
            LatLng temp = entity.getLatLng("location");
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