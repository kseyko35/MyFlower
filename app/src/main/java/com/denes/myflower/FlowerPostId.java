package com.denes.myflower;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class FlowerPostId {

    @Exclude
    public String flower_PostId;

    public <T extends FlowerPostId> T withId(@NonNull final String id) {
        this.flower_PostId = id;
        return (T) this;
    }
}
