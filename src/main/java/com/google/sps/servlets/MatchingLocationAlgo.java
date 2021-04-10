package com.google.sps.servlets;


import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import static com.google.sps.Constants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.Math.*;


//This could be very useful for turning returned lat/lng into a city name https://developers.google.com/maps/documentation/javascript/examples/geocoding-reverse
@WebServlet("/MatchingAlgoLocation")
public class MatchingLocationAlgo extends HttpServlet {
    //option 2, where we process the center here in the servlet manually using math.
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        String currEvent = request.getParameter("text-input");
        Query<Entity> query = Query.newEntityQueryBuilder().setKind(USER_KIND).setFilter(PropertyFilter.eq(USER_EVENT_ID_KEY, currEvent)).build();
        QueryResults<Entity> results = datastore.run(query);

        ArrayList<Double> lats =  new ArrayList<>();
        ArrayList<Double> longs =  new ArrayList<>();

        while (results.hasNext()) {
            Entity entity = results.next();
            LatLng temp = entity.getLatLng(USER_LOCATION_KEY);
            lats.add(temp.getLatitude()*Math.PI/180);
            longs.add(temp.getLongitude()*Math.PI/180);

        }

        //convert lats and longs to spherical coordinates
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

        Double Hypo = Math.sqrt((trueX * trueX) + (trueY * trueY));
        Double[] Center = {Math.atan2(trueZ, Hypo)*180/(Math.PI), Math.atan2(trueY,trueX)*180/(Math.PI)};
        Gson bson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(bson.toJson(Center));
        
    }
}



