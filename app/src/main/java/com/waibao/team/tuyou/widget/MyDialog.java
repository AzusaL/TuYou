package com.waibao.team.tuyou.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.waibao.team.tuyou.R;

/**
 * Created by Azusa on 2016/5/3.
 */
public class MyDialog {

    public AlertDialog showLodingDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setView(View.inflate(context, R.layout.progressbar_layout, null))
                .show();
        dialog.setCancelable(false);
        return dialog;
    }

    public AlertDialog showConversationMore(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setView(View.inflate(context, R.layout.dialog_conversation_more, null))
                .show();
        return dialog;
    }


    public AlertDialog showItemSelect(Context context, final TextView textView, final String[] array) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_selectitem, null);
        final AlertDialog dialog = builder.setView(view).show();

        ListView listView = (ListView) view.findViewById(R.id.lv);
        listView.setAdapter(new ArrayAdapter(context, R.layout.item_simpleselectitem, R.id.tv_item1, array));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(array[position]);
                dialog.cancel();
            }
        });
        return dialog;
    }

}
