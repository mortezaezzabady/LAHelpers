package com.lordarian.lahelpers.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lordarian.lahelpers.R;
import com.lordarian.lahelpers.UnitHelper;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class LAMenu extends FrameLayout {

    private Context context;
    private ImageButton button;
    private EditText field;
    private FrameLayout root;
    private boolean shown;

    private ViewGroup parent;
    private LinearLayout layout;
    private ArrayList<LAMenuItem> items;
    private String[] itemTitles;

    private String TAG = "LAMenu";

    public int width;
    public int height;
    private int marginTop;
    private int marginRight;
    private int marginBottom;
    private int marginLeft;

    private int selectedItem;
    private int id;

    private String defaultItemTitle;

    private LAMOnItemClickListener listener;

    public LAMenu(@NonNull Context context, ViewGroup parent) {
        super(context);
        this.context = context;
        this.parent = parent;
        this.items = new ArrayList<>();
        this.selectedItem = 0;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.la_menu, this);
        this.listener = null;
        uiInit();
    }

    public LAMenu(@NonNull Context context, ViewGroup parent, String[] itemTitles) {
        this(context, parent);
        setItems(itemTitles);
    }

    public void setOnItemClickListener(LAMOnItemClickListener listener) {
        this.listener = listener;
    }

    public void setID(int id){
        this.id = id;
    }

    public void setItems(String[] itemTitles){
        setItems(itemTitles, null);
    }

    public void setItems(String[] itemTitles, String defaultItemTitle){
        this.defaultItemTitle = defaultItemTitle;
        this.itemTitles = itemTitles;
        this.parent.removeView(layout);
        buildLayout();
    }

    private void uiInit() {
        root = findViewById(R.id.root);
        button = findViewById(R.id.btn);
        field = findViewById(R.id.field);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(true);
        }
        shown = false;
        width = root.getWidth();
        height = root.getHeight();
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shown){
                    hide();
                }
                else{
                    show();
                }
            }
        });
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyUnconsumed, dxUnconsumed, dyConsumed);
    }

    public void hide() {
        if(layout == null)
            return;
        shown = false;
        layout.setVisibility(View.GONE);
        button.setImageResource(R.drawable.ic_arrow_down);
    }

    private void buildLayout(){
        layout = new LinearLayout(context);
        int w = width - height;

        LayoutParams params = new LayoutParams(w, 5 * LAMenuItem.getDefaultHeight(context));
        params.setMargins(marginLeft + UnitHelper.dpToPx(context, 4), marginTop + height - UnitHelper.dpToPx(context, 6), marginRight + height - UnitHelper.dpToPx(context, 4), marginBottom);
        LayoutParams rootParams = (LayoutParams) root.getLayoutParams();
        params.gravity = rootParams.gravity;

        layout.setLayoutParams(params);
        layout.setBackgroundColor(Color.WHITE);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout tmp = new LinearLayout(context);
        LayoutParams tmpp = new LayoutParams(w, 5 * LAMenuItem.getDefaultHeight(context));
        tmp.setLayoutParams(tmpp);
        tmp.setBackgroundColor(Color.WHITE);
        tmp.setOrientation(LinearLayout.VERTICAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layout.setElevation(10);
        }
        NestedScrollView scroll = new NestedScrollView(context);
        scroll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        final LAMenu self = this;
        for(int i = 0; i < itemTitles.length; i++){
            LAMenuItem item = new LAMenuItem(context,this, itemTitles[i], i);
            if(i == itemTitles.length - 1)
                item.isLast();
            items.add(item);
            if(defaultItemTitle == null) {
                if (i == 0) {
                    item.select(true);
                    setSelectedItem(item.getId());
                    setText(item.getTitle());
                }
            }
            else{
                if(itemTitles[i].equals(defaultItemTitle)){
                    item.select(true);
                    setSelectedItem(item.getId());
                    setText(item.getTitle());
                }
            }
            tmp.addView(item);
            item.setOnClickListener(new LAMenuItem.LAMIOnClickListener() {
                @Override
                public void onClick(LAMenuItem item) {
                    try {
                        getSelectedItem().select(false);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    setSelectedItem(item.getId());
                    setText(item.getTitle());
                    hide();
                    if(listener!=null)
                        listener.onItemClick(self, item);
                }
            });
        }
        scroll.addView(tmp);
        layout.addView(scroll);
        layout.setVisibility(GONE);
        parent.addView(layout);
    }

    public void show() {
        shown = true;
        if(layout == null){
            buildLayout();
        }
        layout.setVisibility(View.VISIBLE);
        button.setImageResource(R.drawable.ic_arrow_up);
    }

    public void setText(String text){
        field.setText(text);
    }

    public void setHint(String hint){
        field.setHint(hint);
    }

    public String getText(){
        return field.getText().toString();
    }

    public void setSize(int width, int height){
        this.width = UnitHelper.dpToPx(context, width);
        this.height = UnitHelper.dpToPx(context, height);

        LayoutParams params = (LayoutParams) root.getLayoutParams();
        params.height = UnitHelper.dpToPx(context, height);
        params.width = UnitHelper.dpToPx(context, width);

        LayoutParams btnParams = (LayoutParams) button.getLayoutParams();
        btnParams.height = UnitHelper.dpToPx(context, height - 10);
        btnParams.width = UnitHelper.dpToPx(context, height - 10);

        LayoutParams fieldParams = (LayoutParams) field.getLayoutParams();
        fieldParams.height = UnitHelper.dpToPx(context, height - 10);
        fieldParams.width = UnitHelper.dpToPx(context, width - height);

    }

    public void setSize(int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom, int gravity){
        setSize(width, height);
        setMargins(marginLeft, marginTop, marginRight, marginBottom);
        setLayoutGravity(gravity);
    }

    public void setLayoutGravity(int gravity){
        LayoutParams params = (LayoutParams) root.getLayoutParams();
        params.gravity = gravity;
    }

    public void setMargins(int marginLeft, int marginTop, int marginRight, int marginBottom){
        this.marginLeft = UnitHelper.dpToPx(context, marginLeft);
        this.marginTop = UnitHelper.dpToPx(context, marginTop);
        this.marginRight = UnitHelper.dpToPx(context, marginRight);
        this.marginBottom = UnitHelper.dpToPx(context, marginBottom);

        LayoutParams params = (LayoutParams) root.getLayoutParams();
        params.setMargins(UnitHelper.dpToPx(context, marginLeft),UnitHelper.dpToPx(context, marginTop),UnitHelper.dpToPx(context, marginRight),UnitHelper.dpToPx(context, marginBottom));
    }

    public void setSelectedItem(int id){
        this.selectedItem = id;
    }

    public LAMenuItem getSelectedItem() throws ArrayIndexOutOfBoundsException{
        return items.get(selectedItem);
    }

    public int getID() {
        return id;
    }

    public interface LAMOnItemClickListener{
        public void onItemClick(LAMenu menu, LAMenuItem item);
    }

}