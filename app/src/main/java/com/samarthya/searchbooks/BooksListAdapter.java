package com.samarthya.searchbooks;


import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.ViewHolder> {

	private static ArrayList<Book> localDataSet;
	private final MainActivity mainActivity;

	public static class ViewHolder extends RecyclerView.ViewHolder {

		private final TextView bookTitle;
		private final TextView bookAuthor;
		private final ImageView bookImage;
		private final TextView bookPrice;

		public ViewHolder(View view) {

			super(view);

			view.setOnClickListener(v -> {

				Intent openWebPage = new Intent(Intent.ACTION_VIEW);
				String buyLink = localDataSet.get(getAdapterPosition()).buyLink;
				Uri urlToOpen = Uri.parse(buyLink);
				openWebPage.setData(urlToOpen);

				if (buyLink.isEmpty()) {
					Toast.makeText(view.getContext(), "Book Not Avsilable!",
							Toast.LENGTH_SHORT).show();
				} else {
					view.getContext().startActivity(openWebPage);
				}

			});

			bookTitle = view.findViewById(R.id.bookTitle);
			bookAuthor = view.findViewById(R.id.bookAuthor);
			bookImage = view.findViewById(R.id.bookImage);
			bookPrice = view.findViewById(R.id.bookPrice);

		}

		public TextView getBookTitle() {
			return bookTitle;
		}

		public TextView getBookAuthor() {
			return bookAuthor;
		}

		public ImageView getBookImage() {
			return bookImage;
		}

		public TextView getBookPrice() {
			return bookPrice;
		}

	}

	public BooksListAdapter(ArrayList<Book> dataSet, MainActivity mainActivity) {

		localDataSet = dataSet;
		this.mainActivity = mainActivity;

	}

	@NonNull @Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

		View view = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.list_item_layout, viewGroup, false);

		return new ViewHolder(view);

	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

		// altering the title
		String bookTitleDisplay = localDataSet.get(position).bookTitle;
		if (bookTitleDisplay.length() >= 20) {
			bookTitleDisplay = bookTitleDisplay.substring(0, 20) + "...";
		}
		viewHolder.getBookTitle().setText(bookTitleDisplay);

		// altering the author
		String bookAuthorDisplay = localDataSet.get(position).bookAuthor;
		if (bookAuthorDisplay.length() >= 25) {
			bookAuthorDisplay = bookAuthorDisplay.substring(0, 25) + "...";
		}
		viewHolder.getBookAuthor().setText(bookAuthorDisplay);

		// using glide to set images from a url
		ExecutorService exec = Executors.newSingleThreadExecutor();
		Handler handler = new Handler(Looper.getMainLooper());
		exec.execute(() -> handler.post(() -> Glide.with(mainActivity)
				.load(localDataSet.get(position).imageUrl)
				.error(R.drawable.warningimage)
				.placeholder(R.drawable.waiting)
				.into(viewHolder.getBookImage())));

		// set the price of the book
		String price =
				mainActivity.getString(R.string.rupee_symbol) + localDataSet.get(position).listPrice;
		viewHolder.getBookPrice().setText(price);

	}

	@Override
	public int getItemCount() {
		return localDataSet.size();
	}

}
