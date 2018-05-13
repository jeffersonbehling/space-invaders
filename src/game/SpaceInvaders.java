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
	public void start(Stage cena) {	
		Image spaceshipImage = new Image(getClass().getResourceAsStream("../resource/spaceship.gif"));
		Image spaceshipImage1 = new Image(getClass().getResourceAsStream("../resource/spaceship1.gif"));
		Image spaceshipImage2 = new Image(getClass().getResourceAsStream("../resource/spaceship2.gif"));
		Image spaceshipImage3 = new Image(getClass().getResourceAsStream("../resource/spaceship3.gif"));
		Image bullet = new Image(getClass().getResourceAsStream("../resource/bullet.png"));
		Image background = new Image(getClass().getResourceAsStream("../resource/background.gif"));
		Image alienImage = new Image(getClass().getResourceAsStream("../resource/alien.gif"));
		Image explosion = new Image(getClass().getResourceAsStream("../resource/explosion.gif"));
		Image fireAlien = new Image(getClass().getResourceAsStream("../resource/fireAlien.gif"));
		Image gameOver = new Image(getClass().getResourceAsStream("../resource/GameOver.png"));
		Image youWin = new Image(getClass().getResourceAsStream("../resource/YouWin.png"));
		Image livesImage = new Image(getClass().getResourceAsStream("../resource/heart.png"));
		
		final int largura = 1024, altura = 600, fps = 100;
        final String nomeJogo = "Space Invaders";
        
        // Criacao do jogo
        FX_CG_2D_API jogo = new FX_CG_2D_API(fps, largura, altura) {

        	int vy = 5;
        	int shootAlienY = -4;
        	boolean left = false;
        	boolean right = false;
        	boolean shoot = false;
        	boolean drawAlien = true;
        	boolean moveBicho = false;
        	boolean shooting = false;
        	boolean shootingAlien = false;
        	boolean win = false;
        	boolean lose = false;
        	int score = 0;
        	int aliensDown = 0;
        	boolean reloadBullet = true;
        	boolean startedNow  = true;
        	int lives = 3;
        	boolean atirou = false;
        	double speed = 1;
        	
        	LinkedList<ShootSpaceship> shoots = new LinkedList<ShootSpaceship>();
        	LinkedList<Alien> aliens = new LinkedList<Alien>();
        	LinkedList<ShootAlien> shootAliens = new LinkedList<ShootAlien>();
        	
        	Alien alien = new Alien();
        	Spaceship spaceship = new Spaceship();
        	
        	
        	// Metodo que desenha os objetos do jogo. Chamado continuamente.
			@Override
			public void desenhar() {

                retangulo(0, 0, largura, altura, Estilo.PREENCHIDO);
                imagem(background, 0, 0);
                          
                empilhar();
                	transladar(largura/2, altura/2);
                	contorno(3, Color.BLUE);
                	preenchimento(Color.BLUE);
                	
                	// FAZ AS CONFIGURAÇÕES INICIAIS DO JOGO
                	if (startedNow) {
                		spaceship.setImage(spaceshipImage);
                		spaceship.setX(-50);
                    	spaceship.setY(170);
                    	alien.setImage(alienImage);
                    	startedNow = false;
                	}
                	contorno(Color.GREEN);
                	preenchimento(Color.GREEN);
                	//linha(-500, 171, 500, 171);
                	imagem(spaceship.getImage(), spaceship.getX(), spaceship.getY());
                	int aux = -50;
                	for (int i = 0; i < lives; i++) {
                		imagem(livesImage, aux, -290);
                		aux += 50;
                	}
                	
                	if (moveBicho) {
                 		if (alien.getXAlien() <= -95) {
                 			aliensDown++;
                 			moveBicho = false;
                 		}
                 		alien.setXAlien(alien.getXAlien() - speed);
             			
                 	} else {
                 		if (alien.getXAlien() >= 95) {
                 			aliensDown++;
                 			moveBicho = true;
                 		}
                 		alien.setXAlien(alien.getXAlien() + speed);
                 	}
                	
                	if (aliensDown == 2) {
                		aliensDown = 0;
                		speed += 0.2;
                		
                		alien.setXAlien(alien.getXAlien());
                		for (Alien a : aliens) {
                			a.setY(a.getY() + 50);
                		}
                	}
                	
                	for (Alien a : aliens) {
                		ShootAlien sa = new ShootAlien();
                		if (a.getMoveLeft()) {
                			imagem(alien.getImage(), a.getX() + alien.getXAlien(), a.getY());
                		} else {
                			imagem(alien.getImage(), a.getX() - alien.getXAlien(), a.getY());
                		}
                		
                		if (!shootingAlien && reloadBullet) {
                			if (a.getX() + alien.getXAlien() == spaceship.getX()) {
                				int xAlien = (int) alien.getXAlien();
                				sa.setX(a.getX() + xAlien);
                				sa.setY(a.getY());
                				shootAliens.add(sa);
                				reloadBullet = false;
                			}	
                		}
                		
                	}
                	
                	if(shooting) {
                		empilhar();
                		contorno(3, Color.RED);
                    	preenchimento(Color.RED);
                    	
                    	for (ShootSpaceship shoot : shoots) {
                    		shoot.setImage(bullet);
                    		imagem(shoot.getImage(), shoot.getX() + 4, shoot.getY() - 10);
                    		
                    		boolean colidiu = false;
                    		for (Alien a : aliens) {
                    			if (a.getMoveLeft()) {
                    				if (colisao(shoot.getX() + 4, shoot.getY() - 10, a.getX() + alien.getXAlien(), a.getY(), 24, 24, 34, 34)) {
                        				score += 100;
                            			colidiu = true;
                            			aliens.remove(a);
                            			shoots.remove(shoot);
                            			
                        				imagem(explosion, a.getX() + alien.getXAlien(), a.getY());
                            			
                            			break;
                            		}
                    			} else {
                    				if (colisao(shoot.getX() + 4, shoot.getY() - 10, a.getX() - alien.getXAlien(), a.getY(), 24, 24, 34, 34)) {
                    					score += 100;
                    					colidiu = true;
                            			aliens.remove(a);
                            			shoots.remove(shoot);
                            			imagem(explosion, a.getX() - alien.getXAlien(), a.getY());
                            			
                            			break;
                            		}
                    			}
                    		}
                    		
                    		if (colidiu) {
                    			break;
                    		}
                    		
						}    
                    	
                		desempilhar();
                	}
                	// VERIFICA SE ALIENS BATERAM NA NAVE E ATIRA
                	for (Alien a : aliens) {
                		if (a.getY() >= 170) {
                			lose = true;
                			imagem(gameOver, -250, -250);
                			parar();
                		}
                	}
                	
                	// VERIFICA SE TODOS OS ALIENS FORAM "MORTOS"
                	if (aliens.isEmpty()) {
                		win = true;
    			    	imagem(youWin, -250, -250);
    			    }
                	contorno(3, Color.WHITE);
                	preenchimento(Color.WHITE);
                	
                	texto("Score: " + score, -500, -260, 40);
                
                	// DISPARA OS TIROS DOS ALIENS
                	for (ShootAlien sa : shootAliens) {
	            		sa.setImage(fireAlien);
	            		imagem(sa.getImage(), sa.getX(), sa.getY());
	            		shootingAlien = true;
	            		break;
                    }
               	 // VERIFICA SE TIRO DE ALIEN ACERTOU A NAVE
                	
                		if (lives == 0) {
                			imagem(gameOver, -250, -250);
                			spaceship.setImage(null);
            				parar();
                		}
                		
                desempilhar();
                
			}

            // Metodo chamado continuamente no loop do jogo. 
			// Usado pra atualizar valores e fazer calculos de posicoes, etc...
			@Override
			public void atualizar() {
				if (shoot) {							
					ShootSpaceship s = new ShootSpaceship();						
        			s.setX(spaceship.getX() + 37);
        			s.setY(spaceship.getY());
        			shoots.add(s);            			
        			shoot = false;            			
        			shooting = true;            			
				}
				
			   if (left && spaceship.getX() >= -512) {
				   spaceship.setX(spaceship.getX() - 2);
			    }
			   
			    if (right && spaceship.getX() <= 408) {
			    	spaceship.setX(spaceship.getX() + 2);
			        
			    }
			    
			    if (shooting) {
			    	for (ShootSpaceship shoot : shoots) {
						shoot.setY(shoot.getY() - vy);
					}
			    }
			    
			 // VERIFICA SE TIRO DE ALIEN ACERTOU A NAVE
            	for (ShootAlien sa : shootAliens) {
            		if (colisao(sa.getX(), sa.getY(), spaceship.getX(), spaceship.getY(), 24, 24, 90, 130)) {
            			lives--;
            			if (lives == 2) {
            				spaceship.setImage(spaceshipImage1);	
            			} else if (lives == 1) {
            				spaceship.setImage(spaceshipImage2);
            			}
            			
            			shootAliens.clear();
            			shootingAlien = false;
            			reloadBullet = true;
            			break;
            		}
            	}
			    
		
			    
			    // CRIA ALIENS
			    if (drawAlien) {
			    	
			    	alien.createAliens(aliens, true, -250, 1);
			    	
			    	alien.createAliens(aliens, false, -200, 2);
			    	
			    	alien.createAliens(aliens, true, -150, 3);
			    	
			    	alien.createAliens(aliens, false, -100, 4);
			    	
			    	alien.createAliens(aliens, true, -50, 5);
				    
			    }
			    drawAlien = false;
			    
			    // FAZ COM QUE O JOGO NÃO TENHA FIM
			    if (aliens.size() < 10) {
			    	alien.createAliens(aliens, true, -250, 1);
			    }
			    
			    if (shootingAlien) {
			    	// REMOVE TIROS QUE JÁ PASSARAM
	            	for (ShootAlien sa : shootAliens) {
	            		if (sa.getY() >= (spaceship.getY() + 50)) {
	            			shootAliens.remove(sa);
	            			reloadBullet = true;
	            			shootingAlien = false;
	            			break;
	            		} else {
	            			
				    		sa.setY(sa.getY() - shootAlienY);
	            		}
	            	}
			    }
			}
			
			@Override
			public void movimentoDoMouse(MouseEvent e) { }
			
			@Override
			public void cliqueDoMouse(MouseEvent e) {
				if (e.getX() >= 316 && e.getX() <= 685 &&
						e.getY() >= 343 && e.getY() <= 371) {
					
					if (win) {
						resetar();
						System.out.println("Ganhou");
					} else if (lose) {
						System.out.println("Perdeu");
						resetar();
					}
					
				}
			}
			
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
					if (atirou) {
						atirou = false;
						shoot = false;
					}
					
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
				
				if(e.getCode() == KeyCode.SPACE) {
					if (!atirou) {
						atirou = true;
						shoot = true;
					}
				}
								
			}
			
		};
			    
		// Inicia o jogo
		jogo.iniciar(cena, nomeJogo);
	}
}














