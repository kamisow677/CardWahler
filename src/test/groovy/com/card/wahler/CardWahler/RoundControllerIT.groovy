package com.card.wahler.CardWahler

import com.card.wahler.CardWahler.Answer.AnswerRepository
import com.card.wahler.CardWahler.Pokerman.PokermanRepository
import com.card.wahler.CardWahler.Round.Round
import com.card.wahler.CardWahler.Round.RoundDto
import com.card.wahler.CardWahler.Round.RoundRepository
import com.card.wahler.CardWahler.Session.domain.Session
import com.card.wahler.CardWahler.Session.infrastructure.SessionRepository
import com.card.wahler.CardWahler.utils.BaseIntegration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@WithMockCustomUser(username = "keycloakUserId", roles = "user")
class RoundControllerIT extends BaseIntegration {

	private static final TASK_NAME_VALUE = "hira-1000"
	private static final TASK_NAME_VALUE2 = "hira-1001"
	private static final Integer SESSION_ID_VALUE = 1

	@Autowired
	private MockMvc mvc;

	@Autowired
	private AnswerRepository answerRepository

	@Autowired
	private RoundRepository roundRepository

	@Autowired
	private PokermanRepository pokermanRepository

	@Autowired
	private SessionRepository sessionRepository

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

	def "should correctly get current Round"() {
		given:
			def session = Session.builder().id(SESSION_ID_VALUE).password(TASK_NAME_VALUE).build()
			def sessionSaved = sessionRepository.save(session)
			Set rounds = [
					Round.builder().taskName(TASK_NAME_VALUE).isCurrent(false)
							.session(sessionSaved).build(),
					Round.builder().taskName(TASK_NAME_VALUE2).isCurrent(true)
							.session(sessionSaved).build(),
			]
			roundRepository.saveAll(rounds)

		when:
			def action = mvc.perform(get("/api/round/${SESSION_ID_VALUE}"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn()
		then:
			String contentAsString = action.getResponse().getContentAsString()
			RoundDto someClass = objectMapper.readValue(contentAsString, RoundDto.class)
			someClass.with {
				it.isCurrent = true
				it.taskName = TASK_NAME_VALUE
			}

	}


}
