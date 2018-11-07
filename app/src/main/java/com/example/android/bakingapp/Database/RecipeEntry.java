package com.example.android.bakingapp.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "recipe")
public class RecipeEntry implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int key;
    private String id;
    private String quantity;
    private String measure;
    private String ingredient;

    public RecipeEntry(int key, String id, String quantity, String measure, String ingredient){
        this.key = key;
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    @Ignore
    public RecipeEntry(String id, String quantity, String measure, String ingredient){
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public int getKey() {
        return key;
    }

    public String getId() {
        return id;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }


    public static final Creator CREATOR = new Creator() {
        public RecipeEntry createFromParcel(Parcel in) {
            return new RecipeEntry(in);
        }

        public RecipeEntry[] newArray(int size) {
            return new RecipeEntry[size];
        }
    };

    public RecipeEntry(Parcel in){
        this.id = in.readString();
        this.quantity = in.readString();
        this.measure = in.readString();
        this.ingredient =  in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.quantity);
        parcel.writeString(this.measure);
        parcel.writeString(this.ingredient);
    }
}
