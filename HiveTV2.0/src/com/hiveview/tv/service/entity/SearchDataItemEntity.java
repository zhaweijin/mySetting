package com.hiveview.tv.service.entity;

import com.hiveview.tv.grideview.library.model.AsymmetricItem;

import android.os.Parcel;
import android.os.Parcelable;



public class SearchDataItemEntity extends ThirdListEntity implements AsymmetricItem {
    /**
	 * @Fields serialVersionUID:TODO
	 */
	private static final long serialVersionUID = -4066237423828799079L;
	private int columnSpan;
    private int rowSpan;
    private int position;

    public SearchDataItemEntity() {
        this(1, 1, 0);
    }

    public SearchDataItemEntity(final int columnSpan, final int rowSpan, int position) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
    }

    public SearchDataItemEntity(final Parcel in) {
        readFromParcel(in);
    }
    public void setColumnSpan( int columnSpan){
    	this.columnSpan = columnSpan;
    }
    public void setRowSpan( int rowSpan){
    	this.rowSpan = rowSpan;
    }
    public void setPosition(int position){
    	this.position = position;
    }
    public int getColumnSpan() {
        return columnSpan;
    }

    
    public int getRowSpan() {
        return rowSpan;
    }

    public int getPosition() {
        return position;
    }

    
    public String toString() {
        return String.format("%s: %sx%s", position, rowSpan, columnSpan);
    }

    
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(final Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();
        position = in.readInt();
    }

    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(columnSpan);
        dest.writeInt(rowSpan);
        dest.writeInt(position);
    }

    /* Parcelable interface implementation */
    public static final Parcelable.Creator<SearchDataItemEntity> CREATOR = new Parcelable.Creator<SearchDataItemEntity>() {

        
        public SearchDataItemEntity createFromParcel(final Parcel in) {
            return new SearchDataItemEntity(in);
        }

        
        public SearchDataItemEntity[] newArray(final int size) {
            return new SearchDataItemEntity[size];
        }
    };
}
