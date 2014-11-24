package de.jpaw8.batch.api;

import java.util.function.Predicate;

/** BatchFilter is Java 8 Predicate<E> plus Contributor. */
public interface BatchFilter<E> extends Contributor, Predicate<E> {
}
