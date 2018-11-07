package com.example.android.bakingapp.Recipe.Ingredient;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private String quantity;
    private String measure;
    private String ingredient;

    public static final Creator CREATOR = new Creator() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public Ingredient(String quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public Ingredient(Parcel in) {
        this.quantity = in.readString();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public String getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.quantity);
        parcel.writeString(this.measure);
        parcel.writeString(this.ingredient);
    }
}
