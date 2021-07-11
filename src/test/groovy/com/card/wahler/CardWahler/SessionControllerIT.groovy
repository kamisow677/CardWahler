package com.card.wahler.CardWahler

import com.card.wahler.CardWahler.Answer.AnswerRepository
import com.card.wahler.CardWahler.Pokerman.Pokerman
import com.card.wahler.CardWahler.Pokerman.PokermanRepository
import com.card.wahler.CardWahler.Round.RoundRepository
import com.card.wahler.CardWahler.Session.domain.Session
import com.card.wahler.CardWahler.Session.domain.SessionDto
import com.card.wahler.CardWahler.Session.infrastructure.SessionRepository
import com.card.wahler.CardWahler.utils.BaseIntegration
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Shared

import static org.junit.Assert.assertNotNull

class SessionControllerIT extends BaseIntegration {

	private static final PASSWORD_KEY = "password"
	private static final PASSWORD_1 = "password1"
	private static final PASSWORD_2 = "password2"
	private static final LINK_1 = "link1"
	private static final LINK_2 = "link2"

	@Shared
	private String accessToken = ""
	@Shared
	private String keycloakId = ""

	@Autowired
	private SessionRepository sessionRepository

	@Autowired
	private AnswerRepository answerRepository

	@Autowired
	private RoundRepository roundRepository

	@Autowired
	private PokermanRepository pokermanRepository


	private def initAccessToken() {
		def restTemplate = new RestTemplate()
		def headers = new HttpHeaders()
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED)

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>()
		map.add("client_id","voting-client-id")
		map.add("username","user1")
		map.add("password","password1")
		map.add("grant_type","password")
		map.add("client_secret","1698bbde-ebed-470a-bcf2-e72e57e66a24")

		HttpEntity<String> request =
				new HttpEntity<String>(map, headers)

		String personResultAsJsonStr =
				restTemplate.postForObject("http://localhost:8080/auth/realms/PokerVoting/protocol/openid-connect/token", request, String.class);
		accessToken = objectMapper.readTree(personResultAsJsonStr)["access_token"]
		accessToken = accessToken.replaceAll("\"","")
		String[] chunks = accessToken.split("\\.");
		Base64.Decoder decoder = Base64.getDecoder();
		String payload = new String(decoder.decode(chunks[1]));
		def value = objectMapper.readValue(payload, Map.class)
		keycloakId = value["sub"]
		accessToken = "Bearer " + objectMapper.readTree(personResultAsJsonStr)["access_token"]
		accessToken = accessToken.replaceAll("\"","")
	}

	def setup() {
		answerRepository.deleteAll()
		roundRepository.deleteAll()
		sessionRepository.deleteAll()
		pokermanRepository.deleteAll()
		if (accessToken == "")
			accessToken = initAccessToken()
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

			final String baseUrl = "http://localhost:"+randomServerPort+"/api/session/all"
			URI uri = new URI(baseUrl)
			HttpHeaders headers = new HttpHeaders()
			headers.add(HttpHeaders.AUTHORIZATION, accessToken)
		when:
			RestTemplate testRestTemplate = new RestTemplate()
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

	def "should get specified session"() {
		given:
			def sessions = [
					Session.builder().password(PASSWORD_2).link(LINK_2).pokermen(
							[Pokerman.builder().keycloakUserId(keycloakId).build()].toSet()
					).build(),
					Session.builder().password(PASSWORD_1).link(LINK_1).build(),
			]
			sessionRepository.save(sessions[0])
			sessionRepository.save(sessions[1])

			final String baseUrl = "http://localhost:"+randomServerPort+"/api/session";
			URI uri = new URI(baseUrl)
			HttpHeaders headers = new HttpHeaders()
			headers.add(HttpHeaders.AUTHORIZATION, accessToken)

		when:
			RestTemplate testRestTemplate = new RestTemplate()
			def response = testRestTemplate.
					exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), SessionDto[].class)
		then:
			assert HttpStatus.OK == response.getStatusCode()
			response.getBody()[0].with {
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

			URI uri = new URI(UriComponentsBuilder.newInstance()
					.scheme("http")
					.host("localhost")
					.port(randomServerPort)
					.path("/api/session/join")
					.queryParam(PASSWORD_KEY, PASSWORD_2).build().toUriString())
			HttpHeaders headers = new HttpHeaders()
			headers.add(HttpHeaders.AUTHORIZATION, accessToken)
			headers.setContentType(MediaType.APPLICATION_JSON)

		when:
			RestTemplate restTemplate = new RestTemplate()
			def response = restTemplate.
					exchange(uri, HttpMethod.POST,
							new HttpEntity<LinkedMultiValueMap<String, Object>>("", headers),
							SessionDto.class)
		then:
			assert HttpStatus.OK == response.getStatusCode()
			response.getBody().with {
				assert it.password == PASSWORD_2
				assert it.link == LINK_2
			}
	}

}
