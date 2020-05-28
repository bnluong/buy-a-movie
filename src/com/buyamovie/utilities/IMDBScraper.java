package com.buyamovie.utilities;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IMDBScraper {
	private Document document;

	public IMDBScraper() { /* Empty constructor */	}
	public IMDBScraper(String imdbURL) { 
		try {
			document = Jsoup.connect(imdbURL).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getStarPortrait() {
		String portraitURL = "";
		
		portraitURL = document.select("#name-poster").attr("src");

		return portraitURL;
	}
	public String getStarProfession() {
		String profession = "";
		
		profession = document.select("#name-job-categories .itemprop").eachText().toString().replaceAll("[\\[\\](){}]", "");
		
		return profession;
	}

	public String getStarBio() {
		String bio = "";
		
		bio = document.select(".name-trivia-bio-text .inline").text();

		return bio;
	}

	public String getMoviePoster() {
		String posterURL = "";
		
		posterURL = document.select("div.poster").select("img").attr("src");

		return posterURL;
	}
	public String getMoviePoster(String imdbURL) {
		String posterURL = "";
		
		try {
			document = Jsoup.connect(imdbURL).get();
			posterURL = document.select("div.poster").select("img").attr("src");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return posterURL;
	}
	public String getMovieRuntime() {
		String runtime = "";

		runtime = document.select(".subtext time").text();

		return runtime;
	}

	public String getMovieOverview() {
		String overview = "";

		overview = document.select(".summary_text").text();

		return overview;
	}


}