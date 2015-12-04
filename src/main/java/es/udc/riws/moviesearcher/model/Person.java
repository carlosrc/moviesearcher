package es.udc.riws.moviesearcher.model;

public class Person {

	public enum TypePerson {
		CAST, DIRECTOR, WRITER
	};

	private String name;
	private String characterName;
	private int order;
	private TypePerson type;

	public Person(String name, String characterName, int order, TypePerson type) {
		super();
		this.name = name;
		this.characterName = characterName;
		this.order = order;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public TypePerson getType() {
		return type;
	}

	public void setType(TypePerson type) {
		this.type = type;
	}

}
