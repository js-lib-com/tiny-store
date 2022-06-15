package js.tiny.store.bdd;

import java.util.List;

public interface IDatabase {

	void createPersons(List<String> names);

	void hasPerson(String name);

	int getRecordsCount();

	Object getRecord(int id);

}
