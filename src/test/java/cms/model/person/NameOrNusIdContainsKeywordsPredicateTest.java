package cms.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import cms.testutil.PersonBuilder;

public class NameOrNusIdContainsKeywordsPredicateTest {

    @Test
    public void test_nameOnly_matches() {
        NameOrNusIdContainsKeywordsPredicate predicate = new NameOrNusIdContainsKeywordsPredicate(
                Arrays.asList("Alice", "Bob"), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_idOnly_matches() {
        NameOrNusIdContainsKeywordsPredicate predicate = new NameOrNusIdContainsKeywordsPredicate(
                Collections.emptyList(), Collections.singletonList("A0234504F"));
        assertTrue(predicate.test(new PersonBuilder().withNusId("A0234504F").build()));
    }

    @Test
    public void test_mixed_matchesOnEither() {
        NameOrNusIdContainsKeywordsPredicate predicate = new NameOrNusIdContainsKeywordsPredicate(
                Arrays.asList("john"), Arrays.asList("A0234504F"));
        assertTrue(predicate.test(new PersonBuilder().withName("John Doe").build()));
        assertTrue(predicate.test(new PersonBuilder().withNusId("A0234504F").build()));
    }

    @Test
    public void test_noMatches_returnsFalse() {
        NameOrNusIdContainsKeywordsPredicate predicate = new NameOrNusIdContainsKeywordsPredicate(
                Arrays.asList("x"), Arrays.asList("A9999999Z"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withNusId("A0234504F").build()));
    }

    @Test
    public void test_nullLists_treatedAsEmpty() {
        NameOrNusIdContainsKeywordsPredicate predicate = new NameOrNusIdContainsKeywordsPredicate(null, null);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withNusId("A0234504F").build()));

        // equals behavior
        NameOrNusIdContainsKeywordsPredicate emptyPredicate =
                new NameOrNusIdContainsKeywordsPredicate(Collections.emptyList(), Collections.emptyList());
        assertEquals(emptyPredicate, predicate);
    }

    @Test
    public void toStringMethod() {
        NameOrNusIdContainsKeywordsPredicate predicate = new NameOrNusIdContainsKeywordsPredicate(
                Arrays.asList("a"), Arrays.asList("A0123456B"));
        String expected = NameOrNusIdContainsKeywordsPredicate.class.getCanonicalName()
                + "{nameKeywords=" + Arrays.asList("a") + ", idKeywords=" + Arrays.asList("A0123456B") + "}";
        // toString uses ToStringBuilder, so just check it contains both lists
        String actual = predicate.toString();
        assertTrue(actual.contains("a"));
        assertTrue(actual.contains("A0123456B"));
    }

    @Test
    public void test_idCaseInsensitive_matches() {
        // predicate has lowercase id keyword, person has uppercase stored NusId; should match
        NameOrNusIdContainsKeywordsPredicate predicate = new NameOrNusIdContainsKeywordsPredicate(
                Collections.emptyList(), Collections.singletonList("a0234504f"));
        assertTrue(predicate.test(new PersonBuilder().withNusId("A0234504F").build()));

        // token in uppercase also matches
        NameOrNusIdContainsKeywordsPredicate predicateUpper = new NameOrNusIdContainsKeywordsPredicate(
                Collections.emptyList(), Collections.singletonList("A0234504F"));
        assertTrue(predicateUpper.test(new PersonBuilder().withNusId("a0234504f").build()));
    }
}
