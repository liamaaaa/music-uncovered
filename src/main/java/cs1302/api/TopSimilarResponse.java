package cs1302.api;

import java.util.List;

/**
 * Stores top similar artists to artist from MusicBrainz API,
 *     using Last.fm API.
 */
public class TopSimilarResponse {
    SimilarArtists similarartists;


   /**
    * Stores list of artists.
    */
    static class SimilarArtists {
        List<Artist> artist;
    }
}
