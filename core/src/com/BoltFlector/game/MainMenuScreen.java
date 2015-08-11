package com.BoltFlector.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;



public class MainMenuScreen implements Screen{

	final BoltFlector game;
	OrthographicCamera camera;

	public MainMenuScreen(final BoltFlector gam){
		game=gam;
		int xMax=Gdx.graphics.getWidth();
    	int yMax=Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);
		
		
		
	}

	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0,0,0.2f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.font.draw(game.batch,"Welcome to Drop",100,150);
		game.font.draw(game.batch, "Tap anywhere to start!",100,100);
		game.batch.end();
		
		if(Gdx.input.isTouched()){
			game.setScreen(new DiffcultySettings(game));

		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		camera.setToOrtho(false,800,480);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}