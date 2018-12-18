package com.feedback.service.client;

import java.util.List;

import com.feedback.service.client.model.Answer;
import com.feedback.service.client.model.Event;
import com.feedback.service.client.model.Option;
import com.feedback.service.client.model.Question;

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
	
	

}
