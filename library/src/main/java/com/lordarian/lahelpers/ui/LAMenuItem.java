package com.lordarian.lahelpers.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lordarian.lahelpers.R;
import com.lordarian.lahelpers.UnitHelper;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class LAMenuItem extends FrameLayout {

    private Context context;
    private FrameLayout root;
    private TextView titleView;
    private View bottomBorder;
    private View selected;
    private int id;
    private LAMenu menu;
    private String title;
    private LAMIOnClickListener listener;

    private String TAG = "MYMENUITEM";

    public LAMenuItem(@NonNull Context context, LAMenu menu, String title, int id) {
        super(context);
        this.context = context;
        this.menu = menu;
        this.title = title;
        this.id = id;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.la_menu_item, this);
        this.listener = null;
        uiInit();
    }

    public void setOnClickListener(LAMIOnClickListener listener) {
        this.listener = listener;
    }

    private void uiInit() {
        root = findViewById(R.id.root);
        titleView = findViewById(R.id.title);
        bottomBorder = findViewById(R.id.bottom_border);
        selected = findViewById(R.id.selected);
        titleView.setText(title);
        final LAMenuItem self = this;
        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selected.setVisibility(VISIBLE);
                if(listener!=null)
                    listener.onClick(self);
            }
        });
    }

    public void select(boolean mark) {
        if(mark){
            selected.setVisibility(VISIBLE);
        }
        else{
            selected.setVisibility(GONE);
        }
    }

    public void setHeight(int height, boolean isDP){
        LayoutParams params = (LayoutParams) root.getLayoutParams();
        params.height = (isDP ? UnitHelper.dpToPx(context, height) : height);
    }


    public void isLast(){
        bottomBorder.setVisibility(GONE);
    }

    public static int getDefaultHeight(Context context) {
        return UnitHelper.dpToPx(context, 30);
    }

    public void setTitle(String title){
        this.title = title;
        this.titleView.setText(title);
    }

    public String getTitle(){
        return title;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public interface LAMIOnClickListener{
        public void onClick(LAMenuItem item);
    }
}