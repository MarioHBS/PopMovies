package br.com.mario.popmovies.data;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/** Created by MarioH on 01/11/2016. */
public class Movies implements Parcelable {
	public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
		public Movies createFromParcel(Parcel in) {
			return (new Movies(in));
		}

		public Movies[] newArray(int size) {
			return (new Movies[size]);
		}
	};

	private final int id;
	private final double average;
	private final String title;
	private final String posterUrl;
	private final String backdropUrl;
	private final String releaseDate;
	private final String synopsis;

	private int pos;

	private Bitmap poster;

	public Movies(int id, String title, String posterUrl, String backdropUrl, String releaseDate,
	              String synopsis, double average) {
		this.id = id;
		this.title = title;
		this.posterUrl = posterUrl;
		this.backdropUrl = backdropUrl;
		this.releaseDate = releaseDate;
		this.synopsis = synopsis;
		this.average = average;
	}

	private Movies(Parcel in) {
		id = in.readInt();
		title = in.readString();
		posterUrl = in.readString();
		backdropUrl = in.readString();
		releaseDate = in.readString();
		synopsis = in.readString();
		average = in.readDouble();
	}

	@BindingAdapter("imageLoad")
	public static void setImagePoster(ImageView view, Bitmap img) {
		view.setImageBitmap(img);
	}

	public Bitmap getPoster() {
		return (poster); //new BitmapDrawable(ctx.getResources(), poster)
	}

	public void setPoster(Bitmap poster) {
		this.poster = poster;
	}

	public String getBackdropUrl() {
		return (backdropUrl);
	}

	public double getAverage() {
		return average;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public String getTitle() {
		return title;
	}

	public int getId() {
		return (id);
	}

	@Override
	public int describeContents() {
		return (0);
	}

	/**
	 * Flatten this object in to a Parcel.
	 *
	 * @param dest  The Parcel in which the object should be written.
	 * @param flags Additional flags about how the object should be written.
	 *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(posterUrl);
		dest.writeString(backdropUrl);
		dest.writeString(releaseDate);
		dest.writeString(synopsis);
		dest.writeDouble(average);
	}

	public int getPos() {
		return (pos);
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
}