package rayTracing;

/**
 * @author Alexandre Vieira
 */
public class Settings {

    private int tracingDepth;
    private boolean useDiffuse;
    private boolean useSpecular;
    private boolean useReflection;
    private boolean useRefraction;
    private boolean useShadows;
    private boolean useCullFace;

    public Settings() {
        this.tracingDepth = 0;
    }

    public Settings(int tracingDepth, boolean useDiffuse, boolean useSpecular,
            boolean useReflection, boolean useRefraction, boolean useShadows, boolean useCullFace) {
        this.tracingDepth = tracingDepth;
        this.useDiffuse = useDiffuse;
        this.useSpecular = useSpecular;
        this.useReflection = useReflection;
        this.useRefraction = useRefraction;
        this.useShadows = useShadows;
        this.useCullFace = useCullFace;
    }

    public void setTracingDepth(int tracingDepth) {
        this.tracingDepth = tracingDepth;
    }

    public void setUseDiffuse(boolean useDiffuse) {
        this.useDiffuse = useDiffuse;
    }

    public void setUseReflection(boolean useReflection) {
        this.useReflection = useReflection;
    }

    public void setUseRefraction(boolean useRefraction) {
        this.useRefraction = useRefraction;
    }

    public void setUseSpecular(boolean useSpecular) {
        this.useSpecular = useSpecular;
    }

    public void setUseShadows(boolean useShadows) {
        this.useShadows = useShadows;
    }

    public void setUseCullFace(boolean useCullFace) {
        this.useCullFace = useCullFace;
    }

    public int getTracingDepth() {
        return tracingDepth;
    }

    public boolean isUseDiffuse() {
        return useDiffuse;
    }

    public boolean isUseReflection() {
        return useReflection;
    }

    public boolean isUseRefraction() {
        return useRefraction;
    }

    public boolean isUseSpecular() {
        return useSpecular;
    }

    public boolean isUseShadows() {
        return useShadows;
    }

    public boolean isUseCullFace() {
        return useCullFace;
    }

    public static Settings minimumSettings(int depth) {
        return new Settings(depth, false, false, false, false, false, false);
    }

    public static Settings maximumSettings(int depth) {
        return new Settings(depth, true, true, true, true, true, true);
    }
}
