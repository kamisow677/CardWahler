package com.card.wahler.CardWahler

import com.card.wahler.CardWahler.Answer.AnswerRepository
import com.card.wahler.CardWahler.Pokerman.Pokerman
import com.card.wahler.CardWahler.Pokerman.PokermanRepository
import com.card.wahler.CardWahler.Round.RoundRepository
import com.card.wahler.CardWahler.Session.infrastructure.SessionRepository
import com.card.wahler.CardWahler.utils.BaseIntegration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.server.ResponseStatusException

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.junit.Assert.assertNotNull

@WithMockCustomUser(username = "keycloakUserId", roles = "user")
class PokermanControllerIT extends BaseIntegration {

	private static final NICK_KEY = "nick"
	private static final KEYCLOAK_KEY = "keycloakUserId"
	private static final NICK_VALUE_1 = "minsc"
	private static final NICK_VALUE_2 = "john"
	private static final BAD_REQUEST_NICK_VALUE = "bad nick"
	private static final KEYCLOAK_USER_ID_1 = "keycloakUserId"
	private static final KEYCLOAK_USER_ID_2 = "keycloakUserId2"

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
		pokermanRepository
	}

	def "should get all pokermans"() {
		given:
			def pokermans = [
					Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID_1).nick(NICK_VALUE_1).build(),
					Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID_2).nick(NICK_VALUE_2).build()
			]
			pokermanRepository.save(pokermans[0])
			pokermanRepository.save(pokermans[1])
		when:
			 def action = mvc.perform(get("/api/pokerman/all"))
					.andReturn()
		then:
			String contentAsString = action.getResponse().getContentAsString()
			Pokerman[] someClass = objectMapper.readValue(contentAsString, Pokerman[].class)
			someClass.toList().each {
				Pokerman entry ->
					assertNotNull(entry.nick)
					assertNotNull(entry.keycloakUserId)
			}
	}

	def "should get get pokerman"() {
		given:
			def pokermans = [
					Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID_1).nick(NICK_VALUE_1).build(),
					Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID_2).nick(NICK_VALUE_2).build()
			]
			pokermanRepository.saveAll(pokermans)
		when:
			def action = mvc.perform(get("/api/pokerman")
					.param(NICK_KEY, NICK_VALUE_2)
					.param(KEYCLOAK_KEY, KEYCLOAK_USER_ID_2))
					.andReturn()
		then:
			String contentAsString = action.getResponse().getContentAsString()
			Pokerman pokerman = objectMapper.readValue(contentAsString, Pokerman.class)
			pokerman.with {
				assert it.nick == pokermans[1].nick
				assert it.keycloakUserId == pokermans[1].keycloakUserId
			}
	}

	@WithMockCustomUser(username = "keycloakUserId2", roles = "user")
	def "should get get pokerman based on principal keycloak id"() {
		given:
			def pokermans = [
					Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID_1).nick(NICK_VALUE_1).build(),
					Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID_2).nick(NICK_VALUE_2).build()
			]
			pokermanRepository.saveAll(pokermans)
		when:
			def action = mvc.perform(get("/api/pokerman"))
					.andReturn()
		then:
			String contentAsString = action.getResponse().getContentAsString()
			Pokerman pokerman = objectMapper.readValue(contentAsString, Pokerman.class)
			pokerman.with {
				assert it.nick == pokermans[1].nick
				assert it.keycloakUserId == pokermans[1].keycloakUserId
			}
	}

	def "should get 400 if bad params "() {
		given:
			def pokermans = [
					Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID_1).nick(NICK_VALUE_1).build(),
					Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID_2).nick(NICK_VALUE_2).build()
			]
			pokermanRepository.saveAll(pokermans)
		when:
			def andReturn = mvc.perform(MockMvcRequestBuilders.get("/api/pokerman")
					.param(NICK_KEY, BAD_REQUEST_NICK_VALUE))

		then:
			andReturn
					.andExpect(status().isBadRequest())
					.andExpect({ result ->
						assertTrue(result.getResolvedException() instanceof ResponseStatusException)
					})
					.andExpect({ result ->
						assertEquals("400 BAD_REQUEST \"no user for Optional[${BAD_REQUEST_NICK_VALUE}]\"".toString(), result.getResolvedException().getMessage().toString())
					})
	}

	def "should post pokerman"() {
		given:
			def pokermans = [
					Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID_1).nick(NICK_VALUE_1).build(),
					Pokerman.builder().keycloakUserId(KEYCLOAK_USER_ID_2).nick(NICK_VALUE_2).build()
			]
			pokermanRepository.saveAll(pokermans)

			def ow = objectMapper.writer().withDefaultPrettyPrinter();
			String requestJson=ow.writeValueAsString(pokermans[0]);
		when:
			mvc.perform(post("/api/pokerman")
					.content(requestJson))
					.andReturn()
		then:
			assert pokermans[0].nick == pokermanRepository.findById(KEYCLOAK_USER_ID_1).get().nick
			assert pokermans[0].keycloakUserId == pokermanRepository.findById(KEYCLOAK_USER_ID_1).get().keycloakUserId
			assert pokermans[0].image == pokermanRepository.findById(KEYCLOAK_USER_ID_1).get().image
	}

}
