package com.card.wahler.CardWahler

import com.card.wahler.CardWahler.Answer.AnswerRepository
import com.card.wahler.CardWahler.Pokerman.Pokerman
import com.card.wahler.CardWahler.Pokerman.PokermanRepository
import com.card.wahler.CardWahler.Round.RoundDto
import com.card.wahler.CardWahler.Round.RoundRepository
import com.card.wahler.CardWahler.Session.domain.Session
import com.card.wahler.CardWahler.Session.domain.SessionDto
import com.card.wahler.CardWahler.Session.infrastructure.SessionRepository
import com.card.wahler.CardWahler.utils.BaseIntegration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Shared

import static org.junit.Assert.assertNotNull
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WithMockUser(username="jim",  roles=["USER","MANAGER"])
class SessionControllerIT extends BaseIntegration {

	private static final PASSWORD_KEY = "password"
	private static final PASSWORD_1 = "password1"
	private static final PASSWORD_2 = "password2"
	private static final NICK = "jim"
	private static final LINK_1 = "link1"
	private static final LINK_2 = "link2"

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

	def "should get all sessions"() {
		given:
			def sessions = [
					Session.builder().password(PASSWORD_1).link(LINK_1).build(),
					Session.builder().password(PASSWORD_2).link(LINK_2).build(),
			]
			sessionRepository.save(sessions[0])
			sessionRepository.save(sessions[1])

			final String baseUrl = "/api/session/all"
		when:
			def action = mvc.perform(get(baseUrl))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn()
			String contentAsString = action.getResponse().getContentAsString()
			SessionDto[] someClass = objectMapper.readValue(contentAsString, SessionDto[].class)
		then:
			assert someClass.size() == 2
			someClass.eachWithIndex { SessionDto entry, int i ->
				assertNotNull(entry.password)
				assertNotNull(entry.link)
			}
	}

	def "should get specified session"() {
		given:
			def sessions = [
					Session.builder().password(PASSWORD_2).link(LINK_2).pokermen(
							[Pokerman.builder().nick(NICK).build()].toSet()
					).build(),
					Session.builder().password(PASSWORD_1).link(LINK_1).build(),
			]
			sessionRepository.save(sessions[0])
			sessionRepository.save(sessions[1])

			final String baseUrl = "/api/session";
//			URI uri = new URI(baseUrl)
//			HttpHeaders headers = new HttpHeaders()
//			headers.add(HttpHeaders.AUTHORIZATION, accessToken)

		when:
			def action = mvc.perform(get(baseUrl))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn()
			String contentAsString = action.getResponse().getContentAsString()
			SessionDto[] someClass = objectMapper.readValue(contentAsString, SessionDto[].class)
		then:
			someClass[0].with {
				assert it.password == PASSWORD_2
				assert it.link == LINK_2
			}
	}

	def "should join session"() {
		given:
			def sessions = [
					Session.builder().password(PASSWORD_2).link(LINK_2).build(),
					Session.builder().password(PASSWORD_1).link(LINK_1).build(),
			]
			sessionRepository.save(sessions[0])
			sessionRepository.save(sessions[1])

		when:
			def action = mvc.perform(post("/api/session/join")
					.param(PASSWORD_KEY, PASSWORD_2))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn()
			String contentAsString = action.getResponse().getContentAsString()
			SessionDto someClass = objectMapper.readValue(contentAsString, SessionDto.class)
		then:
			someClass.with {
				assert it.password == PASSWORD_2
				assert it.link == LINK_2
			}
	}

}
