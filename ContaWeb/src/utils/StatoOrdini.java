package utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public enum StatoOrdini {

    EVASO("Evaso", 10),

    PARZIALMENTE_EVASO("Parzialmente evaso", 2),

    DA_EVADERE("Da evadere", 0);

    protected String label;

    protected int output;

    StatoOrdini(String label, int output) {
        this.label = label;
        this.output = output;
    }

    public String getLabel() {
        return this.label;
    }

    public int getOutput() {
        return this.output;
    }

    public static Collection<String> list() {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < values().length; i++) {
            result.add(values()[i].getLabel());
        }
        return result;
    }

    public static int getOutputFromLabel(String label) {
        if (label != null && !label.isEmpty()) {
            String name = label.toUpperCase().replace(" ", "_");
            return StatoOrdini.valueOf(StatoOrdini.class, name).getOutput();
        }
        return -1;
    }

}
