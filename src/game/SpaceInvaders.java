package game;
	
import java.awt.List;
import java.util.LinkedList;

import api.FX_CG_2D_API;
import javafx.application.Application;
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
		
		final int largura = 600, altura = 400, fps = 100;
        final String nomeJogo = "Space Invaders";
        
        // Criacao do jogo
        FX_CG_2D_API jogo = new FX_CG_2D_API(fps, largura, altura) {
    
        	int x = -50;
        	int y = 100;
        	int vy = 5;        	        	
        	boolean left = false;
        	boolean right = false;
        	boolean shoot = false;
        	boolean shooting = false;
        	LinkedList<Shoot> shoots = new LinkedList<Shoot>();
        
			
        	// Metodo que desenha os objetos do jogo. Chamado continuamente.
			@Override
			public void desenhar() {

				preenchimento(Color.BLACK);
                retangulo(0, 0, largura, altura, Estilo.PREENCHIDO);                           	
                          
                empilhar();
                	transladar(largura/2, altura/2);
                	contorno(3, Color.BLUE);
                	preenchimento(Color.BLUE);
                	retangulo(x, y, 100, 100, Estilo.PREENCHIDO);
                	
                	if(shooting) {
                		empilhar();
                		contorno(3, Color.RED);
                    	preenchimento(Color.RED);
                    	for (Shoot shoot : shoots) {
                    		circulo(shoot.x, shoot.y, 20, 20, Estilo.PREENCHIDO);
						}                		
                		desempilhar();
                	}
                desempilhar();
                
			}

            // Metodo chamado continuamente no loop do jogo. 
			// Usado pra atualizar valores e fazer c�lculos de posicoes, etc...
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
				
				   if(left){
				        x -= 2;
				    }
				   
				    if(right){
				        x += 2;
				    }
				    
				    if(shooting) {
				    	for (Shoot shoot : shoots) {
							shoot.y -= vy;
						}				    
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
}