package cs1302.api;

import java.util.List;

/**
 * Stores json response for top albums from Last.fm API.
 */
public class TopAlbumsResponse {
    TopAlbums topalbums;

    /**
     * Stores list of albums from Last.fm API.
     */
    static class TopAlbums {
        List<Album> album;
    }

    /**
     * Stores artist object and name of album.
     */
    static class Album {
        Artist artist;
        String name;
    }

}
