public class Word {

    public final String text;
    public final long start;
    public final long end;
    public final float confidence;
    public final String speaker;

    public Word(String text, long start, long end, float confidence, String speaker) {
        this.text = text;
        this.start = start;
        this.end = end;
        this.confidence = confidence;
        this.speaker = speaker;
    }

    @Override
    public String toString() {
        return "text=" + text + ", ";
    }

}
