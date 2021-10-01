package com.card.wahler.CardWahler

import com.card.wahler.CardWahler.Answer.AnswerRepository
import com.card.wahler.CardWahler.Pokerman.PokermanRepository
import com.card.wahler.CardWahler.Round.RoundRepository
import com.card.wahler.CardWahler.Session.infrastructure.SessionRepository
import com.card.wahler.CardWahler.utils.BaseIntegration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WithMockUser(username="jim",  roles=["USER","MANAGER"])
@AutoConfigureWireMock(port = 9000)
class EmailControllerIT extends BaseIntegration {

	@Autowired
	private SessionRepository sessionRepository

	@Autowired
	private AnswerRepository answerRepository

	@Autowired
	private RoundRepository roundRepository

	@Autowired
	private PokermanRepository pokermanRepository

	def setup() {
		answerRepository.deleteAll()
		roundRepository.deleteAll()
		sessionRepository.deleteAll()
		pokermanRepository.deleteAll()
	}

	def "when context is loaded then all expected beans are created"() {
		expect: "the WebController is created"
			sessionRepository
	}

	def "should send mail"() {
		given:
			stubFor(get(urlEqualTo("/api/email"))
					.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "text/plain")
							.withBody("Everything was just fine!")))

			final String baseUrl = "/api/session/email"
		when:
			def action = mvc.perform(post(baseUrl))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn()
		then:
			assert action.getResponse().getContentAsString() == "Everything was just fine!"
	}

}
