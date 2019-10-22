public class DocInfo {
    private String filename;
    private double fleschScore;
    private double kincaidScore;

    public DocInfo(String filename, double fleschScore, double kincaidScore) {
        this.fleschScore = fleschScore;
        this.filename = filename;
        this.kincaidScore = kincaidScore;
    }

    public String getFilename() {
        return this.filename;
    }

    public double getFleschScore() {
        return this.fleschScore;
    }

    public void setFleschScore(double fleschScore) {
        this.fleschScore = fleschScore;
    }

    public double getKincaidScore() {
        return this.kincaidScore;
    }

    public void setKincaidScore(double kincaidScore) {
        this.kincaidScore = kincaidScore;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
