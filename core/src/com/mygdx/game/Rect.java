package com.mygdx.game;

public class Rect {
    float x;
    float y;
    int w;
    int h;

    Rect(float x, float y, int w, int h){

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    static boolean collides(Rect a, Rect b){

        float left1 = a.x;
        float left2 = b.x;
        float right1 = a.x + a.w;
        float right2 = b.x + b.w;

        float top1 = a.y + a.h;
        float top2 = b.y + b.h;
        float bottom1 = a.y;
        float bottom2 = b.y;

        if (bottom1 < top2) return false;
        if (top1 > bottom2) return false;
        if (right1 < left2) return false;
        if (left1 > right2) return false;

        return true;
    }
}
