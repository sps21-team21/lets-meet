/*

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
