package com.buyamovie.utilities.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.buyamovie.utilities.IMDBScraper;

class IMDBSCraperTests {

	@Test
	void testGetStarPortrait() {
		IMDBScraper portraitScraper = new IMDBScraper("https://www.imdb.com/name/nm1659547");;
		String portraitURL = portraitScraper.getStarPortrait();
		assertEquals("https://m.media-amazon.com/images/M/MV5BMTUzODM0OTY4OF5BMl5BanBnXkFtZTgwMTg3NDk0NzE@._V1_UX214_CR0,0,214,317_AL_.jpg", portraitURL);
	}

	@Test
	void testGetStarProfession() {
		IMDBScraper imdbScraper = new IMDBScraper("https://www.imdb.com/name/nm0001618");
		String professions = imdbScraper.getStarProfession();
		assertEquals("Actor, Producer, Soundtrack", professions);
	}

	@Test
	void testGetStarBio() {
		IMDBScraper imdbScraper = new IMDBScraper("https://www.imdb.com/name/nm0001618");
		String bio = imdbScraper.getStarBio();
		assertEquals("Joaquin Phoenix was born Joaquin Rafael Bottom in San Juan, Puerto Rico, to Arlyn (Dunetz) and John Bottom, and is the middle child in a brood of five. His parents, from the continental United States, were then serving as Children of God missionaries. His mother is from a Jewish family from New York, while his father, from California, is of mostly... See full bio »", bio);
	}

	@Test
	void testGetMoviePoster() {
		IMDBScraper imdbScraper = new IMDBScraper("https://www.imdb.com/title/tt1334260");
		String posterURL = imdbScraper.getMoviePoster();
		assertEquals("https://m.media-amazon.com/images/M/MV5BMTM3NDQ1MjE2OF5BMl5BanBnXkFtZTcwNDIxNTk2Mw@@._V1_UX182_CR0,0,182,268_AL_.jpg", posterURL);
	}
	
	@Test
	void testGetMovieRuntime() {
		IMDBScraper imdbScraper = new IMDBScraper("https://www.imdb.com/title/tt7286456");
		String runtime = imdbScraper.getMovieRuntime();
		assertEquals("2h 2min", runtime);
	}

	@Test
	void testGetMovieOverview() {
		IMDBScraper imdbScraper = new IMDBScraper("https://www.imdb.com/title/tt7286456");
		String overview = imdbScraper.getMovieOverview();
		assertEquals("In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.", overview);
	}
}