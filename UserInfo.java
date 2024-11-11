public class UserInfo {

    private long id;
    private long lastUpdated;
    private long uaid;
    public UserInfo(long id, long lastUpdated, long uaid ) {
        this.id = id;
        this.lastUpdated = lastUpdated;
        this.uaid = uaid;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public long getLastUpdated() { return lastUpdated; }

    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }

    public long getUaid() { return uaid; }

    public void setUaid(long uaid) { this.uaid = uaid; }

    public String toString() { return "[" + getId() + ", " + getLastUpdated() + ", " + getUaid() + "]"; }
}
