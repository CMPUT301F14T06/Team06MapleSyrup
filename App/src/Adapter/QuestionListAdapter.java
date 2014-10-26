package Adapter;

import java.util.ArrayList;

import models.Question;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QuestionListAdapter extends ArrayAdapter<Question> {

	public QuestionListAdapter(Context context, int textViewResourceId, ArrayList<Question> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

}
