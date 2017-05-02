/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.widget.buttons;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.widget.interfaces.OnFavoriteButtonClicked;

/**
 * FavoriteButton
 *
 * Controls the toggable nature of the Favorite button.  When a Favorite is selected the nature of
 * the Buttons view will be modified to reflect that change.
 *
 * @author Travis Anthony Torres
 * @version April 8, 2017
 */

public class FavoriteButton implements View.OnClickListener {
    private boolean isFavorite;
    private ImageButton buttonView;
    private Context context;
    private OnFavoriteButtonClicked onClickOperation;

    /**
     * Specifies the Button view object that will represent a Favorite within the system.  The
     * initial state of the toggle will be defined by the isFavorite method call.
     *
     * @param button
     * @param isFavorite
     * @param clickHandler
     * @param mContext
     */
    public FavoriteButton(ImageButton button, boolean isFavorite, OnFavoriteButtonClicked clickHandler, Context mContext) {
        buttonView = button;
        buttonView.setOnClickListener(this);
        context = mContext;
        onClickOperation = clickHandler;

        setIsFavorite(isFavorite);
    }

    /**
     * Sets the state of the button to either favorite or un-favorite.  Will then call either
     * `onSetAsFavorite` or `onSetAsNotFavorite` based on the value provided for `setFavorite`.
     *
     * @param setFavorite
     */
    public void setIsFavorite(boolean setFavorite) {
        isFavorite = setFavorite;
        if (isFavorite) {
            onSetAsFavorite();
        } else {
            onSetAsNotFavorite();
        }
    }

    /**
     * Toggles the value of the `isFavorite` flag when the button is pressed.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        toggle();
    }

    /**
     * Toggles the state of the Favorite button for being `favorite` to `un-favorite` or from
     * `un-favorite` to being `favorite`.
     *
     */
    public void toggle() {
        setIsFavorite(!isFavorite);
    }

    /**
     * Specifies what to do when the favorite state is triggered.
     *
     */
    protected void onSetAsFavorite() {
        String addedFavorite = context.getString(R.string.added_movie_to_favorites_message);
        specifyButtonState(R.drawable.ic_favorite_movie, addedFavorite);
        onClickOperation.onFavorited(buttonView);
    }

    /**
     * Specifies what to do when the un-favorite state is triggered.
     *
     */
    protected void onSetAsNotFavorite() {
        String removedFavorite = context.getString(R.string.removed_movie_from_favorites_message);
        specifyButtonState(R.drawable.ic_unfavorite_movie, removedFavorite);
        onClickOperation.onUnfavorited(buttonView);
    }

    /**
     * Appends an image resource and content description to the ImageButton.
     *
     * @param resourceId
     * @param contentDescription
     */
    private void specifyButtonState(int resourceId, String contentDescription) {
        Resources resources = context.getResources();
        Drawable icon = resources.getDrawable(resourceId, null);
        buttonView.setImageDrawable(icon);
        buttonView.setContentDescription(contentDescription);
    }
}
