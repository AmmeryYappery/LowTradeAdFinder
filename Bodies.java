import java.util.ArrayList;

public class Bodies {
    public static class TradeFormat {
        private final ArrayList<SideFormat> offers = new ArrayList<>();

        public TradeFormat(SideFormat offer, SideFormat request) {
            offers.add(offer);
            offers.add(request);
        }

        public static class SideFormat {
            private long userId;
            private ArrayList<Long> userAssetIds;
            private int robux;

            public SideFormat(long userId, ArrayList<Long> userAssetIds, int robux) {
                this.userId = userId;
                this.userAssetIds = userAssetIds;
                this.robux = robux;
            }

            public ArrayList<Long> getUserAssetIds() {
                return userAssetIds;
            }
        }
    }
}
