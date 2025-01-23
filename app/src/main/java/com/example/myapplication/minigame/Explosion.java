package com.example.myapplication.minigame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.R;

public class Explosion {
    Bitmap explosion[] = new Bitmap[4];
    int explosionFrame = 0;
    int explostionX, explostionY;

    public Explosion(Context context) {
        explosion[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explode1);
        explosion[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explode2);
        explosion[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explode3);
        explosion[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explode4);
    }

    public Bitmap getExplosion(int explosionFrame) {
        return explosion[explosionFrame];
    }

    public int getNumberOfFrames() {
        return explosion.length; // This will return 4 in this case
    }
}
