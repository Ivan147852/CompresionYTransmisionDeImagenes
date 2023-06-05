package TPEntrega1;

import java.awt.Color;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
/*import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;*/
import javax.swing.JComponent;

public class marsSurface {
	
	static final int CANTIDAD_COLORES=256;
	static final int LARGO_BLOQUE=500;
	static final int ALTO_BLOQUE=500;
	static final int CANTIDAD_LARGO_BLOQUES=4;
	static final int CANTIDAD_BLOQUES=20;
	static final int MINIMO_TIRADAS=10000;
	
	public static void main(String[] args) /*throws IOException*/{

		float entropiaSM[] = new float[CANTIDAD_BLOQUES];
		float entropiaCM[] = new float[CANTIDAD_BLOQUES];
		int primercolor=0;
		//SE FIJA SI LA IMAGEN EXISTE
		BufferedImage img = null;
		JFrame frame = new JFrame();
		JFileChooser fc = new JFileChooser();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int returnVal = fc.showDialog(frame, "Adjuntar");
		File file = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			System.out.print(file + "\n");
		}
		try {
			img= ImageIO.read(file);
		} catch (IOException e) {
		System.out.println(e.getMessage());
		}

		//ITERA POR CADA BLOQUE DE PIXELES DE LA IMAGEN
 		for (int w = 0; w < CANTIDAD_BLOQUES; w++)
		{
 			float prob[] = new float[CANTIDAD_COLORES];
 			float probcond[][] = new float [CANTIDAD_COLORES][CANTIDAD_COLORES];
			
			//SE OBTIENE LA CANTIDAD DE VECES QUE SALIO CADA COLOR, DE ORDEN 0 Y 1
			obtenerDistribucionColoresOrden0y1(img,w,prob,probcond,primercolor);
			
			//CALCULO DE PROBABILIDADES CONDICIONALES
			calcularMatrizProbCond(probcond,prob);
			
			// CALCULO DE LA ENTROPIA CON MEMORIA
			entropiaCM[w]=calcularEntropiaConMemoria(prob,probcond);
			
			//ADICION DEL PRIMER COLOR, QUE SE QUITO AL PRINCIPIO PARA NO USARLO COMO DATO EN LA FUENTE CON MEMORIA
			prob[primercolor]++;
			//CALCULO DE LA ENTROPIA SIN MEMORIA
			entropiaSM[w]=calcularEntropiaSinMemoria(prob);
			
			//-------- TPE parte 2 --------
			
			calcularArregloProbabilidades(prob);
			// Contar cantidad de colores que obtuvimos
			List<NodoHuff> ArbolHuff = new ArrayList<NodoHuff>();
			for (int i = 0; i < prob.length; i++){
				if (prob[i] != 0){
					NodoHuff aux;
					aux.setHijo1(null);
					aux.setHijo2(null);
					aux.setP(prob[i]);
					aux.setColor(i);
					ArbolHuff.add(aux);
					Collections.sort(ArbolHuff);
					//InsertarOrdenado(aux, ArbolHuff);
				}
			}
			ArmarArbolHuff(ArbolHuff);
			//byte cod = 0;
			//int longitud = 0;
			ArrayList<Codificacion> codificaciones = new ArrayList<Codificacion>();
			getCodificaciones(ArbolHuff.get(1), (byte) 0, 0, codificaciones);
			generarBits(codificaciones,w,img);
	
		}
 		/*
 		//CALCULO DE LA ENTROPIA PROMEDIO
		float promedioentropia=0;
		for (int j=0; j<CANTIDAD_BLOQUES; j++)
		{
			promedioentropia+=entropiaCM[j];
			System.out.println("La entropíaSM en " + j + " es=" + entropiaSM[j]);
			System.out.println("La entropiaCM en " + j + " es=" + entropiaCM[j]);
		}
		promedioentropia/=CANTIDAD_BLOQUES;
		System.out.println("Promedio entropia="+promedioentropia);
		
		//SE FIJA QUE BLOQUE ES EL DE MAYOR, MENOR ENTROPIA Y EL MAS CERCANO AL VALOR PROMEDIO
		int medio=0; int menor=0; int mayor=0;
		for (int i=1; i<CANTIDAD_BLOQUES; i++)
		{
			if (entropiaCM[menor]>entropiaCM[i]){
				menor=i;}
			if (entropiaCM[mayor]<entropiaCM[i]){
				mayor=i;}
			if (Math.abs(entropiaCM[i]-promedioentropia)<Math.abs(entropiaCM[medio]-promedioentropia)){
				medio=i;}
		}
		
		float probmenoracum[] = new float[CANTIDAD_COLORES];
		float probmayoracum[] = new float[CANTIDAD_COLORES];
		float matmenoracum[][] = new float[CANTIDAD_COLORES][CANTIDAD_COLORES];
		float matmayoracum[][] = new float[CANTIDAD_COLORES][CANTIDAD_COLORES];
		
		//OBTENCION DE DISTRIBUCION DE COLORES DE ORDEN 0 Y 1 DE LOS BLOQUES DE MAYOR Y MENOR ENTROPIA Y CALCULO DE LA
		//MATRIZ CONDICIONAL
		obtenerDistribucionColoresOrden0y1(img,menor,probmenoracum,matmenoracum,primercolor);
		calcularMatrizProbCond(matmenoracum,probmenoracum);
		
		obtenerDistribucionColoresOrden0y1(img,mayor,probmayoracum,matmayoracum,primercolor);
		calcularMatrizProbCond(matmayoracum,probmayoracum);
		
	    //CALCULO DE LA MATRIZ CONDICIONAL Y ARREGLO (VECTOR ESTACIONARIO) DE PROBABILIDADES ACUMULADAS
		probmenoracum[primercolor]++;
		calcularArregloProbabilidades(probmenoracum);
	    calcularArregloAcumulado(probmenoracum);
	    calcularMatrizAcumulada(matmenoracum);
	    
	    probmayoracum[primercolor]++;
	    calcularArregloProbabilidades(probmayoracum);
	    calcularArregloAcumulado(probmayoracum);
	    calcularMatrizAcumulada(matmayoracum);
		
		//CALCULO DE LA MEDIA Y DESVIO ESTANDAR DE LAS ENTROPIAS DE MAYOR Y MENOR VALOR
		float probmediamenor=0;
		float probmediamayor=0;
		float desvioestandarmenor=0;
		float desvioestandarmayor=0;
		probmediamenor=calcularProbMedia(probmenoracum,matmenoracum);
		desvioestandarmenor=calcularDesvioEstandar(probmenoracum,matmenoracum);
		probmediamayor=calcularProbMedia(probmayoracum,matmayoracum);
		desvioestandarmayor=calcularDesvioEstandar(probmayoracum,matmayoracum);
		
		float probmediaacum[] = new float[CANTIDAD_COLORES];
		float matmediaacum[][] = new float[CANTIDAD_COLORES][CANTIDAD_COLORES];
		//OBTENCION DE DISTRIBUCION DE COLORES DE ORDEN 0 Y 1 DEL BLOQUE CON VALOR MAS CERCANO A LA MEDIA
		obtenerDistribucionColoresOrden0y1(img,menor,probmediaacum,matmediaacum,primercolor);
		calcularMatrizProbCond(matmediaacum,probmediaacum);
		
		System.out.println("La media del bloque de menor entropia es="+probmediamenor);
		System.out.println("La media del bloque de mayor entropia es="+probmediamayor);
		System.out.println("El desvio del bloque de menor entropia es="+desvioestandarmenor);
		System.out.println("El desvio del bloque de mayor entropia es="+desvioestandarmayor);
		System.out.println("El bloque con valor mas cercano a la media es="+medio);
		*/
	}
	
	public static void generarBits(ArrayList<Codificacion> codificaciones, int nro_bloque, BufferedImage img){
		int posicion = 0;
		int cantbits = 0;
		ArrayList<Byte> compresion= new ArrayList<Byte>();
		byte mask = (byte) (1 <<(8-1));
		for(int x = (nro_bloque/CANTIDAD_LARGO_BLOQUES)*ALTO_BLOQUE; x < (nro_bloque/CANTIDAD_LARGO_BLOQUES)*ALTO_BLOQUE + ALTO_BLOQUE; x++) 
		{
			for(int y = (nro_bloque%CANTIDAD_LARGO_BLOQUES)*LARGO_BLOQUE; y < (nro_bloque%CANTIDAD_LARGO_BLOQUES)*LARGO_BLOQUE + LARGO_BLOQUE; y++) 
			{
				int rgb= img.getRGB(y, x);
				Color color= new Color(rgb, true);
				Codificacion cod = getCodificacion(codificaciones,color.getBlue());
				ArrayList<Byte> buffer = cod.getCodificacion();
				for (int i=0; i<cod.getLongitud(); i++)
				{
					byte aux = compresion.get(posicion);
					aux<<=1;
					if ((buffer.get(i/8) & mask) == mask)
					{
						aux|=1;
						compresion.set(posicion,aux);
						aux=buffer.get(i/8);
						aux<<=1;
						buffer.set(i/8,aux);
					}
					else
					{
						compresion.set(posicion,aux);
					}
					if (cantbits < 7)
					{
						cantbits++;
					}
					else
					{
						//NECESARIO?
						//compresion.setSize(compresion.size()+1);
						cantbits=0;
						posicion++;
					}
				}
			}
		}
		if (cantbits!=0)
		{
			byte aux=compresion.get(posicion);
			aux <<=(8-cantbits);
			compresion.set(posicion,aux);
		}
		//ESTO SI QUE NO PERRY
		/*else
		{
			compresion.setSize(compresion.size()-1);
		}*/
	}
	
	public static Codificacion getCodificacion(ArrayList<Codificacion> codificaciones, int color)
	{
		for (int i=0; i<codificaciones.size(); i++)
		{
			if (codificaciones.get(i).getColor() == color)
			{
				return codificaciones.get(i);
			}
		}
		return null;
	}
	
	public static void ArmarArbolHuff(List<NodoHuff> ArbolHuff)
	{
		int x = ArbolHuff.size()-1;
		for (int i = 0; i < x; i ++)
		{
			NodoHuff padre = null;
			
			/*NodoHuff aux1 = ArbolHuff.get(ArbolHuff.size);
			NodoHuff aux2 = ArbolHuff.get(ArbolHuff.size-1);
			padre.hijo1 = aux1;
			padre.hijo2 = aux2;
			padre.p = aux1.p + aux2.p;
			padre.color = -1;*/
			
			padre.setHijo1(ArbolHuff.get(ArbolHuff.size()));
			padre.setHijo2(ArbolHuff.get(ArbolHuff.size()-1));
			padre.setP(ArbolHuff.get(ArbolHuff.size()).getP() + ArbolHuff.get(ArbolHuff.size()-1).getP());
			padre.setColor(-1);
			
			ArbolHuff.remove(ArbolHuff.size());
			ArbolHuff.remove(ArbolHuff.size());    // a ver que pasa XD 
			ArbolHuff.add(padre);
			Collections.sort(ArbolHuff);
			
		}
	}
	
	public static void getCodificaciones(NodoHuff nodo, byte cod, int longitud, ArrayList<Codificacion> codificaciones)
	{
		byte codificacion = cod;
		//NO LO SE RICK
		if (nodo.getColor() == -1)
		{
			if (longitud>7)
			{
				Iterator<Codificacion> it = codificaciones.iterator();
				Codificacion aux = null;
				while (it.hasNext())
				{aux=it.next();}
				aux.addCodificacion(codificacion);
				longitud=0;
				codificacion=0;
			}
			codificacion<<= 1;
			getCodificaciones(nodo.getHijo1(),codificacion,longitud+1,codificaciones);
			codificacion |= 1;
			getCodificaciones(nodo.getHijo2(),codificacion,longitud+1,codificaciones);

		}
		else
		{
			Iterator<Codificacion> it = codificaciones.iterator();
			Codificacion aux = null;
			while (it.hasNext())
			{aux=it.next();}
			aux.setColor(nodo.getColor());
			if (longitud!=0)
			{
				codificacion<<=(8-longitud);
			}
			aux.addCodificacion(codificacion);
			aux.setLongitud(longitud + (aux.getCodificacion().size()-1)*8);
			codificaciones.add(aux);
		}
	}
	
	public static void RLCSinPerdida(int nro_bloque, BufferedImage img){
	//O AL REVES	
		int rgb= img.getRGB((nro_bloque/CANTIDAD_LARGO_BLOQUES)*ALTO_BLOQUE,(nro_bloque%CANTIDAD_LARGO_BLOQUES)*LARGO_BLOQUE);
		Color color= new Color(rgb, true);
		int colorinicio = color.getBlue(); //PRIMER DATO DE LA IMAGEN
		int contador=0;
		for(int x = (nro_bloque/CANTIDAD_LARGO_BLOQUES)*ALTO_BLOQUE; x < (nro_bloque/CANTIDAD_LARGO_BLOQUES)*ALTO_BLOQUE + ALTO_BLOQUE; x++) 
		{
			for(int y = (nro_bloque%CANTIDAD_LARGO_BLOQUES)*LARGO_BLOQUE; y < (nro_bloque%CANTIDAD_LARGO_BLOQUES)*LARGO_BLOQUE + LARGO_BLOQUE; y++) 
			{
				rgb= img.getRGB(y, x);
				color= new Color(rgb, true);
				int colorsig= color.getBlue();
				if ( colorsig != colorinicio)
				{
					//copiar al encabezado [dato,contador]
					contador=1;
					colorinicio = colorsig;
				}
				else 
				{
					contador++;
				}
			}
		}
	}

	public static void calcularMatrizProbCond(float[][] probcond, float prob[])
	{
		for (int i = 0; i<CANTIDAD_COLORES; i++)
		{
			if (prob[i]!=0)
			{
				for (int j= 0; j<CANTIDAD_COLORES; j++)
				{
					probcond[j][i] /= prob[i];
				}
			}
		}
	}
	
	public static void calcularArregloProbabilidades(float[] prob)
	{
		for (int i=0; i<CANTIDAD_COLORES; i++)
		{
			prob[i]/=LARGO_BLOQUE*ALTO_BLOQUE;
		}
	}
	
	public static void obtenerDistribucionColoresOrden0y1(BufferedImage img, int nro_bloque, float[] prob, float[][] probcond, int primercolor)
	{
		int coloranterior=-1;
		for(int x = (nro_bloque/CANTIDAD_LARGO_BLOQUES)*ALTO_BLOQUE; x < (nro_bloque/CANTIDAD_LARGO_BLOQUES)*ALTO_BLOQUE + ALTO_BLOQUE; x++) 
		{
			for(int y = (nro_bloque%CANTIDAD_LARGO_BLOQUES)*LARGO_BLOQUE; y < (nro_bloque%CANTIDAD_LARGO_BLOQUES)*LARGO_BLOQUE + LARGO_BLOQUE; y++) 
			{
				int rgb= img.getRGB(y, x);
				Color color= new Color(rgb, true);
				//SI EL COLOR ANTERIOR EXISTE, ITERO NORMALMENTE
				if (coloranterior!=-1)
				{
					prob[color.getBlue()]++;
					probcond[color.getBlue()][coloranterior]++;
				}
				//SI EL COLOR ANTERIOR NO EXISTE (ES LA PRIMERA ITERACION) GUARDO EL PRIMER COLOR
				else 
				{
					primercolor = color.getBlue();
				}
				coloranterior=color.getBlue();
			}
		}
	}
	
	public static float calcularEntropiaConMemoria(float[] prob, float[][] probcond)
	{
		float resultado=0;
		for (int k=0; k<CANTIDAD_COLORES; k++)
		{
			for (int u=0; u<CANTIDAD_COLORES; u++)
			{
				float aux=0;
				if (probcond[k][u]!=0)
				{
					aux+=(-probcond[u][k]*(Math.log(probcond[k][u]) / Math.log(2)));
					resultado += (float) (prob[k]/(LARGO_BLOQUE*ALTO_BLOQUE-1)* aux);
				}	
			}
		}
		return resultado;
	}
	
	public static float calcularEntropiaSinMemoria(float[] prob)
	{
		float resultado=0;
		for (int k=0; k<CANTIDAD_COLORES; k++)
		{	
			if (prob[k]!=0)
			{
				resultado+=-prob[k]/(LARGO_BLOQUE*ALTO_BLOQUE)*(Math.log(prob[k]/(LARGO_BLOQUE*ALTO_BLOQUE)) / Math.log(2));
			}
		}
		return resultado;
	}
	
	public static void calcularMatrizAcumulada(float[][] mat)
	{
		for (int i=0; i<CANTIDAD_COLORES; i++)
		{
			for (int j=1; j<CANTIDAD_COLORES; j++)
			{
				mat[j][i]+=mat[j-1][i];
			}
		}
		corregirPuntoFlotanteMatriz(mat);
	}
	
	public static void calcularArregloAcumulado(float[] arr)
	{
		for (int i=1; i<CANTIDAD_COLORES; i++)
		{
			arr[i]+=arr[i-1];
		}
	}
	
	public static void corregirPuntoFlotanteMatriz(float[][] mat)
	{
		for (int i=0; i<CANTIDAD_COLORES; i++)
		{
			if (mat[CANTIDAD_COLORES-1][i]!=0)
			{
				int j=CANTIDAD_COLORES-1;
				while (mat[j][i]-mat[j-1][i]==0)
				{
					mat[j][i]=1;
					j--;
				}
				mat[j][i]=1;
			}
		}
	}
	
	//MOTOR DE MONTECARLO PARA CALCULAR LA PROBABILIDAD MEDIA Y EL DESVIO DE LOS BLOQUES CON MAYOR Y MENOR ENTROPIA
	public static float calcularProbMedia(float[] probacum, float[][] matprobacum)
	{
	    float media_ant=0;
	    float media_act=0;
	    int suma=0;
	    int tiradas=0;
	   
	    while (tiradas<MINIMO_TIRADAS || ! converge(media_act,media_ant))
	    {
	        int x=sacarPrimerColor(probacum);
	        int y=sacarSegundoColor(matprobacum,x);
	        tiradas++;
	        suma+=y;
	        media_ant=media_act;
	        media_act=(float) suma/tiradas;
	    }
	    return media_act;
	}

	public static float calcularDesvioEstandar(float[] probacum, float[][] matprobacum)
	{
		float desvio_ant=0;
	    float desvio_act=0;
	    int media=0;
	    int tiradas=0;
	    int sumatotal=0;
	    float sumadesvio=0;
	    while (tiradas<MINIMO_TIRADAS || ! converge(desvio_act,desvio_ant))
	    {
	        int x=sacarPrimerColor(probacum);
	        int y=sacarSegundoColor(matprobacum,x);
            sumatotal+=y;
            tiradas++;
            media=sumatotal/tiradas;
            sumadesvio+=Math.pow(y-media,2);
            desvio_ant=desvio_act;
            desvio_act=(float) Math.sqrt(sumadesvio/tiradas);
	    }
	    return desvio_act;
	}
	
	public static int sacarPrimerColor(float[] probacum)
	{
	    float p=(float) Math.random();
	    for (int i=0; i<CANTIDAD_COLORES; i++)
	    {
	        if (p<probacum[i])
	        {
	            return i;
	        }
	    }
	    return -1;
	}

	public static int sacarSegundoColor(float matprobacum[][], int x)
	{
	    float p=(float) Math.random();
	    for (int i=0; i<CANTIDAD_COLORES; i++)
	    {
	        if (p<matprobacum[i][x])
	        {
	            return i;
	        }
	    }
	    return -1;
	}

	public static boolean converge(float act, float ant)
	{
	    for (int i=0; i<CANTIDAD_COLORES; i++)
	    {
	       if (Math.abs(act-ant)>0.0001)
	       {
	            return false;
	       }
	    }
	    return true;
	}

}

