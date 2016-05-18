package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

import java.io.IOException;
import java.io.StringWriter;

public class Test1_JsonParsingSample extends ScreenAdapter implements InputProcessor{

	public static class Item {
		private  String name;
		private  int number;

		@Override
		public String toString() {
			return name + "(" + number + ")";
		}
	}

	public static class Character {
		private  String name = "";
		private  int experience = 0;
		private  int strength = 1;
		private  int dexterity = 1;
		private  int intelligence = 1;

		public Array<Item> items = new Array<Item>();

		@Override
		public String toString() {
			String string = new String();
			string += "Name: " + name + "\n";
			string += "Experience: " + experience + "\n";
			string += "Strength: " + strength + "\n";
			string += "Dexterity: " + dexterity + "\n";
			string += "Intelligence: " + intelligence + "\n";
			string += "Items: ";

			for (Item item : items) {
				string += item.toString() + " ";
			}

			return string;
		}
	}

	public Test1_JsonParsingSample() {

		System.out.println("======================");
		System.out.println("Reading character.json");
		System.out.println("======================");

		Json json = new Json();
		json.setElementType(Character.class, "items", Item.class);
		Character character = json.fromJson(Character.class, Gdx.files.internal("data/character.json"));
		System.out.println(character);

		System.out.println();

		System.out.println("=====================");
		System.out.println("Serializing character");
		System.out.println("=====================");

		System.out.println(json.prettyPrint(json.toJson(character)));
		System.out.println(json.toJson(character));

		JsonReader jj;
//		jj.parse

		JsonValue j;
//		j.asBoolean()

//		j.getBoolean()
		Gdx.app.exit();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}

