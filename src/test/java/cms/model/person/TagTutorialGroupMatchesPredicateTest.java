package cms.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import cms.model.tag.Tag;
import cms.testutil.PersonBuilder;

public class TagTutorialGroupMatchesPredicateTest {

    @Test
    public void equals() {
        TagTutorialGroupMatchesPredicate firstPredicate =
                new TagTutorialGroupMatchesPredicate(Set.of(new Tag("friends")), Set.of(new TutorialGroup("01")));
        TagTutorialGroupMatchesPredicate secondPredicate =
                new TagTutorialGroupMatchesPredicate(Set.of(new Tag("owesMoney")), Set.of(new TutorialGroup("02")));
        TagTutorialGroupMatchesPredicate sameTagsDifferentTutorialGroupsPredicate =
                new TagTutorialGroupMatchesPredicate(Set.of(new Tag("friends")), Set.of(new TutorialGroup("02")));

        assertTrue(firstPredicate.equals(firstPredicate));

        TagTutorialGroupMatchesPredicate firstPredicateCopy =
                new TagTutorialGroupMatchesPredicate(Set.of(new Tag("friends")), Set.of(new TutorialGroup("01")));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(sameTagsDifferentTutorialGroupsPredicate));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void hashCode_sameValues_sameHashCode() {
        TagTutorialGroupMatchesPredicate predicate =
                new TagTutorialGroupMatchesPredicate(Set.of(new Tag("friends")), Set.of(new TutorialGroup("01")));
        TagTutorialGroupMatchesPredicate predicateCopy =
                new TagTutorialGroupMatchesPredicate(Set.of(new Tag("friends")), Set.of(new TutorialGroup("01")));

        assertEquals(predicate.hashCode(), predicateCopy.hashCode());
    }

    @Test
    public void hashCode_consistentAcrossInvocations() {
        TagTutorialGroupMatchesPredicate predicate =
                new TagTutorialGroupMatchesPredicate(Set.of(new Tag("friends")), Set.of(new TutorialGroup("01")));

        assertEquals(predicate.hashCode(), predicate.hashCode());
    }

    @Test
    public void test_matchesWhenTagAndTutorialGroupMatch_returnsTrue() {
        TagTutorialGroupMatchesPredicate predicate =
                new TagTutorialGroupMatchesPredicate(Set.of(new Tag("friends")), Set.of(new TutorialGroup("01")));

        assertTrue(predicate.test(new PersonBuilder().withTags("friends").withTutorialGroup("01").build()));
    }

    @Test
    public void test_failsWhenTutorialGroupDoesNotMatch_returnsFalse() {
        TagTutorialGroupMatchesPredicate predicate =
                new TagTutorialGroupMatchesPredicate(Set.of(new Tag("friends")), Set.of(new TutorialGroup("02")));

        assertFalse(predicate.test(new PersonBuilder().withTags("friends").withTutorialGroup("01").build()));
    }

    @Test
    public void test_requiresAllTags_returnsFalseWhenOneTagMissing() {
        TagTutorialGroupMatchesPredicate predicate =
                new TagTutorialGroupMatchesPredicate(Set.of(new Tag("friends"), new Tag("owesMoney")), Set.of());

        assertFalse(predicate.test(new PersonBuilder().withTags("friends").build()));
    }

    @Test
    public void toStringMethod() {
        Set<Tag> tags = Set.of(new Tag("friends"));
        Set<TutorialGroup> tutorialGroups = Set.of(new TutorialGroup("01"));
        TagTutorialGroupMatchesPredicate predicate =
                new TagTutorialGroupMatchesPredicate(tags, tutorialGroups);

        String expected = TagTutorialGroupMatchesPredicate.class.getCanonicalName()
                + "{tags=" + tags + ", tutorialGroups=" + tutorialGroups + "}";
        assertEquals(expected, predicate.toString());
    }
}
