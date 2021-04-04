
package main.java.com.google.sps.servlets;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.sps.data.Event;
import java.util.HashSet;

@WebServlet("/MatchingAlgoDate")
public class MatchingDateAlgo extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Used to keep track of the good dates. HashSet used for constant "contains" time.
        HashSet<LongValue> GoodDates = new HashSet<LongValue>();

        //Used to hold dates for each user from datastore
        List<LongValue> userDays;

        //Used to *temporarily* hold the values which overlap in current GoodDates and userDays
        HashSet<LongValue> temps = new HashSet<LongValue>();

        //get objects in datastore
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        String currEvent = request.getParameter("text-input");
        Query<Entity> query = Query.newEntityQueryBuilder().setKind("Event").setFilter(PropertyFilter.eq("event-id", currEvent)).build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            userDays = entity.getArrayData("dates");
            if(GoodDates.isEmpty()){
                for(int i = 0; i < userDays.size(); i++){
                    GoodDates.add(userDays[i]);
                }
            }
            else{
                for(int i = 0; i < userDays.size(); i++){
                    if(GoodDates.cotains(userDays[i])){
                        temps.add(userDays[i]);
                    }
                }
                GoodDates = temps;
                temps.clear();
            }
        }
        
        Gson bson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(bson.toJson(Center));
    }
}

/*

   this was the psuedocode for date ranges

   this problem looks related https://leetcode.com/discuss/interview-question/335118/amazon-onsite-find-free-time-for-meetup

   Recieve list of user's date ranges
   AllDates = [[user 1], [user 2], etc]

   GoodDates = []
   for i in AllDates[0]{
      GoodDates.Add(i)
   }

   //need to iterate over all users
   for(int i = 1; i < AllDates.length(); i++){
      //need to iterate over all user's available dates
      temp = []
      for(int g = 0; g < AllDates[i].length(); g++){
         //need to iterate over all of GoodDates
         for(int b = 0; b < GoodDates[i].length(); b++){
            //alldates[i][g][0] is basically saying alldates[user][date pair][beginning]
            //we need to check that the first range begins before the second ends, and that the second
            //begins before the first ends
            if((AllDates[i][g][0] <= GoodDates[b][1]) && (AllDates[i][g][1] <= GoodDates[b][0])){
               temp.add([max(AllDates[i][g][0], GoodDates[b][0]), min(AllDates[i][g][1], GoodDates[b][1])])
            }
      }
      delete[] GoodDates
      GoodDates = temp
   }
   
   return GoodDates

*/