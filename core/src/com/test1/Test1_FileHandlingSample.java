package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Enumeration;

public class Test1_FileHandlingSample extends ScreenAdapter implements InputProcessor{
	enum A{a,b,c}

	public Test1_FileHandlingSample() {
		System.out.println("======================");
		System.out.println("File basic information");
		System.out.println("======================");

//		Object[][] o = {{"aa"}, null};//new Object[3];
//		String[] o2 = new String[]{"oaa", null};//new Object[3];
////		o = new String[]{"aa", null};
////		o[0] = "abc";
//		o[0] = new Object[]{"1", "a"};
//		o[1]= new Object[]{"1"};
////		o = o2;
//		System.out.println(Arrays.toString(o[0]));
//		System.out.println(Arrays.toString(o[1]));
//
//		ArrayList<Object> l = new ArrayList<>();
//		ArrayList l2 = new ArrayList<>();
//
//		l = l2;
//

		A x = A.a;

		switch(11){
			case 0:
				System.out.println("====================== 0");
				break;
			case 1:
				System.out.println("====================== 1");
//				break;
			default:
				System.out.println("====================== default");
//				break;
			case 2:
				System.out.println("====================== 2");
				break;
			case 3:
				System.out.println("====================== 3");
//				break;
		}

		byte[] bytes = new byte[]{'b', 'a', 'l', 'l'};
		FileHandle a1 = Gdx.files.external("ss.txt");
		a1.writeString("AAA", true);
		FileHandle a2 = Gdx.files.external("sbs");
		a2.writeBytes(bytes, true);

		FileHandle file = Gdx.files.internal("data/sfx/sfx_01.wav");
		FileHandle dir = Gdx.files.internal("data/sfx");

//		file.tempFile()

		printHandleInfo(file);
		printHandleInfo(dir);

		System.out.println("");
		System.out.println("=================");
		System.out.println("Directory listing");
		System.out.println("=================");
		try {
			printTree(Gdx.files.internal("data"), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("");
		System.out.println("==============");
		System.out.println("File existence");
		System.out.println("==============");
		FileHandle doesNotExist = Gdx.files.internal("file.txt");
		System.out.println(doesNotExist.name() + " " + (doesNotExist.exists() ? "does exist" : "does not exist"));
		FileHandle doesExist = Gdx.files.internal("data/sfx/sfx_01.wav");
		System.out.println(doesExist.name() + " " + (doesExist.exists() ? "does exist" : "does not exist"));

		System.out.println("");
		System.out.println("==================================");
		System.out.println("Write, read, copy and delete files");
		System.out.println("==================================");
		String testString = "This is a test file";
		FileHandle testFile = Gdx.files.external("test.txt");
		testFile.writeString(testString, false);
		System.out.println("Wrote '" + testString + "' to file " + testFile.name());

		FileHandle testFile2 = Gdx.files.external("test.txt");
		String string = testFile2.readString();
		System.out.println(string.equals(testString) ? "Successfully wrote and read file" : "Failed to read or write file");

		FileHandle testCopy = Gdx.files.external("test-copy.txt");
		System.out.println("Copying " + testFile.name() + " to " + testCopy.name());
		testFile.copyTo(Gdx.files.external("test-copy.txt"));
		string = testCopy.readString();
		System.out.println(string.equals(testString) ? "Successfully copied file" : "Failed to copy file");

		System.out.println("Deleting test files");
		testFile.delete();
		testCopy.delete();
		System.out.println(!testFile.exists() && !testCopy.exists() ? "Successfully deleted files" : "Failed to delete files");

		Gdx.app.exit();
	}

	private void printHandleInfo(FileHandle handle) {
		System.out.println(handle.isDirectory() ? "Directory info" : "File info");
		System.out.println("Name: " + handle.name());
		System.out.println("Name without extension: " + handle.nameWithoutExtension());
		System.out.println("Extension: " + handle.extension());
		System.out.println("Last modified in ms " + handle.lastModified());
		System.out.println("Path " + handle.path());
		System.out.println("Path without extension " + handle.pathWithoutExtension());

		if (!handle.isDirectory()) {
			System.out.println("Size: " + handle.length() + " bytes");
		}

		System.out.println();
	}

	private void printTree(FileHandle handle, int level) {

		addPadding(level);
		System.out.println("|- " + handle.name());

		if (handle.isDirectory()) {
			FileHandle[] children = handle.list();

			for (FileHandle child : children) {
				printTree(child, level + 1);
			}

			addPadding(level + 1);
			System.out.println("\\");
			addPadding(level + 1);
			System.out.println();
		}
	}

	private void addPadding(int level) {
		for (int i = 0; i < level; ++i) {
			System.out.print("|    ");
		}
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

