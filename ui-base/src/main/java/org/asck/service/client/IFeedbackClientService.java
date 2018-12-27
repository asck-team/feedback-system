package org.asck.service.client;

import java.util.List;

import org.asck.service.client.model.Event;
import org.asck.service.client.model.Option;
import org.asck.service.client.model.Question;

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
	
	

}
