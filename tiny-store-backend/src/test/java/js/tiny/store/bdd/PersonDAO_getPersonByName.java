package js.tiny.store.bdd;

public class PersonDAO_getPersonByName {
	private IPersonDAO personDAO;
	
	@Given("a person DAO instance")
	public void aPersonDAO() {
	}
	
	@When("get person by $name")
	public void getPersonByName(String name) {
	}
	
	@Then("the person id is $id")
	public void thePersonIdIs(int id) {
	}
}
