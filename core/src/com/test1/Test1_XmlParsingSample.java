package com.test1;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Scanner;

public class Test1_XmlParsingSample extends ScreenAdapter implements InputProcessor{

	public Test1_XmlParsingSample() throws IOException {
		try {
			XmlReader reader = new XmlReader();
			XmlReader.Element root = reader.parse(Gdx.files.internal("data/credits.xml"));

			System.out.println("=========");
			System.out.println("Book data");
			System.out.println("=========");

			XmlReader.Element bookElement = root.getChildByName("Book");

			System.out.println("Title: " + bookElement.getText());
			System.out.println("Year: " + bookElement.getInt("year"));
			System.out.println("Number of pages: " + bookElement.get("pages"));

			bookElement.setAttribute("pages", "!!!!");

			System.out.println("Title: " + bookElement.getText());
			System.out.println("Year: " + bookElement.getInt("year"));
			System.out.println("Number of pages: " + bookElement.get("pages"));
			System.out.println("/////////////////////////////// " + bookElement.toString());

			Array<XmlReader.Element> authors = root.getChildrenByNameRecursively("Author");

			System.out.println("Authors: ");

			for (XmlReader.Element author : authors) {
				System.out.println("  * " + author.getText());
			}

			Array<XmlReader.Element> reviewers = root.getChildrenByNameRecursively("Reviewer");

			System.out.println("Reviewers: ");

			for (XmlReader.Element reviewer : reviewers) {
				System.out.println("  * " + reviewer.getText());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		StringWriter writer = new StringWriter();
		XmlWriter xml = new XmlWriter(writer);

		xml.element("AA").attribute("a1", "12").element("qq","qqqwww").text("qqq").pop();
//				xml.pop();
//              		xml.attribute("year", "12").pop();

		System.out.println("XMLWriter  : " + writer.toString());

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

