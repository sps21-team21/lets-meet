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
import java.lang.Math.*;
//import com.google.maps.*;
//import com.google.android.gms.maps.model;
//import com.amap.api.maps.model.LatLngBounds;
//import com.amap.api.maps.model.LatLngBounds;
//import com.google.code.geocoder-java.*;
//import org.primefaces.model.map.LatLngBounds;



@WebServlet("/MatchingAlgoLocation")
public class MatchingLocationAlgo extends HttpServlet {
    
    //option 2, where we process the center here in the servlet manually using math.
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        String currEvent = request.getParameter("text-input");
        Query<Entity> query = Query.newEntityQueryBuilder().setKind("Event").setFilter(PropertyFilter.eq("event-id", currEvent)).build();
        QueryResults<Entity> results = datastore.run(query);

        ArrayList<Double> lats =  new ArrayList<>();
        ArrayList<Double> longs =  new ArrayList<>();

        while (results.hasNext()) {
            Entity entity = results.next();
            LatLng temp = entity.getLatLng("location");
            lats.add(temp.getLatitude()*Math.PI/180);
            longs.add(temp.getLongitude()*Math.PI/180);

        }

        ArrayList<Double> xs =  new ArrayList<>();
        ArrayList<Double> ys =  new ArrayList<>();
        ArrayList<Double> zs =  new ArrayList<>();
        for(int i = 0; i < lats.size(); i++){
            xs.add(Math.cos(lats.get(i)) * Math.cos(longs.get(i)));
            ys.add(Math.cos(lats.get(i)) * Math.sin(longs.get(i)));
            zs.add(Math.sin(lats.get(i)));
        }

        double trueX = 0;
        double trueY = 0;
        double trueZ = 0;

        for(int i = 0; i < xs.size(); i++){
            trueX = trueX + xs.get(i);
            trueY = trueY + ys.get(i);
            trueZ = trueZ + zs.get(i);
        }
        trueX = trueX / xs.size();
        trueY = trueY / ys.size();
        trueZ = trueZ / zs.size();

        Double Hypo = Math.sqrt(trueX * trueX + trueY * trueY);
        //LatLng Center = new LatLng(Math.atan2(trueZ, Hypo)*180/(Math.PI), Math.atan2(trueX,trueY)*180/(Math.PI));
        //apparently LatLng constructor is private
        Double[] Center = {Math.atan2(trueX,trueY)*180/(Math.PI), Math.atan2(trueZ, Hypo)*180/(Math.PI)};
        Gson bson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(bson.toJson(Center));
    }
    
    /*
    //option 1, where we send points to javascript page and process them there.
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
        //move finding center to javascript, and leave java to retrieve points?
        //LatLngBounds rectangleFromPoints = new LatLngBounds();
        
        //the week 3 maps tutorial only used javascript, maybe thats why imports are funky
        //check out https://github.com/googlemaps/google-maps-services-java

        ArrayList<LatLng> points =  new ArrayList<>();
        //get the objects from the query, and add to rectangle
        while (results.hasNext()) {
            Entity entity = results.next();
            LatLng temp = entity.getLatLng("location");            
            points.add(temp);
            //rectangleFromPoints.extend(temp);
            
        }

        //now we have our rectangle, so we need to find the center
        //https://developers.google.com/maps/documentation/javascript/reference/map#Map.getCenter
        //LatLng Center = rectangleFromPoints.getCenter();
        
        //now we can return that point
        //return Center
        Gson bson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(bson.toJson(points));
    }
    */
}



