import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which webscrapes the given item and finds information on its owners.
 */
public class FindOwners {
    private final String URL; // URL of the item I want the last online owners of.
    private final long MAX_SINCE_UPDATE; // The maximum amount of time that a user can own the requested item. If the time is exceeded they will not be added to userInfo.
    private final long MIN_SINCE_UPDATE; // The minimum amount of time that a user can own the requested item. If the time is not met they will not be added to userInfo.
    private final long CURRENT_UNIX_TIME = System.currentTimeMillis();
    private final long ONE_DAY_MILLIS = (long)(8.64 * Math.pow(10,7)); // How long one day is in milliseconds

    /**
     * Constructor which sets the URL to be the URL of the given item id, the maximum days since update, and the minimum days since update.
     * @param id ID of the item which you would like to webscrape.
     * @param maxDays The max amount of days someone can own the item for.
     * @param minDays the min amount of days someone can own the item for.
     * */
    public FindOwners(long id, int maxDays, int minDays) {
        URL = "https://www.rolimons.com/item/" + id;
        MAX_SINCE_UPDATE = CURRENT_UNIX_TIME - maxDays * ONE_DAY_MILLIS;
        MIN_SINCE_UPDATE = CURRENT_UNIX_TIME - minDays * ONE_DAY_MILLIS;
    }

    /**
     * Constructor which sets the URL to be the URL of the given item id and the maximum days since update
     * @param id ID of the item which you would like to webscrape.
     * @param maxDays The max amount of days someone can own the item for.
     * */
    public FindOwners(long id, int maxDays) {
        URL = "https://www.rolimons.com/item/" + id;
        MAX_SINCE_UPDATE = CURRENT_UNIX_TIME - maxDays * ONE_DAY_MILLIS;
        MIN_SINCE_UPDATE = CURRENT_UNIX_TIME - 2 * ONE_DAY_MILLIS;
    }

    /**
     * Constructor which sets the URL to be the URL of the given item id
     * @param id ID of the item which you would like to webscrape.
     * */
    public FindOwners(long id) {
        URL = "https://www.rolimons.com/item/" + id;
        MAX_SINCE_UPDATE = CURRENT_UNIX_TIME - 31 * ONE_DAY_MILLIS;
        MIN_SINCE_UPDATE = CURRENT_UNIX_TIME - 2 * ONE_DAY_MILLIS;
    }

    /**
     * Get the premium owners who were last online
     * */
    public List<UserInfo> getUserInfo() throws Exception {
        List<Long> addedIds = new ArrayList<>();
        List<UserInfo> userInfo = new ArrayList<>(); // Array list of UserInfo.

        try {
            final Document doc = Jsoup.connect(URL).get();
            final String bcCopiesData = doc.select("script:containsData(num_bc_copies)").first().toString(); // Select the script that has owner information

            final String[] bcOwners = bcCopiesData.substring(bcCopiesData.indexOf('[', bcCopiesData.indexOf("num_bc_copies")) + 1, bcCopiesData.indexOf(']', bcCopiesData.indexOf("num_bc_copies"))).split(","); // Separate the bc owners from the html and make a String array of bc owner ids.
            final String[] bcUpdated = bcCopiesData.substring(bcCopiesData.indexOf('[', bcCopiesData.indexOf("\"bc_updated\":")) +1, bcCopiesData.indexOf(']', bcCopiesData.indexOf("\"bc_updated\":"))).split(","); // Separate the time at which one of the specified item was last updated from the html and make a String array of those times.
            final String[] bcUaids = bcCopiesData.substring(bcCopiesData.indexOf('[', bcCopiesData.indexOf("\"bc_uaids\":")) +1, bcCopiesData.indexOf(']', bcCopiesData.indexOf("\"bc_uaids\":"))).replace("\"", "").split(","); // Separate bc uaids from the html by replacing \" with "" in the string and then make a String array of bc uaids in the new String.

            for(int i = 0; i < bcOwners.length; i++) {
                if(!addedIds.contains(Long.decode(bcOwners[i]))) {
                    userInfo.add(new UserInfo(Long.decode(bcOwners[i]), Long.decode(bcUpdated[i]), Long.decode(bcUaids[i])));
                    addedIds.add(Long.decode(bcOwners[i]));
                }
            }

            int removed = 0;
            for(int i = 1; i < userInfo.size(); i++) {

                if(userInfo.get(i).getLastUpdated() > MIN_SINCE_UPDATE || userInfo.get(i).getLastUpdated() < MAX_SINCE_UPDATE) {
                    removed++;
                    userInfo.remove(i);
                    i--;
                } else {
                    UserInfo temp = userInfo.get(i);
                    int sortIndex = i - 1;

                    while (sortIndex >= 0 && temp.getLastUpdated() < userInfo.get(sortIndex).getLastUpdated()) {
                        userInfo.set(sortIndex + 1, userInfo.get(sortIndex));
                        sortIndex--;
                    }
                    userInfo.set(sortIndex + 1, temp);
                }
            }

            userInfo.remove(0);

            userInfo = userInfo.reversed(); // I don't want to fuck with the sorting algorithm anymore, so I'm choosing the lazy option.
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
