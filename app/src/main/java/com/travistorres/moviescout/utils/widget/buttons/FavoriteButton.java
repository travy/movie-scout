/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.widget.buttons;

import android.view.View;
import android.widget.Button;

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
    private Button buttonView;
    private OnFavoriteButtonClicked onClickOperation;

    /**
     * Specifies the Button view object that will represent a Favorite within the system.  The
     * initial state of the toggle will be defined by the isFavorite method call.
     *
     * @param button
     * @param isFavorite
     * @param clickHandler
     */
    public FavoriteButton(Button button, boolean isFavorite, OnFavoriteButtonClicked clickHandler) {
        buttonView = button;
        buttonView.setOnClickListener(this);
        onClickOperation = clickHandler;

        setIsFavorite(isFavorite);
    }

    /**
     * Sets the state of the button to either favorite or unfavorite.  Will then call either
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
     * Toggles the state of the Favorite button for being `favorited` to `unfavorited` or from
     * `unfavorited` to being `favorited`.
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
        buttonView.setText("Favorite");
        onClickOperation.onFavorited(buttonView);
    }

    /**
     * Specifies what to do when the unfavorited state is triggered.
     *
     */
    protected void onSetAsNotFavorite() {
        buttonView.setText("Unfavorite");
        onClickOperation.onUnfavorited(buttonView);
    }
}
