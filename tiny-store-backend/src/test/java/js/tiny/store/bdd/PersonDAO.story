PersonDAO is a data service for Person entity. For the purpose of this story a Person entity has only id and name.

Narrative:

Scenario: Person delete by its id.

Given: database with a single record 
When: delete person with id 1
Then: database rows count is 0

Scenario: Person delete by not existing id does nothing.

Given: database with a single record 
When: delete person with id 2
Then: database rows count is 1

Scenario: Delete Person entity by its id.

Given: person table with two records with ids: 1, 2 
When: delete person entity with id 1
Then: person table records count is 1
And: person table does not have record with id 1
And: person table have record with id 2
