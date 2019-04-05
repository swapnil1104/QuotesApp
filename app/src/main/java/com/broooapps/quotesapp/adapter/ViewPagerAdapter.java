package com.broooapps.quotesapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.broooapps.quotesapp.R;
import com.broooapps.quotesapp.model.Quote;
import com.broooapps.quotesapp.util.ViewUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Swapnil Tiwari on 23/03/19.
 * swapnil.tiwari@box8.in
 */
public class ViewPagerAdapter extends PagerAdapter {

    // ------------------Data members of the class --------------------
    private Context context;
    private List<Quote> quotes;
    // -----------------------------------------------------------------

    // ------------------------- Constructors -------------------------
    public ViewPagerAdapter(Context context, List<Quote> quotes) {
        this.context = context;
        this.quotes = quotes;
    }

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }
    //---------------------------------------------------------------------

    /**
     * Method to find out number of Views in ViewPager
     *
     * @return
     */
    @Override
    public int getCount() {
        return quotes.size();
    }

    /**
     * Overridden method
     *
     * @param view   :: current view
     * @param object :: current object
     * @return true if view from object, else false
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * Overridden method
     *
     * @param container :: container ViewGroup
     * @param position  :: current position
     * @return :: Object
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.view_pager_quote, null);

        Quote quote = quotes.get(position);
        if (quote != null) {

            TextView text_quote_display = view.findViewById(R.id.text_quote_display);
            TextView text_quote_author = view.findViewById(R.id.text_quote_author);

            ViewUtils.setVerticalBias(text_quote_display);

            text_quote_author.setText(quote.getAuthor());
            text_quote_display.setText(quote.getText());

            Picasso.get()
                    .load(quote.getUrl())
                    .fit()
                    .centerCrop()
                    .into((ImageView) view.findViewById(R.id.bg_image));

            container.addView(view);
        }

        return view;
    }

    /**
     * Destroy item, Overridden method
     *
     * @param container :: Container ViewGroup
     * @param position  :: position to be destroyed
     * @param object    :: Object associated with View
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

