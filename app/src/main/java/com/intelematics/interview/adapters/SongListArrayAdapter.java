package com.intelematics.interview.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.intelematics.interview.R;
import com.intelematics.interview.SongListActivity;
import com.intelematics.interview.db.DBManager;
import com.intelematics.interview.models.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * I am going to use a Recycler View for improvement of loading data content and quicker
 */
public class SongListArrayAdapter extends RecyclerView.Adapter<SongListArrayAdapter.ViewHolder> implements Filterable {
	private static final String TAG = "TAG";
	private DBManager dbManager;
	private final SongListActivity activity;
	private ArrayList<Song> filteredSongsList;
	private ArrayList<Song> songsList;

	public SongListArrayAdapter(SongListActivity activity, ArrayList<Song> songs, DBManager dbManager) {
//		super(activity, R.layout.song_list_row, songs);
		this.activity = activity;
		this.songsList = songs;
		this.filteredSongsList = songs;
		this.dbManager = dbManager;
	}

	@Override
	public SongListArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View convertView = inflater.inflate(R.layout.song_list_row, parent, false);
		return new ViewHolder(convertView);
	}

	@Override
	public void onBindViewHolder(SongListArrayAdapter.ViewHolder holder, int position) {

		if(position < filteredSongsList.size()) {
			final Song song = filteredSongsList.get(position);
			holder.songName.setText(song.getTitle());
			holder.songArtist.setText(song.getArtist());
			holder.songPrice.setText("$" + String.valueOf(song.getPrice()));
			/**
			 * 3.2 The application displays data from API but the images are loaded as bitmaps (fix this issue)
			 * I would use Picasso to load the images and better management for loading images
			 */
			Picasso.with(activity.getBaseContext())
					.load(song.getCoverURL())
					.into(holder.albumCover);
			Log.i(TAG, "pos: " + position + " size: " + filteredSongsList.size());
		}


	}

	/**
	 * Old code from ListView
	 * @return
     */
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//			LayoutInflater inflater = activity.getLayoutInflater();
//			convertView = inflater.inflate(R.layout.song_list_row, parent, false);
//
//            ImageView albumCover = (ImageView)convertView.findViewById(R.id.album_cover);
//          TextView songName = (TextView)convertView.findViewById(R.id.song_title);
//            TextView songArtist = (TextView)convertView.findViewById(R.id.song_artist);
//            TextView songPrice = (TextView)convertView.findViewById(R.id.song_price);
//        ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progress_bar);
//
//
//		final Song song = filteredSongsList.get(position);
//
//		songName.setText(song.getTitle());
//		songArtist.setText(song.getArtist());
//		songPrice.setText("$" + String.valueOf(song.getPrice()));
//		Picasso.with(activity.getBaseContext())
//				.load(song.getCoverURL())
//				.into(albumCover);
//
////		if(song.getCover() != null) {
////			getCover(song);
////	     	albumCover.setImageBitmap(song.getCover());
//
////		} else {
////			 albumCover.setImageResource(R.drawable.img_cover);
////			 progressBar.setVisibility(View.VISIBLE);
////			 getCover(song);
////		}
////
//		return convertView;
//	}

	@Override
	public int getItemCount() {
		return songsList.size();
	}


	public class ViewHolder extends RecyclerView.ViewHolder {

		ImageView albumCover;
		TextView songName;
		TextView songArtist;
		TextView songPrice;
		ProgressBar progressBar;


		public ViewHolder(View convertView) {
			super(convertView);

			albumCover = (ImageView) convertView.findViewById(R.id.album_cover);
			songName = (TextView) convertView.findViewById(R.id.song_title);
			songArtist = (TextView) convertView.findViewById(R.id.song_artist);
			songPrice = (TextView) convertView.findViewById(R.id.song_price);
			progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);

		}
	}

	//Extra methods

	public void clear() {
		songsList.clear();
	}

	public void updateList(ArrayList<Song> songs) {
		this.songsList = songs;
		this.filteredSongsList.clear();
		this.filteredSongsList.addAll(songs);
		this.notifyDataSetChanged();
	}

	public void updateList(ArrayList<Song> songs, Editable sequence) {
		this.songsList = songs;
		this.filteredSongsList.clear();
		this.filteredSongsList.addAll(songs);
		this.getFilter().filter(sequence);
	}

	//Filter methods

	@Override
	public Filter getFilter() {

		Filter filter = new Filter() {

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				ArrayList<Song> check = (ArrayList<Song>) results.values;
				if (check.size() > 0) {
					filteredSongsList = check;
				}
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				ArrayList<Song> filteredSongs = new ArrayList<Song>();

				constraint = constraint.toString().toLowerCase();
				for (int i = 0; i < songsList.size(); i++) {
					Song song = songsList.get(i);
					if (song.getArtist().toLowerCase().contains(constraint.toString()) ||
							song.getTitle().toLowerCase().contains(constraint.toString())) {
						filteredSongs.add(song);
					}
				}

				results.count = filteredSongs.size();
				results.values = filteredSongs;

				return results;
			}
		};

		return filter;
	}


//    private void getCover(Song song){
//        if(song.getCover() == null){
//            ConnectionManager connectionManager = new ConnectionManager(activity, song.getCoverURL());
//            byte[] imageByteArray = connectionManager.requestImage().buffer();
//            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
//            Bitmap cover = BitmapFactory.decodeStream(imageStream);
//            song.setCover(cover);
//
//            SongManager songManager = new SongManager(activity, dbManager);
//            songManager.saveCover(song, imageByteArray);
//        }
//    }

}
