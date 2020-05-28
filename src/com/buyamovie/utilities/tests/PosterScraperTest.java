package com.buyamovie.utilities.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.buyamovie.utilities.WebScraper;

class PosterScraperTest {
	
	@Test
	void testNeverLetMeGoPoster() {
		WebScraper posterScraper = new WebScraper();;
		String posterURL = posterScraper.getIMDBPoster("https://www.imdb.com/title/tt1334260");
		assertEquals("https://m.media-amazon.com/images/M/MV5BMTM3NDQ1MjE2OF5BMl5BanBnXkFtZTcwNDIxNTk2Mw@@._V1_UX182_CR0,0,182,268_AL_.jpg", posterURL);
	}
	@Test
	void testEmptyPoster() {
		WebScraper posterScraper = new WebScraper();;
		String posterURL = posterScraper.getIMDBPoster("https://www.imdb.com/title/tt0287632");
		assertEquals("", posterURL);
	}
}