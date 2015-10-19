package it.unical.mat.reversi.core;

public class Scacchiera {
	
	private final Cella[][] scacchiera = new Cella[8][8];
	
	// aggiunto costruttore che inizializza la scacchiera a celle vuote
	Scacchiera() {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				this.scacchiera[i][j] = Cella.VUOTA;
	}
	
	Cella getCella(final int r, final int c) {
		return this.scacchiera[r][c];
	}
	
	void setCella(final int r, final int c, final Cella cella) {
		this.scacchiera[r][c] = cella;
	}
	
}
