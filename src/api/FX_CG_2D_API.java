package api;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class FX_CG_2D_API{

    private int fps;
    private Timeline loop;		
    private GraphicsContext gc;
    private Canvas canvas;

    public FX_CG_2D_API(int fps, float w, float h) {
    	
        this.fps = fps;
        this.loop = criarloop();
        this.canvas = new Canvas(w, h);
        this.canvas.setFocusTraversable(true);
        this.gc = canvas.getGraphicsContext2D();

        // Definição de callbacks para mouse e teclado. Os métodos são abstratos para a aplicação do jogo implementar.
        this.gc.getCanvas().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){cliqueDoMouse(e);}
        });
        
        this.gc.getCanvas().setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){movimentoDoMouse(e);}
        });
        
        this.gc.getCanvas().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){teclaPressionada(e);}
        });
        
        this.gc.getCanvas().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){teclaLiberada(e);}
        });
        
        this.gc.getCanvas().setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){teclaDigitada(e);}
        });

    }
    
    /** Método para desenho de pontos2D nas coordenadas X e Y.
    */     
    public void ponto(double x, double y){
    	ponto(x, y, this.gc.getLineWidth());
    }
    
    /** Método para desenho de pontos2D nas coordenadas X e Y com tamanho TAM.
     */
    public void ponto(double x, double y, double tam){
    	double largura = this.gc.getLineWidth();
    	Color contorno = (Color) this.gc.getStroke();
    	Color preenchimento = (Color) this.gc.getFill();    	
    	this.gc.setFill(contorno);
    	this.gc.setLineWidth(tam);
    	circulo((int)x, (int)y, 2, 2, Estilo.LINHAS);
    	circulo((int)x, (int)y, 2, 2, Estilo.PREENCHIDO);
    	this.gc.setFill(preenchimento);
    	this.gc.setLineWidth(largura);
    }
    
    
    /** Método para desenho de círculos sólidos ou apenas com a linha de contorno.
     * O círculo é desenhado nas coordenadas [x,y], com largura l e altura a. Se 
     * o parâmetro estilo for Estilo.PREENCHIDO, ele desenhado sólido, caso contrário, apenas
     * é desenhado o contorno da forma.*/
    public void circulo(int x, int y, int l, int a, Estilo estilo){
    	if(estilo == Estilo.PREENCHIDO){
    		this.gc.fillOval(x, y, l, a);
    	}else{
    		this.gc.strokeOval(x, y, l, a);
    	}
    }
    
    /** Método para desenho de retângulos sólidos ou apenas com a linha de contorno ou apenas com os vértices.
     * O retângulo é desenhado nas coordenadas [x,y], com largura l e altura a. Se o parâmetro estilo for 
     * Estilo.PREENCHIDO, ele desenhado sólido, se for Estilo.LINHAS serão desenhadas as linhas 
     * de contorno da forma, e se for Estilo.PONTOS, serão desenhados apenas os vértices da forma.*/     
    public void retangulo(int x, int y, int l, int a, Estilo estilo){    	    	        
    	if(estilo == Estilo.PREENCHIDO){
    		this.gc.fillRect(x, y, l, a);
    	}else{
    		if(estilo == Estilo.LINHAS){
    			this.gc.strokeRect(x, y, l, a);
    		}else{
    			ponto(x, y);
    	        ponto(x+l-2, y);
    	        ponto(x, y+a-2);
    	        ponto(x+l-2, y+a-2);
    		}
    	}
    }
    
    /** Método para desenho de retângulos sólidos ou apenas com a linha de contorno.
     * Esse método recebe um Rectangle2D para ser desenhado. Ideal para o uso em tratamento de colisões.
     * Se o parâmetro estilo for Estilo.PREENCHIDO, ele desenhado sólido, se for Estilo.LINHAS serão desenhadas as linhas 
     * de contorno da forma, e se for Estilo.PONTOS, serão desenhados apenas os vértices da forma.*/
    public void retangulo(Rectangle2D retangulo, Estilo estilo){
    	double x = retangulo.getMinX();
    	double y = retangulo.getMinY();
    	double l = retangulo.getWidth();
    	double a = retangulo.getHeight();
    	retangulo((int)x, (int)y, (int)l, (int)a, estilo);
    }
    
    /** Método para mudar a cor de preenchimento de um objeto para a Color cor.*/
    public void preenchimento(Color cor){
    	this.gc.setFill(cor);
    }
    
    /** Método para mudar a cor do contorno de um objeto para a Color cor.*/
    public void contorno(Color cor){
    	this.gc.setStroke(cor);
    }
    
    /** Método para mudar a cor e expessura do contorno de um objeto.*/
    public void contorno(double expessura, Color cor){
    	this.gc.setStroke(cor);
    	this.gc.setLineWidth(expessura);
    }
    
    /** Método para mudar a expessura do contorno de um objeto.*/
    public void contorno(double expessura){
    	this.gc.setLineWidth(expessura);
    }
    
    /** Método para desenhar uma linha que vai de [xi,yi] até [xf,yf].
     *	Se o parâmetro estilo for Estilo.LINHAS serão desenhadas as linhas de contorno da forma, 
     *  e se for Estilo.PONTOS, serão desenhados apenas os vértices da forma. */
    public void linha(double xi, double yi, double xf, double yf, Estilo estilo){
    	if(estilo == Estilo.PONTOS){
    		ponto(xi-1, yi-1);
    		ponto(xf-1, yf-1);
    	}else{
	    	this.gc.beginPath();
	    	this.gc.moveTo(xi, yi);
	    	this.gc.lineTo(xf, yf);
	    	this.gc.stroke();
	    	this.gc.closePath();
    	}
    }
    
    /** Método para desenhar uma linha que vai de [xi,yi] até [xf,yf].*/
    public void linha(double xi, double yi, double xf, double yf){    	
    	this.gc.beginPath();
    	this.gc.moveTo(xi, yi);
    	this.gc.lineTo(xf, yf);
    	this.gc.stroke();
    	this.gc.closePath();    
    }


    /** Método para escrever um texto na tela, nas coordenadas [x,y], com a fonte de tamanho tam.*/
    public void texto(String texto, double x, double y, int tam){
    	texto(texto, x, y, tam, FontWeight.NORMAL);
    }
    
    /** Método para escrever um texto na tela, nas coordenadas [x,y], com a fonte de tamanho tam e tipo tipo.*/
    public void texto(String texto, double x, double y, int tam, FontWeight tipo){
        Font fonte = Font.font("Times New Roman", tipo, tam);
        this.gc.setFont(fonte);
        this.gc.fillText(texto, x, y);
        this.gc.strokeText(texto, x, y);
    }
    
    /** Método para detectar a colisão entre dois retângulos.*/
    public boolean colisao(Rectangle2D objeto1, Rectangle2D objeto2){    	
    	return objeto1.intersects(objeto2);     
    }
    
    /** Método para detectar a colisão entre duas formas.*/
    public boolean colisao(double xP1, double yP1, double xP2, double yP2, double larguraP1, double alturaP1, double larguraP2, double alturaP2){
    	Rectangle2D objeto1 = new Rectangle2D(xP1, yP1, larguraP1, alturaP1);
    	Rectangle2D objeto2 = new Rectangle2D(xP2, yP2, larguraP2, alturaP2);
    	return objeto1.intersects(objeto2);     
    }    
    
    /** Método para rotacionar um objeto em ang graus.*/
    public void rotacionar(double ang){
    	this.gc.rotate(ang);        
    }
    
    /** Método para transladar um objeto para as coordenadas [x,y].*/
    public void transladar(double x, double y){
    	this.gc.translate(x,  y);        
    }
    
    /** Método para aplicar a transformação de escala em um objeto nas coordenadas x e y.*/
    public void escalar(double x, double y){
    	this.gc.scale(x, y);
    }
    
    /** Método para desenhar uma imagem na tela. Pode ser usado em conjunto com um Rectangle2D para colisão.*/
    public void imagem(Image img, double x, double y){
    	this.gc.drawImage(img, x, y);
    }
    
    /** Método para empilhar uma transformação.*/
    public void empilhar(){
    	this.gc.save();
    }
    
    /** Método para desempilhar uma transformação.*/
    public void desempilhar(){
    	this.gc.restore();
    }
           
    private void rodar(Event e) {
        this.atualizar();
        this.desenhar();
    }

    /** Método para retomar o jogo pausado.*/
    public void retomar() {
    	this.loop.play();
    }

    /** Método para pausar o jogo.*/
    public void parar() {
    	this.loop.pause();
    }

    /** Método para pausar o jogo.*/
    public void resetar() {
    	this.loop.stop();
    	this.loop.playFromStart();
    }

    private Timeline criarloop() {
        // Baseado em https://carlfx.wordpress.com/2012/04/09/javafx-2-gametutorial-part-2/
        final Duration d = Duration.millis(1000 / fps);
        final KeyFrame oneFrame = new KeyFrame(d, this::rodar);
        Timeline t = new Timeline(fps, oneFrame);
        t.setCycleCount(Animation.INDEFINITE);
        return t;
    }    
        
    /** Método para tratar eventos de tecla pressionada do teclado.*/
    public abstract void teclaPressionada(KeyEvent e);
    
    /** Método para tratar eventos de tecla liberada do teclado.*/
    public abstract void teclaLiberada(KeyEvent e);
    
    /** Método para tratar eventos de tecla digitada do teclado.*/
    public abstract void teclaDigitada(KeyEvent e);
    
    /** Método para tratar eventos de clique do mouse.*/
    public abstract void cliqueDoMouse(MouseEvent e);
    
    /** Método para tratar eventos de movimento do mouse.*/
    public abstract void movimentoDoMouse(MouseEvent e);
    
    /** Método chamado continuamente para atualizar valores e realizar cálculos necessários para o jogo.*/
    public abstract void atualizar();

    /** Método chamado continuamente para desenhar na tela.*/
    public abstract void desenhar();
    
    /** Método para iniciar o jogo, no Stage Cena, com o nome nomeJogo. */
    public void iniciar(Stage cena, final String nomeJogo) {
		// Inicializações do JavaFX para iniciar o jogo.
        cena.resizableProperty().setValue(Boolean.FALSE);
        cena.setScene(new Scene(new StackPane(this.canvas)));
        cena.setTitle(nomeJogo);
        cena.show();
        // Inicia o loop do jogo.
        this.loop.playFromStart();
	}
            
    public static enum Estilo{	
    	PONTOS(1), LINHAS(2), PREENCHIDO(3);    	
    	private final int valor;
    	
    	Estilo(int valor){
    		this.valor = valor;
    	}
    	public int getValor(){
    		return valor;
    	}
    }    
}
