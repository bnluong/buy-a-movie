package com.buyamovie.utilities.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.buyamovie.utilities.WebScraper;

class PortraitScraperTest {

	@Test
	void testCareyMulliganPortrait() {
		WebScraper portraitScraper = new WebScraper();;
		String portraitURL = portraitScraper.getIMDBPortrait("https://www.imdb.com/name/nm1659547");
		assertEquals("https://m.media-amazon.com/images/M/MV5BMTUzODM0OTY4OF5BMl5BanBnXkFtZTgwMTg3NDk0NzE@._V1_UX214_CR0,0,214,317_AL_.jpg", portraitURL);
	}
}