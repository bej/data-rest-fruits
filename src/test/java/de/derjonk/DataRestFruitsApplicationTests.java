package de.derjonk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataRestFruitsApplicationTests {

	private MockMvc mvc;
	private ObjectMapper mapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public final void initMockMvc() throws Exception {
		mvc = webAppContextSetup(webApplicationContext).build();
		mapper = new ObjectMapper();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testApiRoot() throws Exception {
		mvc.perform(get("/")).andExpect(status().isOk());
	}

	@Test
	public void testCreateApple() throws Exception {
		mvc.perform(post("/apples").content(json(new Apple("green apple"))))
				.andExpect(status().isCreated());
	}

	@Test
	public void testCreateAppleAsFruit() throws Exception {
		mvc.perform(post("/fruits").content(json(ImmutableMap.of("name", "apple as fruit", "type", "Apple"))))
				.andExpect(status().isCreated());
	}

	@Test
	public void testCreateBasket() throws Exception {
		mvc.perform(post("/baskets").content(json(new Basket("empty basket"))))
				.andExpect(status().isCreated());
	}

	@Test
	public void testCreateBasketAddItem() throws Exception {
		final MvcResult basketResult = mvc.perform(post("/baskets").content(json(new Basket("ToBeFilled"))))
				.andExpect(status().isCreated())
				.andReturn();

		final MvcResult basketResult2 = mvc.perform(get(basketResult.getResponse().getHeader(HttpHeaders.LOCATION)))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		final List<Map<String, Object>> items = JsonPath.parse(basketResult2.getResponse().getContentAsString()).read("$.items");
		assertThat(items).isEmpty();

		// POST apple to be added
		final MvcResult appleResult = mvc.perform(post("/apples").content(json(new Apple("yellow apple"))))
				.andExpect(status().isCreated())
				.andDo(print())
				.andReturn();

		// POST orange to be added
		final MvcResult orangeResult = mvc.perform(post("/oranges").content(json(new Orange("orange orange"))))
				.andExpect(status().isCreated())
				.andDo(print())
				.andReturn();

		{
			// PUT item collection
			final ImmutableMap<String, Object> data = ImmutableMap.of(
					"name", "ToBeFilled",
					"items", Arrays.asList(ImmutableMap.of(
							"fruit", appleResult.getResponse().getHeader(HttpHeaders.LOCATION)
					), ImmutableMap.of(
							"fruit", orangeResult.getResponse().getHeader(HttpHeaders.LOCATION)
					))
			);

			mvc.perform(put(basketResult.getResponse().getHeader(HttpHeaders.LOCATION))
					.content(json(data)))
					.andExpect(status().is2xxSuccessful())
					.andDo(print());

		}


		final MvcResult basketResult3 = mvc.perform(get(basketResult.getResponse().getHeader(HttpHeaders.LOCATION)))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		final String json = basketResult3.getResponse().getContentAsString();
		final List<Map<String, Object>> items2 = JsonPath.read(json, "$.items");
		assertThat(items2).isNotEmpty();
		assertThat(items2).hasSize(2);

		assertThat((String) JsonPath.read(json, "$.items[0]._embedded.fruit.name")).isEqualTo("yellow apple");
		assertThat((String) JsonPath.read(json, "$.items[1]._embedded.fruit.name")).isEqualTo("orange orange");
	}

	@Test
	public void testCreateAppleAndAddToBasket() throws Exception {
		final MvcResult basketResult = mvc.perform(post("/baskets").content(json(new Basket("grandmas pink basket"))))
				.andExpect(status().isCreated())
				.andReturn();

		{
			final MvcResult appleResult = mvc.perform(post("/apples").content(json(new Apple("red apple"))))
					.andExpect(status().isCreated())
					.andReturn();

			mvc.perform(put(basketResult.getResponse().getHeader(HttpHeaders.LOCATION) + "/fruits")
					.content(appleResult.getResponse().getHeader(HttpHeaders.LOCATION))
					.contentType("text/uri-list"))
					.andExpect(status().is2xxSuccessful());
		}

		final MvcResult basketResult2 = mvc.perform(get(basketResult.getResponse().getHeader(HttpHeaders.LOCATION) + "/fruits"))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		final List<Map<String, Object>> fruits = JsonPath.parse(basketResult2.getResponse().getContentAsString()).read("$._embedded.fruits");
		assertThat(fruits).isNotEmpty();
	}

	private byte[] json(Object object) throws JsonProcessingException {
		return mapper.writeValueAsBytes(object);
	}

}
