package ca.ualberta.app.models;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Question extends InputsModel {
	public Question(String content, String authorLoginName, String title,
			ArrayList<Bitmap> image) {
		super(content, authorLoginName, title, image);
	}
}
