package cms.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import cms.testutil.PersonBuilder;

/**
 * Unit tests for {@link AllFieldsContainsKeywordsPredicate} to improve path coverage.
 */
public class AllFieldsContainsKeywordsPredicateTest {

    @Test
    public void nullKeywords_returnsFalse() {
        AllFieldsContainsKeywordsPredicate predicate = new AllFieldsContainsKeywordsPredicate(null);
        Person person = new PersonBuilder().build();
        assertFalse(predicate.test(person)); // constructor should treat null as empty list
    }

    @Test
    public void emptyKeywords_returnsFalse() {
        AllFieldsContainsKeywordsPredicate predicate = new AllFieldsContainsKeywordsPredicate(Collections.emptyList());
        Person person = new PersonBuilder().build();
        assertFalse(predicate.test(person));
    }

    @Test
    public void matches_eachField_returnsTrue() {
        // Build a person with a variety of fields that we will match against
        Person person = new PersonBuilder()
                .withName("Alice Pauline")
                .withPhone("91234567")
                .withEmail("alice@example.com")
                .withNusId("A0123456X")
                .withSocUsername("alice")
                .withGithubUsername("alicegit")
                .withRole("student")
                .withTutorialGroup("T10")
                .withTags("friends")
                .build();

        // name (contains word ignore case)
        AllFieldsContainsKeywordsPredicate pName = new AllFieldsContainsKeywordsPredicate(Arrays.asList("alice"));
        assertTrue(pName.test(person));

        // nus id equalsIgnoreCase
        AllFieldsContainsKeywordsPredicate pNus = new AllFieldsContainsKeywordsPredicate(Arrays.asList("a0123456x"));
        assertTrue(pNus.test(person));

        // phone contains
        AllFieldsContainsKeywordsPredicate pPhone = new AllFieldsContainsKeywordsPredicate(Arrays.asList("9123"));
        assertTrue(pPhone.test(person));

        // email contains
        AllFieldsContainsKeywordsPredicate pEmail =
                new AllFieldsContainsKeywordsPredicate(Collections.singletonList("example.com"));
        assertTrue(pEmail.test(person));

        // soc contains
        AllFieldsContainsKeywordsPredicate pSoc = new AllFieldsContainsKeywordsPredicate(Arrays.asList("alice"));
        assertTrue(pSoc.test(person));

        // github contains
        AllFieldsContainsKeywordsPredicate pGithub = new AllFieldsContainsKeywordsPredicate(Arrays.asList("alicegit"));
        assertTrue(pGithub.test(person));

        // role equalsIgnoreCase
        AllFieldsContainsKeywordsPredicate pRole = new AllFieldsContainsKeywordsPredicate(Arrays.asList("STUDENT"));
        assertTrue(pRole.test(person));

        // tutorial equalsIgnoreCase
        AllFieldsContainsKeywordsPredicate pTut = new AllFieldsContainsKeywordsPredicate(Arrays.asList("t10"));
        assertTrue(pTut.test(person));

        // tag equalsIgnoreCase
        AllFieldsContainsKeywordsPredicate pTag = new AllFieldsContainsKeywordsPredicate(Arrays.asList("FRIENDS"));
        assertTrue(pTag.test(person));
    }

    @Test
    public void onlyNusIdMatches_returnsTrue() {
        Person person = new PersonBuilder().withNusId("A0123456X").build();
        AllFieldsContainsKeywordsPredicate p = new AllFieldsContainsKeywordsPredicate(
                Collections.singletonList("a0123456x"));
        assertTrue(p.test(person));
    }

    @Test
    public void onlyPhoneMatches_returnsTrue() {
        Person person = new PersonBuilder().withPhone("91234567").build();
        AllFieldsContainsKeywordsPredicate p = new AllFieldsContainsKeywordsPredicate(
                Collections.singletonList("9123"));
        assertTrue(p.test(person));
    }

    @Test
    public void onlyEmailMatches_returnsTrue() {
        Person person = new PersonBuilder().withEmail("alice@example.com").build();
        AllFieldsContainsKeywordsPredicate p = new AllFieldsContainsKeywordsPredicate(
                Collections.singletonList("example.com"));
        assertTrue(p.test(person));
    }

    @Test
    public void onlySocMatches_returnsTrue() {
        Person person = new PersonBuilder().withSocUsername("alice").build();
        AllFieldsContainsKeywordsPredicate p = new AllFieldsContainsKeywordsPredicate(
                Collections.singletonList("alice"));
        assertTrue(p.test(person));
    }

    @Test
    public void onlyGithubMatches_returnsTrue() {
        Person person = new PersonBuilder().withGithubUsername("alicegit").build();
        AllFieldsContainsKeywordsPredicate p = new AllFieldsContainsKeywordsPredicate(
                Collections.singletonList("alicegit"));
        assertTrue(p.test(person));
    }

    @Test
    public void onlyRoleMatches_returnsTrue() {
        Person person = new PersonBuilder().withRole("student").build();
        AllFieldsContainsKeywordsPredicate p = new AllFieldsContainsKeywordsPredicate(
                Collections.singletonList("STUDENT"));
        assertTrue(p.test(person));
    }

    @Test
    public void onlyTutorialMatches_returnsTrue() {
        Person person = new PersonBuilder().withTutorialGroup("T10").build();
        AllFieldsContainsKeywordsPredicate p = new AllFieldsContainsKeywordsPredicate(
                Collections.singletonList("t10"));
        assertTrue(p.test(person));
    }

    @Test
    public void multipleKeywords_laterKeywordMatches_returnsTrue() {
        Person person = new PersonBuilder().withName("Alice Pauline").build();
        AllFieldsContainsKeywordsPredicate p = new AllFieldsContainsKeywordsPredicate(
                Arrays.asList("nomatch", "alice"));
        assertTrue(p.test(person));
    }

    @Test
    public void noFieldMatches_returnsFalse() {
        Person person = new PersonBuilder().withName("Bob").withPhone("88888888").withTags("classmate").build();
        AllFieldsContainsKeywordsPredicate predicate = new AllFieldsContainsKeywordsPredicate(Arrays.asList("nomatch"));
        assertFalse(predicate.test(person));
    }

    @Test
    public void equals_sameAndDifferentObjects_behaviour() {
        AllFieldsContainsKeywordsPredicate predicate = new AllFieldsContainsKeywordsPredicate(Arrays.asList("a", "b"));
        // same object
        assertTrue(predicate.equals(predicate));
        // different type
        assertFalse(predicate.equals(new Object()));
        // different keywords
        AllFieldsContainsKeywordsPredicate other = new AllFieldsContainsKeywordsPredicate(Arrays.asList("c"));
        assertFalse(predicate.equals(other));
    }

    @Test
    public void toString_containsKeywords() {
        AllFieldsContainsKeywordsPredicate predicate = new AllFieldsContainsKeywordsPredicate(Arrays.asList("x"));
        String s = predicate.toString();
        assertTrue(s != null && s.contains("x"));
        // basic sanity: round-trip via equals on keywords isn't available, but toString should mention the keywords
        assertEquals(true, s.contains("keywords"));
    }
}

