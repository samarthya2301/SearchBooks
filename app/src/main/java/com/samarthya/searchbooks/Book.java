package com.samarthya.searchbooks;

public class Book {

	String bookTitle, bookAuthor, imageUrl, listPrice, buyLink;

	Book(String bookTitle,
		 String bookAuthor,
		 String imageUrl,
		 String listPrice,
		 String buyLink) {

		this.bookTitle = bookTitle;
		this.bookAuthor = bookAuthor;
		this.imageUrl = imageUrl;
		this.listPrice = listPrice;
		this.buyLink = buyLink;

	}

}
