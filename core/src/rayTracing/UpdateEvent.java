package rayTracing;

/**
 * @author Alexandre Vieira
 */
public class UpdateEvent {
    
    private float currentPixel;

    public UpdateEvent(float currentPixel){
        this.currentPixel = currentPixel;
    }

    public float getCurrentPixel() {
        return currentPixel;
    }
}
