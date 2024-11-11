import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        long[] blacklist = {
                5279701385L // Fuck this person in particular. No trades for them. >:(
        };

        List<String> allOwnerPayloads = new ArrayList<>();

        final long[] ID_ARRAY = {169444294L}; // Array of items which I want
        AmountOfTradeAds amountOfTradeAds = new AmountOfTradeAds(0, blacklist);
        final long myUserId = 1341100454L;

        // Make the file name pretty. Also so I don't have to decipher which unix timestamp is earlier when trying to find the newest file.
        final String[] FILE_NAME_DECORATION = {
                "Dog", "Cat", "Elephant", "Tiger", "Lion", "Giraffe", "Monkey", "Bear",
                "Wolf", "Fox", "Rabbit", "Deer", "Kangaroo", "Zebra", "Hippopotamus",
                "Rhinoceros", "Crocodile", "Alligator", "Penguin", "Dolphin",
                "Leopard", "Cheetah", "Panda", "Koala", "Otter", "Beaver", "Badger",
                "Hedgehog", "Porcupine", "Skunk", "Squirrel", "Raccoon", "Opossum",
                "Armadillo", "Anteater", "Bat", "Bison", "Buffalo", "Camel", "Donkey",
                "Goat", "Sheep", "Horse", "Cow", "Pig", "Chicken", "Duck", "Goose",
                "Turkey", "Ostrich", "Flamingo", "Peacock", "Parrot", "Sparrow",
                "Hawk", "Eagle", "Falcon", "Owl", "Pigeon", "Seagull", "Swan",
                "Turtle", "Tortoise", "Lizard", "Snake", "Frog", "Toad", "Salamander",
                "Newt", "Fish", "Shark", "Whale", "Octopus", "Squid", "Crab", "Lobster",
                "Shrimp", "Jellyfish", "Starfish", "Sea Urchin", "Coral", "Clam",
                "Snail", "Slug", "Worm", "Ant", "Bee", "Butterfly", "Moth", "Beetle",
                "Dragonfly", "Grasshopper", "Cricket", "Ladybug", "Spider", "Scorpion",
                "Tick", "Flea"
        };
        final long CURRENT_TIME = System.currentTimeMillis();

        // Remove offers which are the same (So a hoarder doesn't end up with 20 trades from me and I don't end up wasting 20 trades on one person.)
        OfferLogic.multipleSameAddOffer(ID_ARRAY,  new Bodies.TradeFormat.SideFormat(myUserId, arrayToList(new long[]{210822232L, 11564885L, 1284890865L, 23725962L}), 0));

        for(int i = 0; i < ID_ARRAY.length; i++) {
            FindOwners findOwners = new FindOwners(ID_ARRAY[i], 360, 2);
            //System.out.println(ID_ARRAY[i] + " payload count " + findOwners.getUserInfo().size());

            List<String> ownerPayloads = amountOfTradeAds.getOwnersNoLimitPayloads(findOwners.getUserInfo(), ID_ARRAY[i], myUserId);

            System.out.println(ID_ARRAY[i] + " payload count " + ownerPayloads.size());
            allOwnerPayloads.addAll(ownerPayloads);

            if(i != ID_ARRAY.length - 1) {
                Thread.sleep(10000);
            }
        }

        int randomIndex = (int)(Math.random() * FILE_NAME_DECORATION.length);
        try (FileWriter fw = new FileWriter(FILE_NAME_DECORATION[randomIndex] + "_" + CURRENT_TIME + "_SIZE=" + allOwnerPayloads.size() + "_" + ".txt")) {
            fw.write("All Owner Payloads: " + allOwnerPayloads);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Long> arrayToList(long[] array) {
        ArrayList<Long> list = new ArrayList<>();

        for(long element : array) {
            list.add(element);
        }

        return list;
    }
}
