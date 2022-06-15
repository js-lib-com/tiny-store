package js.tiny.store.bdd;

import java.util.List;

public class PersonDAO_deleteByName {
	private IDatabase database;
	private IPersonDAO personDAO;

	@Given("database with two person names: $names")
	public void databaseRecords(List<String> names) {
		database.createPersons(names);
	}

	@When("delete person by $name")
	public void deleteByName(String name) {
		personDAO.deleteByName(name);
	}

	@Then("database records count is $count")
	public void databaseRecordsCount(int count) {
		Assert.equalsTo(database.getRecordsCount(), count);
	}

	@And("database have person with id $id")
	public void existingPerson(int id) {
		Assert.notNullValue(database.getRecord(id));
	}

	@And("database does not have person with id $id")
	public void notExistingPerson(int id) {
		Assert.nullValue(database.getRecord(id));
	}
}
