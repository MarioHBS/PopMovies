package br.com.mario.popmovies.model;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
	@SerializedName("poster_path")
	@Expose
	private final String posterUrl;
	private final String backdropUrl;
	private final String releaseDate;
	private final String synopsis;

	private int pos;

	private Bitmap poster;

	private boolean isFavourite;

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

	@BindingAdapter("typefaceLoad")
	public static void setTypeface(TextView tv, String config){
		// compatibilidade para versões 15 ou menores
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
			tv.setTypeface(Typeface.create(config, Typeface.BOLD));
	}

	@BindingAdapter(value={"imageLoad", "placeholder"}, requireAll = false)
	public static void setImagePoster(ImageView view, Bitmap img, Drawable placeholder) {
		if (img == null)
			view.setImageDrawable(placeholder);
		else
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

	public void setFavourite(boolean favourite) {
		this.isFavourite = favourite;
	}
}