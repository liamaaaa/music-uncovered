package cs1302.api;

import java.util.List;

public class Artist {

    String name;
    String country;
    List<Tag> tags;
    List<ArtistPicture> artistPics;

    static class Tag {
        String name;

        @Override
        public String toString() {
            return name;
        }
    }

    class ArtistPicture {
        String url;
    }

}
