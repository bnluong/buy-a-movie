package com.buyamovie.utilities;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

public class PosterScrapper {
	public PosterScrapper() { /* Empty */ }
	
	public String getIMDBPoster(String imdbURL) {
		String posterURL = "";

		try {
			Document document = Jsoup.connect(imdbURL).timeout(6000).get();
			posterURL = document.select("div.poster").select("img").attr("src");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

		return posterURL;
	}
}
