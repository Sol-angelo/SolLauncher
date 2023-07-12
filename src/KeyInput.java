// Decompiled by Procyon v0.5.36
// 

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter
{
    private Handler handler;
    private boolean[] keyDown;
    private Launcher launcher;
    
    public KeyInput(final Handler handler, final Launcher launcher) {
        this.keyDown = new boolean[8];
        this.handler = handler;
        this.launcher = launcher;
        this.keyDown[0] = false;
        this.keyDown[1] = false;
        this.keyDown[2] = false;
        this.keyDown[3] = false;
        this.keyDown[4] = false;
        this.keyDown[5] = false;
        this.keyDown[6] = false;
        this.keyDown[7] = false;
    }
    
    @Override
    public void keyPressed(final KeyEvent e) {
        final float key = (float) e.getKeyCode();
    }
    
    @Override
    public void keyReleased(final KeyEvent e) {
        final float key = (float)e.getKeyCode();
    }
}
