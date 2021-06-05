package com.card.wahler.CardWahler

import com.card.wahler.CardWahler.Answer.AnswerRepository
import com.card.wahler.CardWahler.Pokerman.Pokerman
import com.card.wahler.CardWahler.Pokerman.PokermanRepository
import com.card.wahler.CardWahler.Round.RoundRepository
import com.card.wahler.CardWahler.Session.domain.Session
import com.card.wahler.CardWahler.Session.domain.SessionDto
import com.card.wahler.CardWahler.Session.infrastructure.SessionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification

import static org.junit.Assert.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockCustomUser(username = "keycloakUserId", roles = "user")
@ActiveProfiles("test")
class SessionControllerIT extends Specification {

	private static final NICK_KEY = "nick"
	private static final KEYCLOAK_KEY = "keycloakUserId"
	private static final NICK_VALUE_1 = "minsc"
	private static final NICK_VALUE_2 = "john"
	private static final BAD_REQUEST_NICK_VALUE = "bad nick"
	private static final PASSWORD_1 = "password1"
	private static final PASSWORD_2 = "password2"
	private static final LINK_1 = "link1"
	private static final LINK_2 = "link2"

	@Autowired
	private MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper

	@Autowired
	private SessionRepository sessionRepository

	@LocalServerPort
	int randomServerPort;

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
		sessionRepository
	}

	def "should get all session"() {
		given:
			def sessions = [
					Session.builder().password(PASSWORD_1).link(LINK_1).build(),
					Session.builder().password(PASSWORD_1).link(LINK_1).build(),
			]
			sessionRepository.save(sessions[0])
			sessionRepository.save(sessions[1])
			final String baseUrl = "http://localhost:"+randomServerPort+"/api/session/all";
			URI uri = new URI(baseUrl);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ4X0xYcnlCcXhlNThYb0prOVpvTzc3N0w1ZU1pYmFpZ2hfRWp6Y2RBbXdvIn0.eyJleHAiOjE2MjI0MTI5MjYsImlhdCI6MTYyMjQwOTMyNiwianRpIjoiNmVhZTM0YjYtY2ViNi00OTVhLWExMDgtODA1NDU3ZTE4ZGJjIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL1Bva2VyVm90aW5nIiwiYXVkIjpbImZyb250ZW5kLXBva2VyLWlkIiwiYWNjb3VudCJdLCJzdWIiOiIxZGVhODZkZS0xYThkLTRkYTgtYjlkZC0xNmVlNDkwYzc3MDciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ2b3RpbmctY2xpZW50LWlkIiwic2Vzc2lvbl9zdGF0ZSI6ImIxZDk4MTMxLTkyYmUtNGM3OS04ZjJlLTZhYTc1YmUxYjk0ZCIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDozMDAwLyIsIioiLCJodHRwOi8vbG9jYWxob3N0OjMwMDAiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsidm90aW5nLWNsaWVudC1pZCI6eyJyb2xlcyI6WyJjbGllbnQtdXNlciJdfSwiZnJvbnRlbmQtcG9rZXItaWQiOnsicm9sZXMiOlsiY2xpZW50LXVzZXIiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkVyd2luIEVsb2lzIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlcjMiLCJnaXZlbl9uYW1lIjoiRXJ3aW4iLCJmYW1pbHlfbmFtZSI6IkVsb2lzIn0.PH_6nKehJ5qVa6pQ314qfQz8BWuJQiWbXKiHuYCyuUnxsqS2XCrkcm90123_NLGWrRU0nONqi1ZNPcm8gcsWpf0k6Na9iU_1ymIeg-gRLVhf--FUdqYpooFgY0ZsX_4uDHoX09VowhLpYgrJ14JuINDe62ssdcq0LeLxc9Te8xDs9CUchmTBOW5WHf_rgG4jmm-V2yKKXtg9ZvJ4lThBbfnulgWWaU7Jc98C5OXHUXimz9b0dQmF3ogK2lWlncuADf_LgB3k3zFW9h6m9WWhUoxGiCUMz2dXit_eej9sRS1MU4VxZJTadE0Qf-hjPbReog0OZjPwrbc-bVHLnBkR1A");
		when:

			TestRestTemplate testRestTemplate = new TestRestTemplate("user",
					"passwd", TestRestTemplate.HttpClientOption.ENABLE_COOKIES);
			def response = testRestTemplate.
					exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), SessionDto[].class)
		then:
			assert HttpStatus.OK == response.getStatusCode()
			assert response.getBody().size() == 2
			response.getBody().eachWithIndex { SessionDto entry, int i ->
				assertNotNull(entry.password)
				assertNotNull(entry.link)
			}
	}



}
