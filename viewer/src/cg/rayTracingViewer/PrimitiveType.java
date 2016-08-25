package cg.rayTracingViewer;

import rayTracing.scene.Primitive;

/**
 * @author Alexandre Vieira
 */
public class PrimitiveType {

    private Class<? extends Primitive> type;
    private String alias;

    public PrimitiveType(Class<? extends Primitive> type, String alias) {
        this.type = type;
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PrimitiveType)) {
            throw new IllegalArgumentException("Only PrimitiveType allowed.");
        }
        return hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 11 * hash + (this.alias != null ? this.alias.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return alias;
    }

    public Class<? extends Primitive> getType() {
        return type;
    }

    public String getAlias() {
        return alias;
    }

    protected Primitive getPrimitiveInstance() throws Exception {
        return (Primitive) type.getConstructors()[0].newInstance();
    }
}
