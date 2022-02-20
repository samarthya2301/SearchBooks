package com.samarthya.searchbooks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {

	private final String FETCH_URL;
	JSONObject volumeInfo;
	JSONObject saleInfo;

	Utils(String FETCH_URL) {
		this.FETCH_URL = FETCH_URL;
	}

	public ArrayList<Book> init() {

		ArrayList<Book> books = null;
		String jsonResponse;

		try {
			jsonResponse = fetchResponse();
			books = parseJson(jsonResponse);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return books;

	}

	private String fetchResponse() throws IOException {

		URL url = new URL(FETCH_URL);
		HttpURLConnection httpURLConnection;
		InputStream inputStream;
		Scanner inputStreamReader;
		StringBuilder jsonResponse = new StringBuilder();

		httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setConnectTimeout(10000);
		httpURLConnection.setReadTimeout(10000);
		httpURLConnection.connect();

		inputStream = httpURLConnection.getInputStream();
		inputStreamReader = new Scanner(inputStream);

		while (inputStreamReader.hasNext()) {
			jsonResponse.append(inputStreamReader.nextLine());
		}

		inputStream.close();
		inputStreamReader.close();
		httpURLConnection.disconnect();

		return jsonResponse.toString();

	}

	private ArrayList<Book> parseJson(String json) throws JSONException {

		ArrayList<Book> books = new ArrayList<>();

		JSONObject root = new JSONObject(json);
		JSONArray items = root.getJSONArray("items");

		for (int i = 0; i < items.length(); i++) {

			String bookTitle, imageUrl, listPrice, buyLink;
			StringBuilder bookAuthor = new StringBuilder();

			JSONObject newBook = items.getJSONObject(i);
			volumeInfo = newBook.getJSONObject("volumeInfo");
			saleInfo = newBook.getJSONObject("saleInfo");

			try {
				bookTitle = volumeInfo.getString("title");
			} catch (JSONException e) {
				bookTitle = "";
			}

			try {
				JSONArray authors = volumeInfo.getJSONArray("authors");
				for (int j = 0; j < authors.length(); j++) {
					bookAuthor.append(authors.get(j));
					if (j != authors.length() - 1) {
						bookAuthor.append(", ");
					}
				}
			} catch (JSONException e) {
				bookAuthor.append("");
			}

			try {
				JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
				imageUrl = imageLinks.getString("thumbnail");
			} catch (JSONException e) {
				imageUrl = "";
			}

			try {
				JSONObject lp = saleInfo.getJSONObject("listPrice");
				listPrice = lp.getString("amount");
			} catch (JSONException e) {
				listPrice = "N.A";
			}

			try {
				buyLink = saleInfo.getString("buyLink");
			} catch (JSONException e) {
				buyLink = "";
			}

			books.add(new Book(
					bookTitle,
					bookAuthor.toString(),
					imageUrl,
					listPrice,
					buyLink));

		}

		return books;

	}

}
