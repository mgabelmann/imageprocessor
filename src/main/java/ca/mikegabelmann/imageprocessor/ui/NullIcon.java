package ca.mikegabelmann.imageprocessor.ui;

/**
 * Class that implements the Icon interface. To be used when there is no Icon
 * for whatever reason, but one is required.
 *
 */
public final class NullIcon implements javax.swing.Icon {

    /** Creates a new instance of NullIcon */
    public NullIcon() {
        ;
    }

    @Override
    public int getIconHeight() {
        return 0;
    }

    @Override
    public int getIconWidth() {
        return 0;
    }

    @Override
    public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
        ;
    }
    
}
