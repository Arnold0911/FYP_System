package com.example.myapplication.minigame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.R;

import java.util.Random;

public class Trash {
    Bitmap trash[] = new Bitmap[3];
    int trashFrame = 0;
    int trashX, trashY, trashVelocity;
    Random random;

    private boolean isExploding = false;
    private Explosion explosion;


    public Trash(Context context) {
        trash[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash1);
        trash[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash2);
        trash[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash3);

        // Initialize the Random object here
        random = new Random();

        // Now that random is initialized, you can call resetPosition
        resetPosition();
    }



    public Bitmap getTrash(int trashFrame) {
        return trash[trashFrame];
    }

    public int getTrashWidth() {
        return trash[0].getWidth();
    }

    public int getTrashHeight() {
        return trash[0].getHeight();
    }

    public void resetPosition() {
        // These lines now use the properly initialized random object
        trashX = random.nextInt(GameView.dWidth - getTrashWidth());
        trashY = -200 + random.nextInt(600) * -1;
        trashVelocity = 35 + random.nextInt(16);
    }
}