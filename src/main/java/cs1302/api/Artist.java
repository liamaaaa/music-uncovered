package cs1302.api;

import java.util.List;

public class Artist {

    String name;
    String country;
    List<Tag> tags;
    List<ArtistPicture> artistPics;
    List<Album> albums;
    List<SimilarArtist> similarArtists;
    AlbumImage albumPic;

    static class Tag {
        String name;

        @Override
        public String toString() {
            return name;
        }
    }

    static class ArtistPicture {
        String url;
    }

    static class Album {
        String name;
    }

    static class SimilarArtist {
        String name;
    }
    static class AlbumImage {
        String url;
    }
}
