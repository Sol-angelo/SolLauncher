// Decompiled by Procyon v0.5.36
// 

import java.awt.*;

public abstract class GameObject
{
    protected float x;
    protected float y;
    protected ID id;
    protected float velX;
    protected float velY;
    
    public GameObject(final float x, final float y, final ID id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }
    
    public abstract void tick();
    
    public abstract void render(final Graphics p0);
    
    public abstract Rectangle getBounds();
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void setId(final ID id) {
        this.id = id;
    }
    
    public ID getId() {
        return this.id;
    }
    
    public void setVelX(final float velX) {
        this.velX = velX;
    }
    
    public void setVelY(final float velY) {
        this.velY = velY;
    }
    
    public float getVelX() {
        return this.velX;
    }
    
    public float getVelY() {
        return this.velY;
    }
}
