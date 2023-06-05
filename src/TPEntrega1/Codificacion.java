package TPEntrega1;
import java.util.*;

public class Codificacion {
	
	int color;
	int longitud;
	ArrayList<Byte> codificacion = new ArrayList<Byte>();
	
	public int getLongitud(){
		return longitud;
	}
	
	public int getColor(){
		return color;
	}

	public ArrayList<Byte> getCodificacion(){
		return codificacion;
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	public void setLongitud(int longitud){
		this.longitud = longitud;
	}
	
	public void setCodificacion(ArrayList<Byte> codificacion){
		this.codificacion = codificacion;
	}
	
	public void addCodificacion(Byte codificacion)
	{
		this.codificacion.add(this.codificacion.size()-1, codificacion);
	}
}
