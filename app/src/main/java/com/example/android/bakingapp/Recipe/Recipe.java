package com.example.android.bakingapp.Recipe;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.bakingapp.Recipe.Ingredient.Ingredient;
import com.example.android.bakingapp.Recipe.Step.Step;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    private String id;
    private String name;
    private String servings;
    private String imageURL;
    private List<Ingredient> ingredients;
    private List<Step> steps;

    public static final Creator CREATOR = new Creator() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe(String id, String name, String servings, String imageURL, List<Ingredient> ingredients, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.imageURL = imageURL;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Recipe(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.servings = in.readString();
        this.imageURL = in.readString();
        if (in.readByte() == 0x01) {
            this.ingredients = new ArrayList<>();
            in.readList(this.ingredients, Ingredient.class.getClassLoader());
        } else {
            this.ingredients = null;
        }
        if (in.readByte() == 0x01) {
            this.steps = new ArrayList<>();
            in.readList(this.steps, Ingredient.class.getClassLoader());
        } else {
            this.steps = null;
        }
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getServings() {
        return this.servings;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public List<Step> getSteps() {
        return this.steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.servings);
        parcel.writeString(this.imageURL);
        if (this.ingredients == null){
            parcel.writeByte((byte) (0x00));
        } else {
            parcel.writeByte((byte) (0x01));
            parcel.writeList(this.ingredients);
        }
        if(this.steps == null){
            parcel.writeByte((byte) (0x00));
        } else {
            parcel.writeByte((byte) (0x01));
            parcel.writeList(this.steps);
        }
    }
}
