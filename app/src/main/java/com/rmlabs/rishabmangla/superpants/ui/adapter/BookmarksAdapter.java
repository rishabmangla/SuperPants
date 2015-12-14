package com.rmlabs.rishabmangla.superpants.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rmlabs.rishabmangla.superpants.R;
import com.rmlabs.rishabmangla.superpants.tools.StoreSession;
import com.squareup.picasso.Picasso;

public class BookmarksAdapter extends PagerAdapter {

    Uri mCurrentTopUri;
    String mTopsUriList[];
    Uri mCurrentPantsUri;
    String mPantsUriList[];
    int numberOfBookmarks;
    private Context mContext;

    private final int IMAGE_SIZE = 256;

    public BookmarksAdapter(Context context) {
        super();
        mContext = context;
        StoreSession ssn = new StoreSession(context);
        mTopsUriList = ssn.retrieveArray(StoreSession.BOOKMARKS_TOPS_URI);
        mPantsUriList = ssn.retrieveArray(StoreSession.BOOKMARKS_PANTS_URI);
        if(mTopsUriList != null)
            numberOfBookmarks = mTopsUriList.length;
    }

    @Override
    public int getCount() {
        return numberOfBookmarks;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /* Inflating the view */
        View view = inflater.inflate(R.layout.view_bookmarks_pager_item, container, false);

        final ImageView bookmarksTop = (ImageView) view.findViewById(R.id.bookmark_top);
        final ImageView bookmarksPants = (ImageView) view.findViewById(R.id.bookmark_pants);
        mCurrentTopUri = Uri.parse(mTopsUriList[position]);
//        Picasso.with(mContext).load(mCurrentTopUri).into(bookmarksTop);
        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });
        builder.build().load(mCurrentTopUri).resize(IMAGE_SIZE, IMAGE_SIZE).centerCrop().into(bookmarksTop);
        mCurrentPantsUri = Uri.parse(mPantsUriList[position]);
//        Picasso.with(mContext).load(mCurrentPantsUri).into(bookmarksPants);
        Picasso.Builder builder2 = new Picasso.Builder(mContext);
        builder2.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });
        builder2.build().load(mCurrentPantsUri).resize(IMAGE_SIZE, IMAGE_SIZE).centerCrop().into(bookmarksPants);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
