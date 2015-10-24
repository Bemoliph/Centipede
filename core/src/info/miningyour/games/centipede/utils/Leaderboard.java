package info.miningyour.games.centipede.utils;

import java.util.LinkedList;
import java.util.List;

public class Leaderboard {

    private static final int maxScores = 10;
    private List<Integer> scores;

    public Leaderboard() {
        scores = new LinkedList<Integer>();
        load();
    }

    private void prune() {
        while (maxScores < scores.size()) {
            scores.remove(maxScores);
        }
    }

    private void save() {
        AssetLoader.prefs.putInteger("high_score_count", scores.size());

        for (int i = 0; i < scores.size(); i++) {
            AssetLoader.prefs.putInteger("high_score_" + i, scores.get(i));
        }

        AssetLoader.prefs.flush();
    }

    private void load() {
        int count = AssetLoader.prefs.getInteger("high_score_count", 0);

        for (int i = 0; i < count; i++) {
            scores.add(AssetLoader.prefs.getInteger("high_score_" + i));
        }
    }

    public void add(int score) {
        if (score == 0) {
            return;
        }

        for (Integer highScore : scores) {
            if (highScore < score) {
                scores.add(scores.indexOf(highScore), score);
                prune();
                save();

                return;
            }
        }

        if (scores.size() < maxScores) {
            scores.add(score);
            save();
        }
    }

    public int get(int index) {
        return scores.get(index);
    }

    public void clear() {
        scores.clear();
    }

    public int size() {
        return scores.size();
    }
}
