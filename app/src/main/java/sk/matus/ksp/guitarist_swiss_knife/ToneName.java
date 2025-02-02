package sk.matus.ksp.guitarist_swiss_knife;

/**
 * Simple class that describes name(s) that a tone can have.
 */
class ToneName {
    char baseName;
    String accidental;
    int octave;

    public ToneName(char baseName, String accidental, int octave) {
        this.baseName = baseName;
        this.accidental = accidental;
        this.octave = octave;
    }

    @Override
    public String toString(){
        return baseName+accidental+octave;
    }

    public String format(String pattern){
        String result = pattern;
        result = result.replaceAll("%b",Character.toString(baseName));
        result = result.replaceAll("%a",accidental);
        result = result.replaceAll("%o",Integer.toString(octave));
        return result;
    }
}