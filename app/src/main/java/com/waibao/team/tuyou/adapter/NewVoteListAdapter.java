package com.waibao.team.tuyou.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.waibao.team.tuyou.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Delete_exe on 2016/6/7.
 */
public class NewVoteListAdapter extends RecyclerView.Adapter<NewVoteListAdapter.ViewHolder> {
    private List<String> list;

    public NewVoteListAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public NewVoteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(parent.getContext(), R.layout.item_new_vote_list, null);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final NewVoteListAdapter.ViewHolder holder, final int position) {
        holder.etMenu.setText("");
        holder.etMenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list.set(holder.getAdapterPosition() - 1, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (position < 2) {
            holder.bt_add.setVisibility(View.GONE);
        } else {
            holder.bt_add.setVisibility(View.VISIBLE);
            holder.bt_add.setBackgroundResource(R.drawable.ic_remove_circle_green);
            holder.bt_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String s = "";
//                    for (int i = 0; i < list.size(); i++) {
//                        s += list.get(i) + ",";
//                    }
//                    Log.e("LLLLL", s);
//                    Log.e("LLLLL", "position: " + (holder.getAdapterPosition()));
                    list.remove(holder.getAdapterPosition() - 1);
                    notifyItemRemoved(holder.getAdapterPosition());
//                    s = "";
//                    for (int i = 0; i < list.size(); i++) {
//                        s += list.get(i) + ",";
//                    }
//                    Log.e("LLLLL", s);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.et_menu)
        EditText etMenu;
        @Bind(R.id.bt_add)
        View bt_add;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
