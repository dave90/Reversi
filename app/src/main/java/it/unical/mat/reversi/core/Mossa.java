package it.unical.mat.reversi.core;

public class Mossa {
	
	// da 1 a 8
	private final int riga;
	private final int colonna;
	
	private final Cella tipo;
	
	public Mossa(final int riga, final int colonna, final Cella tipo) {
		super();
		this.riga = riga;
		this.colonna = colonna;
		this.tipo = tipo;
	}
	
	public int getColonna() {
		return this.colonna;
	}
	
	public int getRiga() {
		return this.riga;
	}
	
	public Cella getTipo() {
		return this.tipo;
	}
	
	@Override
	public String toString() {
		final char c = (char) (this.colonna + 64);
		return c+" "+this.riga+"         "+ this.tipo;
		//return "R:   " + this.riga + "    C:   " + c + "   "
			//	+ this.tipo;
	}
	
}
