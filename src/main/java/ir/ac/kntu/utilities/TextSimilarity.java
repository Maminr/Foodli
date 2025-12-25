package ir.ac.kntu.utilities;

import java.util.*;
import java.util.stream.Collectors;

/*
 * TextSimilarity - Utility class for implementing text similarity algorithms
 *
 * BONUS FEATURES IMPLEMENTATION:
 * - Levenshtein distance for edit-based similarity
 * - Jaccard similarity for set-based similarity
 * - Fuzzy search with result ranking
 * - Autocomplete suggestions
 */
public class TextSimilarity {

    /**
     * Calculate Levenshtein distance between two strings
     *
     * @param str1 First string
     * @param str2 Second string
     * @return Edit distance between strings
     */
    public static int levenshteinDistance(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return Math.max(str1 == null ? 0 : str1.length(),
                    str2 == null ? 0 : str2.length());
        }

        int len1 = str1.length();
        int len2 = str2.length();

        if (len1 == 0) {
            return len2;
        }
        if (len2 == 0) {
            return len1;
        }

        int[][] matrix = new int[len1 + 1][len2 + 1];

        // Initialize first row and column
        for (int i = 0; i <= len1; i++) {
            matrix[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            matrix[0][j] = j;
        }

        // Fill the matrix
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;

                matrix[i][j] = Math.min(
                        Math.min(matrix[i - 1][j] + 1,      // deletion
                                matrix[i][j - 1] + 1),      // insertion
                        matrix[i - 1][j - 1] + cost);     // substitution
            }
        }

        return matrix[len1][len2];
    }

    /**
     * Calculate similarity score based on Levenshtein distance
     *
     * @param str1 First string
     * @param str2 Second string
     * @return Similarity score between 0.0 and 1.0
     */
    public static double levenshteinSimilarity(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return 0.0;
        }

        int maxLength = Math.max(str1.length(), str2.length());
        if (maxLength == 0) {
            return 1.0;
        }

        int distance = levenshteinDistance(str1, str2);
        return 1.0 - (double) distance / maxLength;
    }

    /**
     * Calculate Jaccard similarity between two strings (treating them as sets of words)
     *
     * @param str1 First string
     * @param str2 Second string
     * @return Jaccard similarity score between 0.0 and 1.0
     */
    public static double jaccardSimilarity(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return 0.0;
        }

        Set<String> set1 = tokenizeAndNormalize(str1);
        Set<String> set2 = tokenizeAndNormalize(str2);

        if (set1.isEmpty() && set2.isEmpty()) {
            return 1.0;
        }
        if (set1.isEmpty() || set2.isEmpty()) {
            return 0.0;
        }

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    /**
     * Calculate overall similarity score combining multiple algorithms
     *
     * @param query  Search query
     * @param target Target string to compare
     * @return Combined similarity score
     */
    public static double combinedSimilarity(String query, String target) {
        if (query == null || target == null) {
            return 0.0;
        }

        // Normalize strings
        String normQuery = normalizeString(query);
        String normTarget = normalizeString(target);

        // Calculate different similarity measures
        double levenshtein = levenshteinSimilarity(normQuery, normTarget);
        double jaccard = jaccardSimilarity(query, target);

        // Check for substring matches (high relevance)
        boolean containsQuery = normTarget.contains(normQuery);
        boolean startsWithQuery = normTarget.startsWith(normQuery);

        // Weighted combination
        double score = (levenshtein * 0.4) + (jaccard * 0.4);

        // Bonus for exact matches
        if (containsQuery) {
            score += 0.3;
        }
        if (startsWithQuery) {
            score += 0.3;
        }

        return Math.min(score, 1.0);
    }

    /**
     * Find best matches for a query from a list of candidates
     *
     * @param query      Search query
     * @param candidates List of candidate strings
     * @param maxResults Maximum number of results to return
     * @return List of matches sorted by similarity score
     */
    public static List<SearchResult> findBestMatches(String query, List<String> candidates, int maxResults) {
        List<SearchResult> results = new ArrayList<>();

        for (String candidate : candidates) {
            double score = combinedSimilarity(query, candidate);
            if (score > 0.1) { // Minimum threshold
                results.add(new SearchResult(candidate, score));
            }
        }

        // Sort by score descending
        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        // Return top results
        return results.subList(0, Math.min(maxResults, results.size()));
    }

    /**
     * Generate autocomplete suggestions based on partial input
     *
     * @param partial        Partial input string
     * @param candidates     List of possible completions
     * @param maxSuggestions Maximum number of suggestions
     * @return List of autocomplete suggestions
     */
    public static List<String> getAutocompleteSuggestions(String partial, List<String> candidates, int maxSuggestions) {
        if (partial == null || partial.trim().isEmpty()) {
            return candidates.subList(0, Math.min(maxSuggestions, candidates.size()));
        }

        String normalizedPartial = normalizeString(partial);

        return candidates.stream()
                .filter(candidate -> normalizeString(candidate).startsWith(normalizedPartial))
                .sorted(Comparator.comparingInt(String::length)) // Shorter matches first
                .limit(maxSuggestions)
                .collect(Collectors.toList());
    }

    /**
     * Tokenize and normalize a string into a set of words
     */
    private static Set<String> tokenizeAndNormalize(String text) {
        return Arrays.stream(text.toLowerCase().split("\\s+"))
                .map(word -> word.replaceAll("[^a-zA-Z0-9]", ""))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Normalize string for comparison
     */
    private static String normalizeString(String text) {
        return text.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .trim();
    }

    /**
     * Search result with score
     */
    public static class SearchResult {
        private final String text;
        private final double score;

        public SearchResult(String text, double score) {
            this.text = text;
            this.score = score;
        }

        public String getText() {
            return text;
        }

        public double getScore() {
            return score;
        }

        @Override
        public String toString() {
            return String.format("%.2f: %s", score, text);
        }
    }
}
