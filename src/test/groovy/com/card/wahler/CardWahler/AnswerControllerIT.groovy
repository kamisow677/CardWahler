package com.card.wahler.CardWahler

import com.card.wahler.CardWahler.Answer.Answer
import com.card.wahler.CardWahler.Answer.AnswerRepository
import com.card.wahler.CardWahler.Pokerman.Pokerman
import com.card.wahler.CardWahler.Pokerman.PokermanRepository
import com.card.wahler.CardWahler.Round.Round
import com.card.wahler.CardWahler.Round.RoundRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockCustomUser(username = "keycloakUserId", roles = "user")
@ActiveProfiles("test")
class AnswerControllerIT extends Specification {

	private static final TASK_NAME_VALUE = "jira-1000"
	private static final TASK_NAME_KEY = "task"
	private static final NICK_VALUE = "minsc"
	private static final NICK_KEY = "nick"
	private static final KEYCLOAK_USER_ID = "keycloakUserId"

	@Autowired
	private MockMvc mvc;

	@Autowired
	private AnswerRepository answerRepository

	@Autowired
	private RoundRepository roundRepository

	@Autowired
	private PokermanRepository pokermanRepository

	def setup() {
		answerRepository.deleteAll()
		roundRepository.deleteAll()
		pokermanRepository.deleteAll()
	}

	def "when context is loaded then all expected beans are created"() {
		expect: "the WebController is created"
		answerRepository
	}

	def "should correctly save answer"() {
		given:
			roundRepository.save(Round.builder().taskName(TASK_NAME_VALUE).build())
			pokermanRepository.save(Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID).nick(NICK_VALUE).build())
		when:
			ResultActions action = mvc.perform(post("/api/answer/{points}", 13)
						.param(NICK_KEY, NICK_VALUE)
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
			roundRepository.save(round);
			def pokerman = Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID).nick(NICK_VALUE).build();
			pokermanRepository.save(pokerman)
			answerRepository.save(Answer.builder().pokerman(pokerman).round(round).points(13).build())
		when:
			ResultActions action = mvc.perform(put("/api/answer/{points}", 14)
					.param(NICK_KEY, NICK_VALUE)
					.param(TASK_NAME_KEY, TASK_NAME_VALUE)
			)
			action.andExpect(MockMvcResultMatchers.status().isOk())
		then:
			def answer = answerRepository.findAll()[0]
			assert answer.points == 14
	}

}
