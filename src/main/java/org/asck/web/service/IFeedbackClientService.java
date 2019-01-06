package org.asck.web.service;

import java.util.List;

import org.asck.web.service.model.Answer;
import org.asck.web.service.model.AnswerReport;
import org.asck.web.service.model.Event;
import org.asck.web.service.model.Option;
import org.asck.web.service.model.Question;

public interface IFeedbackClientService {
	
	List<Event> leseAlleEvents();
	
	Event saveEvent(Event event);
	
	Event getEventById(Long id);
	
	List<Question> leseAlleFragenZuEvent(Long eventId);

	List<Option> leseAlleOptionenZuEinerFrage(Long eventId, Long questionId);
	
	Question readQuestion(Long eventId, Long questionId);
	
	Question saveQuestion(Long eventId, Question question);
	
	List<String> readAllSupportedQuestionTypes();
	
	void deleteEvent(Long eventId);
	
	void deleteQuestion(Long eventId, Long questionId);
	
	Answer saveAnswer(Answer answer);
	
	List<AnswerReport> getAllAnswersToEventId(Long eventId);
	
	Option findOptionById(Long optionId);

}
