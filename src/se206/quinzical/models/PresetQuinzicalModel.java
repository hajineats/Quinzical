package se206.quinzical.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import se206.quinzical.models.util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * PresetQuinzicalModel contains categories, questions, score & other game info for the main mode.
 *
 * @author Hajin Kim
 */
public class PresetQuinzicalModel extends QuizModel {
	private final IntegerProperty _score = new SimpleIntegerProperty();
	List<Category> _fiveCategoriesWithFiveQuestions;

	public PresetQuinzicalModel(QuinzicalModel qdb) {
		super(qdb);

		_fiveCategoriesWithFiveQuestions = new ArrayList<>();

		List<Category> categories = selectFiveCategories(qdb);
		for (Category category : categories) {
			Category newCategory = selectFiveQuestions(category);
			_fiveCategoriesWithFiveQuestions.add(newCategory);
		}
	}

	@Override
	public void answerQuestion(String answer) {
		if (getState() != State.ANSWER_QUESTION) {
			throw new IllegalStateException("Previous state should be ANSWER_QUESTION, found " + getState());
		}
		// boolean correct = getCurrentQuestion().checkAnswer(answer);
		boolean correct = true;
		setState(correct ? State.CORRECT_ANSWER : State.INCORRECT_ANSWER);
	}

	public int getScore() {
		return _score.get();
	}

	public IntegerProperty getScoreProperty() {
		return _score;
	}

	/*
	 * Below are utility methods to select 5 categories/questions and return List<Category> / Category
	 */

	public List<Category> selectFiveCategories(QuinzicalModel qdb) {
		if (qdb._categories.size() == 0) {
			return new ArrayList<>();
		}

		List<Integer> fiveNumbers = RandomNumberGenerator.takeFive(qdb._categories.size());
		List<Category> result = new ArrayList<>();

		for (Integer n : fiveNumbers) {
			// get nth category from the categories set, and add to the preset categories
			result.add(qdb._categories.get(n));
		}
		return result;
	}

	public Category selectFiveQuestions(Category c) {
		List<Integer> fiveNumbers = RandomNumberGenerator.takeFive(c._questions.size());
		List<Question> fiveQuestions = new ArrayList<>();

		for (Integer n : fiveNumbers) {
			// get nth question from the questions of the category, and add to the list
			fiveQuestions.add(c._questions.get(n));
		}
		return new Category(fiveQuestions, c._name, true);
	}
}
