package ir.ac.kntu.utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TextSimilarityTest - Comprehensive unit tests for text similarity algorithms
 */
@DisplayName("Text Similarity Tests")
class TextSimilarityTest {

    @Test
    @DisplayName("Levenshtein Distance - Exact match")
    void testLevenshteinDistanceExactMatch() {
        assertEquals(0, TextSimilarity.levenshteinDistance("hello", "hello"));
        assertEquals(0, TextSimilarity.levenshteinDistance("", ""));
    }

    @Test
    @DisplayName("Levenshtein Distance - Known examples")
    void testLevenshteinDistanceKnownExamples() {
        assertEquals(3, TextSimilarity.levenshteinDistance("kitten", "sitting"));
        assertEquals(1, TextSimilarity.levenshteinDistance("hello", "hallo"));
        assertEquals(3, TextSimilarity.levenshteinDistance("abc", "xyz"));
    }

    @Test
    @DisplayName("Levenshtein Distance - Empty strings")
    void testLevenshteinDistanceEmptyStrings() {
        assertEquals(5, TextSimilarity.levenshteinDistance("hello", ""));
        assertEquals(5, TextSimilarity.levenshteinDistance("", "hello"));
    }

    @Test
    @DisplayName("Levenshtein Distance - Null handling")
    void testLevenshteinDistanceNull() {
        assertEquals(5, TextSimilarity.levenshteinDistance("hello", null));
        assertEquals(5, TextSimilarity.levenshteinDistance(null, "hello"));
    }

    @Test
    @DisplayName("Levenshtein Similarity - Exact match")
    void testLevenshteinSimilarityExactMatch() {
        assertEquals(1.0, TextSimilarity.levenshteinSimilarity("hello", "hello"), 0.01);
    }

    @Test
    @DisplayName("Levenshtein Similarity - Different strings")
    void testLevenshteinSimilarityDifferent() {
        double similarity = TextSimilarity.levenshteinSimilarity("kitten", "sitting");
        assertTrue(similarity > 0.0 && similarity < 1.0);
    }

    @Test
    @DisplayName("Jaccard Similarity - Exact match")
    void testJaccardSimilarityExactMatch() {
        assertEquals(1.0, TextSimilarity.jaccardSimilarity("hello world", "hello world"), 0.01);
    }

    @Test
    @DisplayName("Jaccard Similarity - Partial match")
    void testJaccardSimilarityPartialMatch() {
        double similarity = TextSimilarity.jaccardSimilarity("hello world", "hello");
        assertTrue(similarity > 0.0 && similarity < 1.0);
    }

    @Test
    @DisplayName("Jaccard Similarity - No match")
    void testJaccardSimilarityNoMatch() {
        assertEquals(0.0, TextSimilarity.jaccardSimilarity("hello", "xyz"), 0.01);
    }

    @Test
    @DisplayName("Combined Similarity - Exact match")
    void testCombinedSimilarityExactMatch() {
        assertEquals(1.0, TextSimilarity.combinedSimilarity("pizza", "pizza"), 0.01);
    }

    @Test
    @DisplayName("Combined Similarity - Substring match")
    void testCombinedSimilaritySubstringMatch() {
        double similarity = TextSimilarity.combinedSimilarity("pizza", "cheese pizza");
        assertTrue(similarity > 0.5);
    }

    @Test
    @DisplayName("Combined Similarity - Starts with")
    void testCombinedSimilarityStartsWith() {
        double similarity = TextSimilarity.combinedSimilarity("pizza", "pizza margherita");
        assertTrue(similarity > 0.7);
    }

    @Test
    @DisplayName("Find Best Matches - Basic functionality")
    void testFindBestMatches() {
        List<String> candidates = Arrays.asList(
            "cheese pizza",
            "pepperoni pizza",
            "pasta",
            "burger",
            "pizza margherita"
        );

        List<TextSimilarity.SearchResult> results = TextSimilarity.findBestMatches("pizza", candidates, 3);

        assertNotNull(results);
        assertTrue(results.size() <= 3);
        assertTrue(results.size() > 0);
        
        // Check that results are sorted by score (descending)
        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue(results.get(i).score >= results.get(i + 1).score);
        }
    }

    @Test
    @DisplayName("Find Best Matches - Empty query")
    void testFindBestMatchesEmptyQuery() {
        List<String> candidates = Arrays.asList("pizza", "burger", "pasta");
        List<TextSimilarity.SearchResult> results = TextSimilarity.findBestMatches("", candidates, 10);
        
        // Should return empty or very few results due to low threshold
        assertNotNull(results);
    }

    @Test
    @DisplayName("Find Best Matches - No matches")
    void testFindBestMatchesNoMatches() {
        List<String> candidates = Arrays.asList("pizza", "burger", "pasta");
        List<TextSimilarity.SearchResult> results = TextSimilarity.findBestMatches("xyzabc123", candidates, 10);
        
        assertNotNull(results);
        // Should have very few or no results due to low similarity
    }

    @Test
    @DisplayName("Autocomplete Suggestions - Basic functionality")
    void testAutocompleteSuggestions() {
        List<String> candidates = Arrays.asList(
            "pizza",
            "pizza margherita",
            "pepperoni pizza",
            "burger",
            "pasta"
        );

        List<String> suggestions = TextSimilarity.getAutocompleteSuggestions("piz", candidates, 3);

        assertNotNull(suggestions);
        assertTrue(suggestions.size() <= 3);
        assertTrue(suggestions.size() > 0);
        
        // All suggestions should start with "piz"
        for (String suggestion : suggestions) {
            assertTrue(suggestion.toLowerCase().startsWith("piz"));
        }
    }

    @Test
    @DisplayName("Autocomplete Suggestions - Empty partial")
    void testAutocompleteSuggestionsEmptyPartial() {
        List<String> candidates = Arrays.asList("pizza", "burger", "pasta");
        List<String> suggestions = TextSimilarity.getAutocompleteSuggestions("", candidates, 2);
        
        assertNotNull(suggestions);
        assertTrue(suggestions.size() <= 2);
    }

    @Test
    @DisplayName("Autocomplete Suggestions - No matches")
    void testAutocompleteSuggestionsNoMatches() {
        List<String> candidates = Arrays.asList("pizza", "burger", "pasta");
        List<String> suggestions = TextSimilarity.getAutocompleteSuggestions("xyz", candidates, 10);
        
        assertNotNull(suggestions);
        assertEquals(0, suggestions.size());
    }

    @Test
    @DisplayName("Null handling in all methods")
    void testNullHandling() {
        assertEquals(0.0, TextSimilarity.levenshteinSimilarity(null, "test"), 0.01);
        assertEquals(0.0, TextSimilarity.levenshteinSimilarity("test", null), 0.01);
        assertEquals(0.0, TextSimilarity.jaccardSimilarity(null, "test"), 0.01);
        assertEquals(0.0, TextSimilarity.combinedSimilarity(null, "test"), 0.01);
        
        List<String> candidates = Arrays.asList("test1", "test2");
        assertNotNull(TextSimilarity.findBestMatches(null, candidates, 10));
        assertNotNull(TextSimilarity.getAutocompleteSuggestions(null, candidates, 10));
    }
}

