package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class AmountOfTradeAds {

    private final int maxTradeAds; // The maximum amount of trade ads that a player can have before being remove from the ArrayList of players in FindOwners.
    private final long[] blacklist;
    /**
     * Constructor which sets the maximum amount of trade ads a player have before being excluded from the ArrayList in FindOwners.
     * @param maxTradeAds The maximum amount of trade ads that a player can have before being remove from the ArrayList of players in FindOwners.
     * */
    public AmountOfTradeAds(int maxTradeAds, long[] blacklist) {
        this.maxTradeAds = maxTradeAds;
        this.blacklist = blacklist;
    }

    /**
     * Constructor for AmountOfTradeAds. maxTradeAds is defaulted to 1000
     * */
    public AmountOfTradeAds() {
        maxTradeAds = 1000;
        blacklist = new long[]{};
    }

    public List<String> getOwnersWithLowTradeAdsPayloads(List<UserInfo> userInfo, long itemId, long yourUserId) throws Exception {
        List<String> payloads = new ArrayList<>();

        for(UserInfo user : userInfo) {
            if (!checkBlacklist(user)) {
                System.out.println(user);
                final Document doc = Jsoup.connect("https://www.rolimons.com/player/" + user.getId()).get();
                final String tradeAdScript = doc.select("script:containsData(trade_ad_count)").first().toString();
                final int tradeAdCount = Integer.decode(tradeAdScript.substring(tradeAdScript.indexOf(':', tradeAdScript.indexOf("trade_ad_count")) + 1, tradeAdScript.indexOf(',', tradeAdScript.indexOf("\"trade_ad_count\":"))));

                if (tradeAdCount <= maxTradeAds) {
                    String payload = OfferLogic.createPayload(itemId, user, yourUserId);
                    System.out.println("Payload Created: " + payload);
                    payloads.add(payload);
                }
                Thread.sleep(7000); // Do not go under 5 seconds per player scrape.
            }
        }
        return payloads; // TODO: Sort playersWithAds
    }

    public List<String> getOwnersNoLimitPayloads(List<UserInfo> userInfo, long itemId, long yourUserId) {
        List<String> payloads = new ArrayList<>();

        for(UserInfo user : userInfo) {
            if(!checkBlacklist(user)) {
                payloads.add(OfferLogic.createPayload(itemId, user, yourUserId));
            }
        }
        return payloads;
    }

    public List<String> getOwnersNoLimitPayloadsWithLimit(List<UserInfo> userInfo, long itemId, long yourUserId, int maxListSize) {
        List<String> payloads = new ArrayList<>();

        if(maxListSize > userInfo.size()) {
            maxListSize = userInfo.size();
        }


        for(int i = 0; i < maxListSize; i++) {
            if(!checkBlacklist(userInfo.get(i))) {
                payloads.add(OfferLogic.createPayload(itemId, userInfo.get(i), yourUserId));
            }
        }
        return payloads;
    }

    public boolean checkBlacklist(UserInfo user) {
        boolean blacklisted = false;

        for(long blacklistedName : blacklist) {
            if (blacklistedName == user.getId()) {
                blacklisted = true;
                break;
            }
        }

        return blacklisted;
    }
}
