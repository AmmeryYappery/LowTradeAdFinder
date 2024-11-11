import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class OfferLogic {
    private final static HashMap<Long, Bodies.TradeFormat.SideFormat> offers = new HashMap<>();

    /**
     * Add an item offer to offers.
     * @param itemID The id of the item you want to offer on
     * @param offer An SideFormat (Which is an offer or request of a trade) details what you will offer
     * */
    public static void addOffer(long itemID, Bodies.TradeFormat.SideFormat offer) {
        offers.put(itemID, offer);
    }

    /**
     * Add an item offer to offers for each item id.
     * @param itemIDs An array of items you want to offer on
     * @param offer An SideFormat (Which is an offer or request of a trade) details what you will offer
     * */
    public static void multipleSameAddOffer(long[] itemIDs, Bodies.TradeFormat.SideFormat offer) {
        for(long itemID : itemIDs) {
            addOffer(itemID, offer);
        }
    }

    public static String createPayload(long itemID, UserInfo user, long yourUserId) {
        Gson gson = new Gson();
        ArrayList<Long> requestItems = new ArrayList<>();
        requestItems.add(user.getUaid());

        try {
            return gson.toJson(
                    new Bodies.TradeFormat(
                            offers.get(itemID),
                            new Bodies.TradeFormat.SideFormat(user.getId(), requestItems, 0)
                    )
            );
        } catch (Exception e) {
            return "";
        }
    }
}
