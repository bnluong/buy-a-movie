package com.buyamovie.utilities;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import okhttp3.OkHttpClient;

public class PosterScrapper {
	public PosterScrapper() { /* Empty */ }
	
	public String getIMDBPoster(String imdbURL) {
		String posterURL = "";
		OkHttpClient client = new OkHttpClient();
		okhttp3.Request request = new okhttp3.Request.Builder().url(imdbURL).get().build();
		
		try {
			Document document = Jsoup.parse(client.newCall(request).execute().body().string());
			posterURL = document.select("div.poster").select("img").attr("src");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

		return posterURL;
	}
}
