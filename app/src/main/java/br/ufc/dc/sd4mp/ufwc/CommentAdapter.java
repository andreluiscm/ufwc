package br.ufc.dc.sd4mp.ufwc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by André on 20/06/2015.
 */
public class CommentAdapter extends BaseAdapter {

    List<Comment> alComments;
    LayoutInflater inflater;

    public CommentAdapter(List<Comment> alComments, Context context) {

        this.alComments = alComments;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void addItem(Comment comment) {

        alComments.add(comment);

        notifyDataSetChanged();

    }

    @Override
    public int getCount() {

        return alComments.size();

    }

    @Override
    public Object getItem(int position) {

        return alComments.get(position);

    }

    @Override
    public long getItemId(int position) {

        return position;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String commentVO = alComments.get(position).getText();
        String commentPositionVO = Integer.toString(position + 1);

        convertView = inflater.inflate(R.layout.comment_row, null);

        TextView tvCommentPosition = (TextView) convertView.findViewById(R.id.textView55);
        TextView tvComment = (TextView) convertView.findViewById(R.id.textView56);

        tvComment.setText(commentVO);
        tvCommentPosition.setText("#" + commentPositionVO + ":");

        return convertView;

    }
}
