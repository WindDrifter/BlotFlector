package com.BoltFlector.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;


public class RegularGameMode implements Screen{
	int vect = 300;
	double vectX =100;
	double vectY=200;
	private TextureRegion region;
	final BoltFlector game;
	int boltOnField;
	private boolean gameOver = false;
	Texture[] Bolts;
	Texture regBolt,regBolt2,regBolt3, pauseButton;
	Texture chargeBolt,chargeBolt2;
	Texture barImage, backgroundImage;
	Sound Bounce;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle bar;
	Rectangle touchArea;
	long currentTime;
	Rectangle bolt;
	Random rand= new Random();
	int power;
	int score;
	int boltX=-1; // -1 means left 1 means right
	int boltY=-1; // -1 means down 1 means up
	int xMax=0;
	int yMax=0;
	int speed=0;
	int rate=0;
	private int boltStage=0;
	private boolean pause = false;
	private boolean boltCharged=false;
	private boolean justHitBar=false; // prevent exploit that gives multiple points
	
	/*
	private String currentMusic;
	private FileHandle dirHandle;
	private ArrayList<String> musicList = new ArrayList<String>();
	private Music bgMusic;
	private int currentNumber =0;
*/
	public RegularGameMode(BoltFlector gam, int speed, int rate){
		
		this.speed=speed;
		this.rate=rate;
		xMax=Gdx.graphics.getWidth();
		yMax=Gdx.graphics.getHeight();
		game=gam;
		score = 0;
		power = 0;
		boltCharged=false;
		boltStage=0;
		// load the images for the droplet and the bucket, 64x64 pixels each
		backgroundImage = new Texture(Gdx.files.internal("footer_lodyas.png"));
		region = new TextureRegion(backgroundImage, 0, 0, 800, 480);
		barImage = new Texture(Gdx.files.internal("bar.png"));
		regBolt = new Texture(Gdx.files.internal("UnchargedBoltV2.png"));
		regBolt2 = new Texture(Gdx.files.internal("UnChargedBolt2.png"));
		currentTime = TimeUtils.nanoTime();
		pauseButton = new Texture(Gdx.files.internal("pause.png"));
		chargeBolt = new Texture(Gdx.files.internal("chargedBoltV3.png"));
		chargeBolt2 = new Texture(Gdx.files.internal("chargedBoltV3_2.png"));
		Bolts = new Texture[]{regBolt,regBolt2,chargeBolt,chargeBolt2};
		// load the drop sound effect and the rain background "music"
		Bounce = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		
		vect = Math.min(300+(speed*150),1200);
		vectY=vect/2;
		vectX=vectY;
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, xMax, yMax);
		
		// create a Rectangle to logically represent the bucket
		bar = new Rectangle();
		bar.x = 800 / 2 - 100 / 2; // center the bucket horizontally
		bar.y = 20; // bottom left corner of the bucket is 20 pixels above
		// the bottom screen edge
		
		bar.width = 100;
		bar.height = 32;

		// create the raindrops array and spawn the first raindrop
		spawnBolt();
		resume();
		//getMusics();
	}



	public void spawnBolt(){
		if(boltOnField==0){
			bolt = new Rectangle();
			boltOnField++;}
		bolt.x = MathUtils.random(0, 700 - 30);
		bolt.y = 400;
		bolt.width = 30;
		bolt.height = 30;
		boltStage=0;
		resume();

	}



	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
	private void SwapColor(){
		if(!boltCharged&&!gameOver)
			switch(boltStage){		//bolt sprite changer
			case 0:{
				boltStage=1;
				break;}
			case 1:{
				boltStage=0;	
				break;}	
			default:{
				boltStage=0;	
				break;}
			}
		else if (!gameOver){
			if(boltStage==3){
				boltStage=2;
			}
			else{
				boltStage=3;
			}
		}
		currentTime= TimeUtils.nanoTime();
	}
	@Override
	public void render(float delta) {
		
		// tell the camera to update its matrices.
		camera.update();
		
		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);
		// begin a new batch and draw the bolts and the bar
		game.batch.begin();
		game.batch.draw(region,0,0);
		game.font.draw(game.batch, "Score: " + score, 700, 460);
		game.font.draw(game.batch, "power: " + power, 700, 480);

		if(TimeUtils.nanoTime()-currentTime>400000000){
			SwapColor();}

		game.batch.draw(Bolts[boltStage],Math.round(bolt.x), Math.round(bolt.y));
		game.batch.draw(barImage, bar.x, bar.y);
		game.batch.draw(pauseButton, 0,414);
		if(pause){
			if(!gameOver)
				game.font.draw(game.batch, "Game Paused", 400, 200);
			else
				game.font.draw(game.batch, "Game Over", 400, 200);
			game.batch.end();
			pause();
		}
		else if(!pause){
			game.batch.end();
			resume();
		}

	}

	private void addPower(){
		power= power +(100);
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		camera.setToOrtho(false,800,480);

	}
	public void quit(){
		dispose();
		game.setScreen(new MainMenuScreen(game));
		
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.y<300&&!gameOver){
				pause=false;}
			else if(touchPos.y >200 && touchPos.y<400 && gameOver){
				quit();
			}
		}
	}

	@Override
	public void resume() {
		//musicHandler();
		// TODO Auto-generated method stub
		pause=false;
		int chance = rand.nextInt(10);
		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.y<100){
				bar.x = touchPos.x - 100 / 2;}
			else if(touchPos.y>300 && touchPos.x<64){
				pause=true;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bar.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bar.x += 200 * Gdx.graphics.getDeltaTime();

		// make sure the bucket stays within the screen bounds
		if (bar.x < 0)
			bar.x = 0;
		if (bar.x > 800 - 100)
			bar.x = 800 - 100;

		// check if we need to create a new raindrop
		if (boltOnField==0)
			spawnBolt();

		bolt.y += Math.round(boltY)*vectY * Gdx.graphics.getDeltaTime();
		if(power<1000){

			bolt.x += Math.round(boltX)*vectX * Gdx.graphics.getDeltaTime();}
		else{ // once the power is charged it will start homing the bar
			if(bolt.x!=bar.x){
				if(bolt.x-bar.x<0)
					bolt.x += 200* Gdx.graphics.getDeltaTime();
				else
					bolt.x += -200* Gdx.graphics.getDeltaTime();
			}
		}
		if (bolt.y + 30 < 0){
			if(power<1000){
				boltOnField--;
				power =0;
				pause=true;
				gameOver=true;
				pause();
			}
			else{
				power=0;
				score=score+5000;
				vect+=150;
				spawnBolt();
			}
		}
		if(bolt.y>=400){
			boltY=-1;
			if(chance<rate){
				do{
					directionChangeY();}while(vectY<10);
				directionChangeX();}
			justHitBar = false;
		}
		if(bolt.x<0||bolt.x>750){
			justHitBar = false;
			if(bolt.x<0){ //preventing stuck on one side problem
				boltX=1;
			}
			else if(bolt.x>750){
				boltX=-1;
			}
			Bounce.play();
			if(chance<rate){
				do{
					directionChangeY();
				} while(vectY<10&&vectY>480);
				directionChangeX();}
		}

		if (bolt.overlaps(bar)) {
			if(power<1000 &&!justHitBar){
				justHitBar = true;
				score= score+(100*this.speed);
				if(chance<rate){
					do{directionChangeY();}
					while(vectY<10&&vectY>200);
					directionChangeX();}
				if(boltY<0)
					boltY= 1;
				addPower();}
			else if(power>=1000){
				score = score -1000;
				power =0;
				boltOnField--;
				rate++;
				spawnBolt();
			}
		}
		if(power>=1000){
			boltCharged=true;
		}
		else{
			boltCharged=false;
		}

	}
	public void directionChangeX(){
		vectX= Math.sqrt(Math.pow(vect,2)-Math.pow(vectY,2));
	}
	public void directionChangeY(){
		vectY = rand.nextInt(vect);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}



	@Override
	public void dispose() {
		barImage.dispose();
		Bounce.dispose();
		//rainMusic.dispose();
		//game.dispose();
		regBolt.dispose();
		regBolt2.dispose();

		chargeBolt.dispose();
		chargeBolt2.dispose();

	}

}
