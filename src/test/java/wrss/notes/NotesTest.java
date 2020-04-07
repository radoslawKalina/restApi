package wrss.notes;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/* This class is not part of a project. It is just a kind of notes. */

class NotesTest {

	@Test
	void hamcrest() {
		
		List<Integer> numbers = Arrays.asList(1, 7, 14);

		assertThat(numbers, hasSize(3));
		assertThat(numbers, hasItems(1, 7));
		assertThat(numbers, everyItem(greaterThan(0)));
		assertThat(numbers, everyItem(lessThan(15)));
		
		assertThat("ABCDE", containsString("ABC"));
		assertThat("ABCDE", startsWith("ABC"));
		assertThat("ABCDE", endsWith("DE"));
		
	}
	
	@Test
	void assertJ() {
		
		List<Integer> numbers = Arrays.asList(1, 7, 14);

		assertThat(numbers).hasSize(3)
						   .contains(1, 7)
						   .allMatch(number -> number > 0)
						   .allMatch(number -> number < 15)
						   .noneMatch(number -> number == 5);
		
		assertThat("").isEmpty();
		
		assertThat("ABCDE").startsWith("ABC")
						   .endsWith("DE");
		
	}
	
	

}
