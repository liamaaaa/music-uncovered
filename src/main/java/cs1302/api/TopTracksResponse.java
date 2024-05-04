package cs1302.api;

import java.util.List;

/**
 * Stores JSON response for top tracks.
 */
public class TopTracksResponse {
    TopTracks toptracks;


    /**
     * Stores list of tracks.
     */
    static class TopTracks {
        List<Track> track;
    }

    /**
     * Stores artist and name of track.
     */
    static class Track {
        Artist artist;
        String name;
    }
}
