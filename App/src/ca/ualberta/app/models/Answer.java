package ca.ualberta.app.models;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Answer extends InputsModel {
	public Answer(String content, String authorLoginName,
			ArrayList<Bitmap> image) {
		super(content, authorLoginName, image);
	}
}
