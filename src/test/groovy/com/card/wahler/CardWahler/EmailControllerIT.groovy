package com.card.wahler.CardWahler

import com.card.wahler.CardWahler.Answer.AnswerRepository
import com.card.wahler.CardWahler.Pokerman.PokermanRepository
import com.card.wahler.CardWahler.Round.RoundRepository
import com.card.wahler.CardWahler.Session.infrastructure.SessionRepository
import com.card.wahler.CardWahler.utils.BaseIntegration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import spock.lang.Shared

import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

@WithMockCustomUser(username = "keycloakUserId", roles = "user")
@AutoConfigureWireMock(port = 9000)
class EmailControllerIT extends BaseIntegration {

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
			stubFor(get(urlEqualTo("/api/email"))
					.willReturn(aResponse()
							.withStatus(200)
							.withStatusMessage("Everything was just fine!")
							.withHeader("Content-Type", "text/plain")));

			final String baseUrl = "http://localhost:"+randomServerPort+"/api/session/email"
			URI uri = new URI(baseUrl)
			HttpHeaders headers = new HttpHeaders()
			headers.add(HttpHeaders.AUTHORIZATION, accessToken)
		when:
			RestTemplate testRestTemplate = new RestTemplate()
			def response = testRestTemplate.
					exchange(uri, HttpMethod.POST, new HttpEntity<>(headers), String.class)
		then:
			assert response.getStatusCode() == HttpStatus.OK
	}

}
