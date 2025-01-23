package com.example.myapplication.minigame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.R;

import java.util.Random;

public class Spike {
    Bitmap spike[] = new Bitmap[3];
    int spikeFrame = 0;
    int spikeX, spikeY, spikeVelocity;
    Random random;

    public Spike(Context context) {
        spike[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.item1_1);
        spike[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.item1_2);
        spike[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.item1_3);

        // Initialize the Random object here
        random = new Random();

        // Now that random is initialized, you can call resetPosition
        resetPosition();
    }

    public Bitmap getSpike(int spikeFrame) {
        return spike[spikeFrame];
    }

    public int getSpikeWidth() {
        return spike[0].getWidth();
    }

    public int getSpikeHeight() {
        return spike[0].getHeight();
    }

    public void resetPosition() {
        // These lines now use the properly initialized random object
        spikeX = random.nextInt(GameView.dWidth - getSpikeWidth());
        spikeY = -200 + random.nextInt(600) * -1;
        spikeVelocity = 35 + random.nextInt(16);
    }
}