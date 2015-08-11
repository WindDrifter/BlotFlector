package com.BoltFlector.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class DiffcultySettings implements Screen{
	Texture dropImage;
	Texture bucketImage;
	int rateSet=3;
	int spdSet=3;
	BoltFlector game;
	Rectangle rate;
	Rectangle spd;
	
	OrthographicCamera camera;
	public DiffcultySettings(final BoltFlector gam){
		game=gam;
	//	int xMax=Gdx.graphics.getWidth();
    //	int yMax=Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		spd = new Rectangle();
		spd.y= 350;
		spd.width=64;
		spd.height=64;
		rate = new Rectangle();
		rate.y = 250;
		rate.width=64;
		rate.height=64;
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
		game.font.draw(game.batch, "Speed: " + spdSet, 0, 480);
		game.font.draw(game.batch, "deflectRate: " + rateSet, 700, 480);
		
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.y>300 && touchPos.y<400){
				if(touchPos.x<(800-80)&&touchPos.x>0)
					spd.x=Math.min(Math.max(touchPos.x,16),600);
					spdChange();
			}
			else if(touchPos.y>200 && touchPos.y<400){
				if(touchPos.x<(800-80)&&touchPos.x>0)
					rate.x=Math.min(Math.max(touchPos.x,16),600);
					rateChange();
			}
			else if(touchPos.y<200){
				dispose();
				game.setScreen(new RegularGameMode(game,spdSet,rateSet));
			}
		}
		
		//game.font.draw(game.batch, "Speed pos: " + spd.x, 0, 50);
		//game.font.draw(game.batch, "rate pos: " + rate.x, 700, 50);

		game.batch.draw(bucketImage, spd.x, spd.y);
		game.batch.draw(dropImage, rate.x, rate.y);
		game.batch.end();
	}
	
	private void spdChange(){
		spdSet=(int) ((spd.x-16)/88);
		
		
	}
	
	private void rateChange(){
		rateSet=(int) ((rate.x-16)/88);
		
		
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
		dropImage.dispose();
		bucketImage.dispose();
	}
	
	
	
	
	
	
	
	
	
}
