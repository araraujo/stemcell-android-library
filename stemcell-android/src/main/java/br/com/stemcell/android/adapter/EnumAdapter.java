package br.com.stemcell.android.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.List;

/**
 * This is your own Adapter implementation which displays
 * the ArrayList of Enumeration-Objects.
 */
public class EnumAdapter<T> extends BaseAdapter implements SpinnerAdapter {
    /**
     * The internal data (the ArrayList with the Objects).
     */
    private Activity activity;
    private List<T> data = null;
    private String hint;

    public EnumAdapter(Activity activity, List<T> data, String hint){
        this.activity = activity;
        this.data = data;
        this.hint = hint;
    }

    /**
     * Returns the Size of the ArrayList
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * Returns one Element of the ArrayList
     * at the specified position.
     */
    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Returns the View that is shown when a element was
     * selected.
     */
    @Override
    public View getView(int position, View recycle, ViewGroup parent) {
        TextView text;
        if (recycle != null){
            // Re-use the recycled view here!
            text = (TextView) recycle;
        } else {
            // No recycled view, inflate the "original" from the platform:
            text = (TextView) activity.getLayoutInflater().inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false
            );
        }
        text.setTextColor(Color.BLACK);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        text.setMaxLines(2);
        if (hint != null && !hint.isEmpty()) {
            text.setHint(hint);
        }

        String descricao = null;
        if (data.get(position) != null) {
            Method method = ReflectionUtils.findMethod(data.get(position).getClass(), "getDescricao");
            descricao = (String) ReflectionUtils.invokeMethod(method, data.get(position));
        }
        text.setText(descricao);
        return text;
    }

}
