package cg.rayTracingViewer;

import rayTracing.scene.Light;

/**
 * @author Alexandre Vieira
 */
public class LightType {

    private Class<? extends Light> type;
    private String alias;

    public LightType(Class<? extends Light> type, String alias) {
        this.type = type;
        this.alias = alias;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LightType other = (LightType) obj;
        if (this.type != other.type && (this.type == null || !this.type.equals(other.type))) {
            return false;
        }
        if (this.alias == null || !this.alias.equals(other.alias)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }
    
    @Override
    public String toString() {
        return alias;
    }

    protected Class<? extends Light> getType() {
        return type;
    }

    public String getAlias() {
        return alias;
    }

    protected Light getPrimitiveInstance() throws Exception {
        return (Light) type.getConstructors()[0].newInstance();
    }
}
