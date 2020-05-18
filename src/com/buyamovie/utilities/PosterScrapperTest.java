package com.buyamovie.utilities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PosterScrapperTest {
	@Test
	void testEmptyPoster() {
		PosterScrapper posterScraper = new PosterScrapper();;
		String posterURL = posterScraper.getIMDBPoster("https://www.imdb.com/title/tt0287632");
		assertEquals("", posterURL);
	}
}