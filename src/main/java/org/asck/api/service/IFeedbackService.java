package org.asck.api.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.asck.api.exceptions.EntityNotFoundException;
import org.asck.api.service.model.Answer;
import org.asck.api.service.model.Event;
import org.asck.api.service.model.Option;
import org.asck.api.service.model.Question;
import org.asck.api.service.model.User;
import org.springframework.stereotype.Component;

public interface IFeedbackService {

	List<Event> findEvents();

	List<Event> findEventsOwnedBy(long ownedById);

	Event findEventById(Long id) throws EntityNotFoundException;

	Question findQuestion(Long eventId, Long questionId) throws EntityNotFoundException;

	Long saveAnswer(Answer answer) throws EntityNotFoundException;

	Long saveEvent(@Valid Event event);

	Long saveQuestion(@NotNull Long eventId, @Valid Question question) throws EntityNotFoundException;

	void deleteEvent(@NotNull Long eventId) throws EntityNotFoundException;

	void deleteQuestion(@NotNull Long eventId, @NotNull Long questionId) throws EntityNotFoundException;

	List<Answer> getAllAnswersToQuestion(long questionId) throws EntityNotFoundException;
	
	Option getOptionById(Long optionID) throws EntityNotFoundException;
	
	User getUserByEmail(String email) throws EntityNotFoundException;
	
	Long saveUser(@Valid User user);

}
