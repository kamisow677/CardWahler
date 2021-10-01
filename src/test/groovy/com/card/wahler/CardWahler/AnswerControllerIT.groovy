package com.card.wahler.CardWahler

import com.card.wahler.CardWahler.Answer.Answer
import com.card.wahler.CardWahler.Answer.AnswerDto
import com.card.wahler.CardWahler.Answer.AnswerRepository
import com.card.wahler.CardWahler.Pokerman.Pokerman
import com.card.wahler.CardWahler.Pokerman.PokermanRepository
import com.card.wahler.CardWahler.Round.Round
import com.card.wahler.CardWahler.Round.RoundRepository
import com.card.wahler.CardWahler.Session.infrastructure.SessionRepository
import com.card.wahler.CardWahler.utils.BaseIntegration
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@WithMockUser(username="jim",  roles=["USER","MANAGER"])
class AnswerControllerIT extends BaseIntegration {

	private static final TASK_NAME_VALUE = "jira-1000"
	private static final TASK_NAME_KEY = "task"
	private static final NICK_VALUE = "jim"
	private static final USER_ID = 12
	private static final ROUNDID_KEY = "roundId"

	@Autowired
	private AnswerRepository answerRepository

	@Autowired
	private RoundRepository roundRepository

	@Autowired
	private PokermanRepository pokermanRepository

	@Autowired
	private SessionRepository sessionRepository


	@BeforeEach
	def setup() {
		answerRepository.deleteAll()
		roundRepository.deleteAll()
		sessionRepository.deleteAll()
		pokermanRepository.deleteAll()
	}

	def "when context is loaded then all expected beans are created"() {
		expect: "the WebController is created"
		answerRepository
	}

	def "should correctly save answer"() {
		given:
			roundRepository.save(Round.builder().taskName(TASK_NAME_VALUE).build())
			pokermanRepository.save(Pokerman.builder().id(USER_ID).nick(NICK_VALUE).build())
		when:
			ResultActions action = mvc.perform(post("/api/answer/{points}", 13)
						.param(TASK_NAME_KEY, TASK_NAME_VALUE)
				)
				action.andExpect(MockMvcResultMatchers.status().isOk())
		then:
			def answer = answerRepository.findAll()[0]
			assert answer.points == 13

	}

	def "should correctly update answer"() {
		given:
			def round = Round.builder().taskName(TASK_NAME_VALUE).build()
			roundRepository.save(round)
			def pokerman = Pokerman.builder().nick(NICK_VALUE).build()
			pokermanRepository.save(pokerman)
			answerRepository.save(Answer.builder().pokerman(pokerman).round(round).points(13).build())
		when:
			ResultActions action = mvc.perform(put("/api/answer/{points}", 14)
					.param(TASK_NAME_KEY, TASK_NAME_VALUE)
			)
			action.andExpect(MockMvcResultMatchers.status().isOk())
		then:
			def answer = answerRepository.findAll()[0]
			assert answer.points == 14
	}

	def "should get answer"() {
		given:
			def round = Round.builder().taskName(TASK_NAME_VALUE).build()
			roundRepository.save(round)
			def pokerman = Pokerman.builder().nick(NICK_VALUE).build()
			pokermanRepository.save(pokerman)
			answerRepository.save(Answer.builder().pokerman(pokerman).round(round).points(13).build())
		when:
			def action = mvc.perform(get("/api/answer")
					.param(ROUNDID_KEY, round.getId().toString()))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn()
			String contentAsString = action.getResponse().getContentAsString()
			AnswerDto someClass = objectMapper.readValue(contentAsString, AnswerDto.class)
		then:
			assert someClass.getPoints() == 13
	}

}
