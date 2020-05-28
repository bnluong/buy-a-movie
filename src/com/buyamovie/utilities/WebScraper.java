package com.buyamovie.utilities;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebScraper {
	public WebScraper() { /* Empty */ }
	
	public String getIMDBPortrait(String imdbURL) {
		String portraitURL = "";
		
		try {
			Document document = Jsoup.connect(imdbURL).get();
			portraitURL = document.select("#name-poster").attr("src");
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

		return portraitURL;
	}
	
	public String getIMDBPoster(String imdbURL) {
		String posterURL = "";
		
		try {
			Document document = Jsoup.connect(imdbURL).get();
			posterURL = document.select("div.poster").select("img").attr("src");
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

		return posterURL;
	}
}