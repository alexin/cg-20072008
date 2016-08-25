package cg.rayTracingViewer;



/**
 * @author Alexandre Vieira
 */
public class Resolution {

    private int width;
    private int height;

    public Resolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Resolution other = (Resolution) obj;
        if (this.width != other.width) {
            return false;
        }
        if (this.height != other.height) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.width;
        hash = 89 * hash + this.height;
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%dx%d", width, height);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
