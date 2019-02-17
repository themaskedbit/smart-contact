package com.themaskedbit.tempcontact;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import static android.support.v7.widget.helper.ItemTouchHelper.*;


class SwipeController extends Callback {
    private boolean swipeBack;
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private static final float buttonWidth = 500;
    enum ButtonsState {
        GONE,
        RIGHT_VISIBLE
    };

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE ) {
            if (dX < -1) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
            else{
                buttonShowedState=ButtonsState.GONE;
            }
            setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            drawButtons(c, viewHolder, -(dX));
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder, float dX) {
        float buttonWidthWithoutPadding = dX - 20;
        float corners = 0;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        if(buttonShowedState != ButtonsState.GONE) {
            RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(Color.RED);
            c.drawRoundRect(rightButton, corners, corners, p);
            drawText("Added to temporary", c, rightButton, p);
        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 30;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    }

    private void setTouchListener(final Canvas c,
                                  final RecyclerView recyclerView,
                                  final RecyclerView.ViewHolder viewHolder,
                                  final float dX, final float dY,
                                  final int actionState, final boolean isCurrentlyActive) {

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP ;
                if(event.getAction() == MotionEvent.ACTION_UP && -dX > buttonWidth) {
                    Toast.makeText(recyclerView.getContext(),"Contacted added to temporary", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }

}
