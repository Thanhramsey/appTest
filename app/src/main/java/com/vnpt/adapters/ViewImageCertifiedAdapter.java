package com.vnpt.adapters;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.vnpt.dto.ImageCertified;
import com.vnpt.staffhddtpos58.R;

import java.io.File;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * @Description: adapter cua list book
 * @author:truonglt2
 * @since:Feb 7, 2014 3:49:14 PM
 * @version: 1.0
 * @since: 1.0
 */

public class ViewImageCertifiedAdapter extends PagerAdapter {
    private ArrayList<ImageCertified> arrImage = new ArrayList<>();
    void ViewImageCertifiedAdapter()
    {
    }
    public void addItem (ImageCertified item)
    {
        arrImage.add(item);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return arrImage.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        String pathImage = arrImage.get(position).getPathLocal();
        if(pathImage ==null || pathImage.equals(""))
        {
            photoView.setImageResource(R.drawable.img_no_photo_to_show);
        }
        else
        {
            Uri uri = Uri.fromFile(new File(pathImage));
            final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
            if(arrImage.get(position).getmHoaDon().getCertifiedCustomer().equals("3"))
            {
                Picasso.with(container.getContext())
                        .load(uri).rotate(90).resize(1280,960).placeholder(R.drawable.img_no_photo_to_show)
                        .into(photoView, new Callback() {
                            @Override
                            public void onSuccess() {
                                attacher.update();
                            }

                            @Override
                            public void onError() {
                            }
                        });
            }
            else
            {
                Picasso.with(container.getContext())
                        .load(uri).placeholder(R.drawable.img_no_photo_to_show)
                        .into(photoView, new Callback() {
                            @Override
                            public void onSuccess() {
                                attacher.update();
                            }

                            @Override
                            public void onError() {
                            }
                        });
            }
        }

//        Picasso.with(this).load(uri)
//                .resize(96, 96).centerCrop().into(photoView);
//        photoView.setImageResource(arrImage.get(position).getIdDrawable());
        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}