package commm.oneee.android.globalnews;

public class mDATA {
    String title, description, image, date, mURL;

    public mDATA(String title, String description, String image, String date, String mURL) {
        this.description = description;
        this.title = title;
        this.image = image;
        this.date = date;
        this.mURL = mURL;
    }

    public String getImage() {
        return image;
    }

    public String getmURL() {
        return mURL;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}
