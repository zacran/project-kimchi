package com.zacran.kimchi.cucumber;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zacran.kimchi.model.Kimchi;
import com.zacran.kimchi.model.KimchiRating;
import com.zacran.kimchi.rules.RulesEngine;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CucumberStepDefinitions {

	private RulesEngine rulesEngine;
	private Kimchi kimchi;
	private KimchiRating kimchiRating;

	@Before
	public void setup() {
		rulesEngine = RulesEngine.getInstance();
		kimchi = new Kimchi();
	}

	@Given("I have the following kimchi:")
	public void iHaveTheFollowingIngredients(DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		for (int i = 0; i < rows.size(); i++) {
			Map<String, String> row = rows.get(i);

			List<String> ingredients = new ArrayList<>(Arrays.asList(row.get("Ingredients").split(",")));
			int age = Integer.valueOf(row.get("Age"));

			kimchi = Kimchi.builder().ingredients(ingredients).age(age).rating(KimchiRating.TASTES_GREAT).build();
			log.debug("Testing Kimchi: " + kimchi.toString());
		}
	}

	@When("I attempt to make Kimchi")
	public void iAttemptToMakeKimchi() {
		rulesEngine.runRules(kimchi);
		kimchiRating = kimchi.getRating();
	}

	@Then("The Kimchi will taste great!")
	public void theKimchiWillTasteGreat() {
		assertEquals("Expected kimchi to taste great but it doesn't!", KimchiRating.TASTES_GREAT, kimchiRating);
	}

	@Then("It will not be Kimchi!")
	public void itWillNotBeKimchi() {
		assertEquals("Expected this to not be kimchi but it is!", KimchiRating.NOT_KIMCHI, kimchiRating);
	}

	@Then("The Kimchi will not taste great.")
	public void theKimchiWillNotTasteGreat() {
		assertEquals("Expected kimchi to not taste good but it does!", KimchiRating.TASTES_BAD, kimchiRating);
	}
}