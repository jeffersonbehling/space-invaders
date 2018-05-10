package game;
	
import java.awt.List;
import java.util.Iterator;
import java.util.LinkedList;

import api.FX_CG_2D_API;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SpaceInvaders extends Application{
	
	
	public static void main(String[] args) {
		launch(args);
	}
		
	@Override
	public void start(Stage cena){						
		Image spaceship = new Image(getClass().getResourceAsStream("../resource/spaceship.gif"));
		Image bullet = new Image(getClass().getResourceAsStream("../resource/bullet.png"));
		Image background = new Image(getClass().getResourceAsStream("../resource/bg.png"));
		Image alien = new Image(getClass().getResourceAsStream("../resource/alien.png"));
		Image explosion = new Image(getClass().getResourceAsStream("../resource/explosion.gif"));
		
		final int largura = 1024, altura = 600, fps = 100;
        final String nomeJogo = "Space Invaders";
        
        // Criacao do jogo
        FX_CG_2D_API jogo = new FX_CG_2D_API(fps, largura, altura) {
    
        	int x = -50;
        	int y = 170;
        	int xAlien = -50;
        	int yAlien = -300;
        	int vy = 5;        	        	
        	boolean left = false;
        	boolean alienLeft = true;
        	boolean alienRight = false;
        	boolean right = false;
        	boolean shoot = false;
        	boolean drawAlien = true;
        	boolean shooting = false;
        	LinkedList<Shoot> shoots = new LinkedList<Shoot>();
        	LinkedList<Alien> aliens = new LinkedList<Alien>();
        
        	// Metodo que desenha os objetos do jogo. Chamado continuamente.
			@Override
			public void desenhar() {

                retangulo(0, 0, largura, altura, Estilo.PREENCHIDO);
                imagem(background, 0, 0);
                          
                empilhar();
                	transladar(largura/2, altura/2);
                	contorno(3, Color.BLUE);
                	preenchimento(Color.BLUE);
                	
                	imagem(spaceship, x, y);
                
                	for (Alien a : aliens) {
                		imagem(alien, a.x, a.y);	
                	}
                                         	                	
                	if(shooting) {
                		empilhar();
                		contorno(3, Color.RED);
                    	preenchimento(Color.RED);
                    	
                    	for (Shoot shoot : shoots) {
                    		imagem(bullet, shoot.x+4, shoot.y-10);
                    		boolean colidiu = false;
                    		for (Alien a : aliens) {
                    			if (colisao(shoot.x+4, shoot.y-10, a.x, a.y, 24, 24, 64, 64)) {
                        			colidiu = true;
                        			aliens.remove(a);
                        			shoots.remove(shoot);
                        			imagem(explosion, a.x, a.y);
                        			
                        			break;
                        		}
                    		}
                    		
                    		if (colidiu) {
                    			break;
                    		}
                    		
						}                		
                		desempilhar();
                	}
                desempilhar();
                
			}

            // Metodo chamado continuamente no loop do jogo. 
			// Usado pra atualizar valores e fazer calculos de posicoes, etc...
			@Override
			public void atualizar() {
					if(shoot) {							
						Shoot a = new Shoot();						
            			a.x = x+37;
            			a.y = y;            			
            			shoots.add(a);            			
            			shoot = false;            			
            			shooting = true;            			
					}
					
				   if(left && x >= -512){
				        x -= 2;
				    }
				   
				    if(right && x <= 408){
				        x += 2;
				        
				    }
				    
				    if(shooting) {
				    	for (Shoot shoot : shoots) {
							shoot.y -= vy;
						}				    
				    }
				    
				    if (drawAlien) {
					    for (int i = -300; i < 400;) {
			        		Alien a = new Alien();
							a.x = i;
							i+= 50;
							a.y = yAlien;
							aliens.add(a);
							i += 100;
							
			        	}
				    }
				    
				    drawAlien = false;
				    
				    for (Alien a : aliens) {
				    	if (alienLeft) {
				    		a.x -= 1;
				    		if (aliens.getFirst().x == -512) {
				    			alienLeft = false;
					    		alienRight = true;
					    	}
				    	}
				    	
				    	if (alienRight) {
				    		a.x += 1;
				    		if (aliens.getLast().x == 450) {
				    			alienLeft = true;
					    		alienRight = false;
					    	}
				    	}
				    	
				    }
				    
				    if (aliens.size() == 0) {
				    	// Termina o Jogo
				    }
				    
			}
			
			@Override
			public void movimentoDoMouse(MouseEvent e) { }
			
			@Override
			public void cliqueDoMouse(MouseEvent e) { }
			
			@Override
			public void teclaDigitada(KeyEvent e) {	
				
			}
			
			@Override
			public void teclaLiberada(KeyEvent e) {	
				if(e.getCode() == KeyCode.RIGHT){
					right = false;
				}
				
				if(e.getCode() == KeyCode.LEFT){
					left = false;
				}
						
				if(e.getCode() == KeyCode.SPACE){
					shoot = false;					
					
				}
			}
			
			@Override
			public void teclaPressionada(KeyEvent e) { 
				if(e.getCode() == KeyCode.RIGHT){
					right = true;
					
				}
				
				if(e.getCode() == KeyCode.LEFT){
					left = true;
				}
				
				if(e.getCode() == KeyCode.SPACE){
					shoot = true;
				}
								
			}
			
		};
			    
		// Inicia o jogo
		jogo.iniciar(cena, nomeJogo);
	}

	public class Shoot {
		public int x;
		public int y;
	}
	
	public class Alien {
		public int x;
		public int y;
	}
}