package com.samarthya.searchbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

	private EditText etSearchBook;
	private RecyclerView recyclerView;
	private TextView tvEmptyState;
	private ImageView ivEmptyState;
	private ArrayList<Book> books;
	public ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		etSearchBook = findViewById(R.id.etSearchBook);
		Spinner bookLimit = findViewById(R.id.bookLimit);
		ImageView ivSearch = findViewById(R.id.ivSearch);
		recyclerView = findViewById(R.id.recyclerView);
		tvEmptyState = findViewById(R.id.tvEmptyState);
		ivEmptyState = findViewById(R.id.ivEmptyState);
		progressDialog = new ProgressDialog(this);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
				(this, R.array.bookLimits, android.R.layout.simple_list_item_1);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bookLimit.setAdapter(adapter);

		final int[] limit = {15};

		bookLimit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

				String limitRange = adapterView.getItemAtPosition(i).toString();

				switch (limitRange) {

					case "0–10": limit[0] = 10; break;
					case "10–20": limit[0] = 20; break;
					case "20–30": limit[0] = 30; break;
					case "30–40": limit[0] = 40; break;

				}

			}

			@Override public void onNothingSelected(AdapterView<?> adapterView) {}

		});

		ivSearch.setOnClickListener(view -> {

			if (etSearchBook.getText().toString().isEmpty()) {
				Toast.makeText(this, "No Book Searched!", Toast.LENGTH_SHORT).show();
			} else {

				progressDialog.show();
				progressDialog.setContentView(R.layout.buffering);
				String bookSearched = etSearchBook.getText().toString().trim().toLowerCase();
				fetchAndUpdate(bookSearched, limit[0]);

			}

		});

	}

	private void fetchAndUpdate(String bookSearched, int limit) {

		String url =
			"https://www.googleapis.com/books/v1/volumes?q=" + bookSearched + "&maxResults=" + limit;
		Utils utils = new Utils(url);

		ExecutorService exec = Executors.newSingleThreadExecutor();
		Handler handler = new Handler(Looper.getMainLooper());

		exec.execute(() -> {

			books = utils.init();
			handler.post(this::updateUi);

		});

	}

	private void updateUi() {

		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		if (books == null) {

			Toast.makeText(this, "No Books Found!", Toast.LENGTH_SHORT).show();
			progressDialog.hide();
			return;

		}

		BooksListAdapter adapter = new BooksListAdapter(books, this);
		recyclerView.setAdapter(adapter);

		tvEmptyState.setVisibility(View.GONE);
		ivEmptyState.setVisibility(View.GONE);
		recyclerView.setVisibility(View.VISIBLE);

		progressDialog.hide();

	}

}