package com.example.myapplication.minigame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.GameOver;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    Bitmap background, ground, tong;
    Rect rectBackground, rectGound;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    int life = 3;
    static int dWidth, dHeight;
    Random random;
    float tongX, tongY;
    float oldX;
    float oldtongX;
    ArrayList<Spike> spikes;
    ArrayList<Explosion> explosions;

    ArrayList<Trash> trashArray; // Declare the trash array

    public GameView(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        tong = BitmapFactory.decodeResource(getResources(), R.drawable.tong);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGound = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        textPaint.setColor(Color.rgb(255, 165, 0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.kenney_blocks));
        healthPaint.setColor(Color.GREEN);
        random = new Random();
        tongX = dWidth / 2 - tong.getWidth() / 2;
        tongY = dHeight - ground.getHeight() - tong.getHeight();
        spikes = new ArrayList<>();
        explosions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Spike spike = new Spike(context);
            spikes.add(spike);
        }

        // Initialize the trash array and add Trash objects
        trashArray = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Trash trash = new Trash(context);
            trashArray.add(trash);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGound, null);
        canvas.drawBitmap(tong, tongX, tongY, null);

        // Draw and update spikes
        for (Spike spike : spikes) {
            canvas.drawBitmap(spike.getSpike(spike.spikeFrame), spike.spikeX, spike.spikeY, null);
            spike.spikeFrame++;
            if (spike.spikeFrame > 2) {
                spike.spikeFrame = 0;
            }
            spike.spikeY += spike.spikeVelocity;
            if (spike.spikeY + spike.getSpikeHeight() >= dHeight - ground.getHeight()) {
                points += 10;
                Explosion explosion = new Explosion(context);
                explosion.explostionX = spike.spikeX;
                explosion.explostionY = spike.spikeY;
                explosions.add(explosion);
                spike.resetPosition();
            }
        }





            // Check for collisions between spikes and tong
        for (Spike spike : spikes) {
            if (spike.spikeX + spike.getSpikeWidth() >= tongX
                    && spike.spikeX <= tongX + tong.getWidth()
                    && spike.spikeY + spike.getSpikeHeight() >= tongY
                    && spike.spikeY <= tongY + tong.getHeight()) {
                life--;
                spike.resetPosition();
                if (life == 0) {
                    Intent intent = new Intent(context, GameOver.class); // Assuming you have a GameOverActivity
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }

        // Draw and update explosions
        for (int i = 0; i < explosions.size(); i++) {
            Explosion explosion = explosions.get(i);
            if (explosion.explosionFrame < explosion.getNumberOfFrames()) {
                canvas.drawBitmap(explosion.getExplosion(explosion.explosionFrame), explosion.explostionX, explosion.explostionY, null);
                explosion.explosionFrame++;
            }
            if (explosion.explosionFrame >= explosion.getNumberOfFrames()) {
                explosions.remove(i);
                // Decrement the loop counter to account for the removed item
                i--;
            }
        }

        // Draw and update trash
        for (Trash trash : trashArray) {
            canvas.drawBitmap(trash.getTrash(trash.trashFrame), trash.trashX, trash.trashY, null);
            trash.trashFrame++;
            if (trash.trashFrame > 2){
                trash.trashFrame = 0;
            }
            trash.trashY += trash.trashVelocity;
            if (trash.trashY + trash.getTrashHeight() >= dHeight - ground.getHeight()) {
                Explosion explosion = new Explosion(context);
                explosion.explostionX = trash.trashX;
                explosion.explostionY = trash.trashY;
                explosions.add(explosion);
                trash.resetPosition();
            }
        }

        for (Trash trash : trashArray) {
            canvas.drawBitmap(trash.getTrash(trash.trashFrame), trash.trashX, trash.trashY, null);
            trash.trashY += trash.trashVelocity;

            // Check if the trash goes off the bottom of the screen
            if (trash.trashY > dHeight) {
                trash.resetPosition();
            }

            // Check for collision with the tong
            if (trash.trashX + trash.getTrashWidth() >= tongX
                    && trash.trashX <= tongX + tong.getWidth()
                    && trash.trashY + trash.getTrashHeight() >= tongY
                    && trash.trashY <= tongY + tong.getHeight()) {
                points += 10; // Add points for catching the trash
                trash.resetPosition(); // Reset the trash position
            }
        }

        for (int i = 0; i < explosions.size(); i++) {
            Explosion explosion = explosions.get(i);
            if (explosion.explosionFrame < explosion.getNumberOfFrames()) {
                canvas.drawBitmap(explosion.getExplosion(explosion.explosionFrame), explosion.explostionX, explosion.explostionY, null);
                explosion.explosionFrame++;
            }
            if (explosion.explosionFrame >= explosion.getNumberOfFrames()) {
                explosions.remove(i);
                // Decrement the loop counter to account for the removed item
                i--;
            }
        }


        // Update health color based on life
        if (life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            healthPaint.setColor(Color.RED);
        }

        // Draw health bar and points
        canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 60 * life, 80, healthPaint);
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);

        // Schedule the next frame
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= tongY){
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN){
                oldX = event.getX();
                oldtongX = tongX;
            }
            if (action == MotionEvent.ACTION_MOVE){
                float shift = oldX - touchX;
                float newtongX = oldtongX - shift;
                if (newtongX <= 0)
                    tongX = 0;
                else if (newtongX >= dWidth - tong.getWidth())
                    tongX = dWidth - tong.getWidth();
                else
                    tongX = newtongX;
            }
        }
        return true;
    }
}