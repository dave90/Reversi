package it.unical.mat.reversi.core;

import android.graphics.Point;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import it.unical.mat.reversi.ai.*;

public class GameManager {
	
	private final Scacchiera scacchiera = new Scacchiera();
	
	private int pedineBianca;
	private int pedineNera;
	private AIAbstractClass ai;
	private final List<Mossa> mosse_effettuate;
	
	private Mossa ultimaMossaComputer;

	public GameManager() {
		this.scacchiera.setCella(3, 3, Cella.BIANCA);
		this.scacchiera.setCella(4, 4, Cella.BIANCA);
		this.scacchiera.setCella(3, 4, Cella.NERA);
		this.scacchiera.setCella(4, 3, Cella.NERA);
		this.pedineBianca = 2;
		this.pedineNera = 2;
		this.mosse_effettuate = new LinkedList<Mossa>();
	}
	
	private boolean controllaMossa(final Mossa mossa) {
		
		final int r = mossa.getRiga() - 1;
		final int c = mossa.getColonna() - 1;
		final Cella giocatore = mossa.getTipo();
		
		// se non contiene gia' un'altra pedina
		if (this.scacchiera.getCella(r, c) != Cella.VUOTA)
			// throw new IllegalArgumentException("Cella occupata");
			return false;
		
		// se la posizione selezionata e' sulla stessa riga, colonna o diagonale
		// di un altra pedina del computer e se mangia almeno una pedina
		int i = 0, j = 0, k = 0;
		
		final Cella avversario = giocatore == Cella.BIANCA ? Cella.NERA
				: Cella.BIANCA;
		
		// riga o colonna
		boolean trovato_riga_o_colonna = false;
		for (; i < 8 && !trovato_riga_o_colonna; i++) {
			if (this.scacchiera.getCella(r, i) == giocatore)
				if (c < i) {
					if (this.scacchiera.getCella(r, c + 1) == avversario)
						for (int t = c + 2; t <= i; t++)
							if (this.scacchiera.getCella(r, t) == Cella.VUOTA)
								break;
							else if (this.scacchiera.getCella(r, t) == giocatore) {
								trovato_riga_o_colonna = true;
								break;
							}
				} else if (this.scacchiera.getCella(r, c - 1) == avversario)
					for (int t = c - 2; t >= i; t--)
						if (this.scacchiera.getCella(r, t) == Cella.VUOTA)
							break;
						else if (this.scacchiera.getCella(r, t) == giocatore) {
							trovato_riga_o_colonna = true;
							break;
						}
			if (this.scacchiera.getCella(i, c) == giocatore)
				if (r < i) {
					if (this.scacchiera.getCella(r + 1, c) == avversario)
						for (int t = r + 2; t <= i; t++)
							if (this.scacchiera.getCella(t, c) == Cella.VUOTA)
								break;
							else if (this.scacchiera.getCella(t, c) == giocatore) {
								trovato_riga_o_colonna = true;
								break;
							}
				} else if (this.scacchiera.getCella(r - 1, c) == avversario)
					for (int t = r - 2; t >= i; t--)
						if (this.scacchiera.getCella(t, c) == Cella.VUOTA)
							break;
						else if (this.scacchiera.getCella(t, c) == giocatore) {
							trovato_riga_o_colonna = true;
							break;
						}
		}
		
		if (trovato_riga_o_colonna)
			return true;
		
		// diagonale
		boolean trovato_diagonale = false;
		for (; j < 8 && !trovato_diagonale; j++) {
			k = 0;
			for (; k < 8 && !trovato_diagonale; k++)
				if (this.scacchiera.getCella(j, k) == giocatore
						&& Math.abs(j - r) == Math.abs(k - c))
					if (j < r && k < c) { // alto sinistra
						if (this.scacchiera.getCella(r - 1, c - 1) == avversario)
							for (int u = r - 2, v = c - 2; u >= j && v >= k; u--, v--)
								if (this.scacchiera.getCella(u, v) == Cella.VUOTA)
									break;
								else if (this.scacchiera.getCella(u, v) == giocatore) {
									trovato_diagonale = true;
									break;
								}
					} else if (j < r && k > c) { // alto destra
						if (this.scacchiera.getCella(r - 1, c + 1) == avversario)
							for (int u = r - 2, v = c + 2; u >= j && v <= k; u--, v++)
								if (this.scacchiera.getCella(u, v) == Cella.VUOTA)
									break;
								else if (this.scacchiera.getCella(u, v) == giocatore) {
									trovato_diagonale = true;
									break;
								}
					} else if (j > r && k > c) { // basso destra
						if (this.scacchiera.getCella(r + 1, c + 1) == avversario)
							for (int u = r + 2, v = c + 2; u <= j && v <= k; u++, v++)
								if (this.scacchiera.getCella(u, v) == Cella.VUOTA)
									break;
								else if (this.scacchiera.getCella(u, v) == giocatore) {
									trovato_diagonale = true;
									break;
								}
					} else if (j > r && k < c) // basso sinistra
						if (this.scacchiera.getCella(r + 1, c - 1) == avversario)
							for (int u = r + 2, v = c - 2; u <= j && v >= k; u++, v--)
								if (this.scacchiera.getCella(u, v) == Cella.VUOTA)
									break;
								else if (this.scacchiera.getCella(u, v) == giocatore) {
									trovato_diagonale = true;
									break;
								}
		}
		
		if (!trovato_riga_o_colonna && !trovato_diagonale)
			// throw new IllegalArgumentException("Mossa non valida");
			return false;
		
		return true;
		
	}
	
	// r e c vanno da 1 a 8
	public Cella getCella(final int r, final int c) {
		
		if (r < 1 || r > 8 || c < 1 || c > 8)
			throw new IllegalArgumentException("Riga o Colonna non validi");
		
		return this.scacchiera.getCella(r - 1, c - 1);
		
	}
	
	public int getPedineBianca() {
		return this.pedineBianca;
	}
	
	public int getPedineNera() {
		return this.pedineNera;
	}
	
	public Mossa getUltimaMossaComputer() {
		return this.ultimaMossaComputer;
	}
	
	public Mossa getUltimaMossaEffettuata() {
		return this.mosse_effettuate.get(this.mosse_effettuate.size() - 1);
	}
	
	private boolean ilGiocatorePuoEffetturareUnaMossa(final Cella cella) {
		// esiste una cella vuota nella quale puo' inserire la sua pedina
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (this.scacchiera.getCella(i, j) == Cella.VUOTA)
					if (this.controllaMossa(new Mossa(i + 1, j + 1, cella)))
						return true;
		return false;
	}
	
	private boolean ilGiocoEFinito() {
		// se non ci sono pi� posti liberi sul campo
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (this.scacchiera.getCella(i, j) == Cella.VUOTA)
					return false;
		return true;
	}

	public void inserisciMossaComputer(final boolean bianca,ComputerPlayCallback callback) {

		this.ai.getMossa(this, bianca,callback);

	}

	public boolean doComputerMove(Point mossa_point,final boolean bianca)throws NessunaMossaPossibileException{
		final Cella giocatore = bianca ? Cella.BIANCA : Cella.NERA;

		if (mossa_point == null)
			if (this.ilGiocatorePuoEffetturareUnaMossa(giocatore))
				return false;
			else
				return true;

		// FIXME da togliere
		final Mossa mossa = new Mossa(mossa_point.x, mossa_point.y, giocatore);

		if (!this.controllaMossa(mossa)) {
			System.out.println("Mossa non valida");
			throw new IllegalArgumentException("Mossa non valida");
		}

		this.ultimaMossaComputer = mossa;

		final Cella avversario = bianca ? Cella.NERA : Cella.BIANCA;

		this.setCella(mossa);

		if (this.ilGiocoEFinito())
			return true;

		if (this.ilGiocatorePuoEffetturareUnaMossa(avversario))
			return false;
		else if (this.ilGiocatorePuoEffetturareUnaMossa(giocatore)) {
			System.out.println("Nessuna mossa possibile per " + avversario);
			throw new NessunaMossaPossibileException(
					"Nessuna mossa possibile per " + avversario);
		} else
			return true;
	}
	
	// r e c vanno da 1 a 8
	public boolean inserisciMossaUmano(final Mossa mossa)
			throws NessunaMossaPossibileException {
		
		final int r = mossa.getRiga();
		final int c = mossa.getColonna();
		final Cella giocatore = mossa.getTipo();
		
		if (r < 1 || r > 8 || c < 1 || c > 8)
			throw new IllegalArgumentException("Riga o Colonna non validi");
		
		final Cella avversario = giocatore == Cella.BIANCA ? Cella.NERA
				: Cella.BIANCA;
		
		if (!this.controllaMossa(mossa)) {
			System.out.println("Mossa non valida");
			throw new IllegalArgumentException("Mossa non valida");
		}
		
		this.setCella(mossa);
		
		if (this.ilGiocoEFinito())
			return true;
		
		if (this.ilGiocatorePuoEffetturareUnaMossa(avversario))
			return false;
		else if (this.ilGiocatorePuoEffetturareUnaMossa(giocatore)) {
			System.out.println("Nessuna mossa possibile per " + avversario);
			throw new NessunaMossaPossibileException(
					"Nessuna mossa possibile per " + avversario);
		} else
			return true;
	}
	
	public void setAI(final AIType tipo) {
		
		try {
			final Constructor<?> constructor = Class.forName(
					"it.unical.mat.reversi.ai.AI" + tipo.name()).getConstructor();
			this.ai = (AIAbstractClass) constructor.newInstance();
			System.out.println("Intelligenza scelta: " + tipo.name());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	// gira le caselle mangiate e aggiorna pedine
	private void setCella(final Mossa mossa) {
		
		final int r = mossa.getRiga() - 1;
		final int c = mossa.getColonna() - 1;
		final Cella giocatore = mossa.getTipo();
		
		System.out.println("SetCella: " + r + ", " + c + ", " + giocatore);
		
		this.scacchiera.setCella(r, c, giocatore);
		
		this.mosse_effettuate.add(mossa);
		
		if (giocatore == Cella.BIANCA)
			this.pedineBianca++;
		else
			this.pedineNera++;
		
		int i = 0, j = 0, k = 0;
		
		final Cella avversario = giocatore == Cella.BIANCA ? Cella.NERA
				: Cella.BIANCA;
		
		int pedineGirate = 0;
		
		// riga o colonna
		for (; i < 8; i++) {
			if (this.scacchiera.getCella(r, i) == giocatore) // riga
				if (c < i) {
					for (int t = c + 1; t < i; t++)
						if (this.scacchiera.getCella(r, t) == avversario) {
							this.scacchiera.setCella(r, t, giocatore);
							pedineGirate++;
						} else if (this.scacchiera.getCella(r, t) == Cella.VUOTA) {
							// cancella quelli fatti
							for (int d = c + 1; d < t; d++) {
								this.scacchiera.setCella(r, d, avversario);
								pedineGirate--;
							}
							break;
						} else
							break;
				} else
					for (int t = c - 1; t > i; t--)
						if (this.scacchiera.getCella(r, t) == avversario) {
							this.scacchiera.setCella(r, t, giocatore);
							pedineGirate++;
						} else if (this.scacchiera.getCella(r, t) == Cella.VUOTA) {
							// cancella quelli fatti
							for (int d = c - 1; d > t; d--) {
								this.scacchiera.setCella(r, d, avversario);
								pedineGirate--;
							}
							break;
						} else
							break;
			if (this.scacchiera.getCella(i, c) == giocatore) // colonna
				if (r < i) {
					for (int t = r + 1; t < i; t++)
						if (this.scacchiera.getCella(t, c) == avversario) {
							this.scacchiera.setCella(t, c, giocatore);
							pedineGirate++;
						} else if (this.scacchiera.getCella(t, c) == Cella.VUOTA) {
							// cancella quelli fatti
							for (int d = r + 1; d < t; d++) {
								this.scacchiera.setCella(d, c, avversario);
								pedineGirate--;
							}
							break;
						} else
							break;
				} else
					for (int t = r - 1; t > i; t--)
						if (this.scacchiera.getCella(t, c) == avversario) {
							this.scacchiera.setCella(t, c, giocatore);
							pedineGirate++;
						} else if (this.scacchiera.getCella(t, c) == Cella.VUOTA) {
							// cancella quelli fatti
							for (int d = r - 1; d > t; d--) {
								this.scacchiera.setCella(d, c, avversario);
								pedineGirate--;
							}
							break;
						} else
							break;
		}
		
		// diagonale
		for (; j < 8; j++) {
			
			k = 0;
			
			for (; k < 8; k++)
				if (this.scacchiera.getCella(j, k) == giocatore
						&& Math.abs(j - r) == Math.abs(k - c))
					if (j < r && k < c) { // alto sinistra
						for (int u = r - 1, v = c - 1; u > j && v > k; u--, v--)
							if (this.scacchiera.getCella(u, v) == avversario) {
								this.scacchiera.setCella(u, v, giocatore);
								pedineGirate++;
							} else if (this.scacchiera.getCella(u, v) == Cella.VUOTA) {
								// cancella quelli fatti
								for (int d = r - 1, e = c - 1; d > u && e > v; d--, e--) {
									this.scacchiera.setCella(d, e, avversario);
									pedineGirate--;
								}
								break;
							} else
								break;
					} else if (j < r && k > c) { // alto destra
						for (int u = r - 1, v = c + 1; u > j && v < k; u--, v++)
							if (this.scacchiera.getCella(u, v) == avversario) {
								this.scacchiera.setCella(u, v, giocatore);
								pedineGirate++;
							} else if (this.scacchiera.getCella(u, v) == Cella.VUOTA) {
								// cancella quelli fatti
								for (int d = r - 1, e = c + 1; d > u && e < v; d--, e++) {
									this.scacchiera.setCella(d, e, avversario);
									pedineGirate--;
								}
								break;
							} else
								break;
					} else if (j > r && k > c) { // basso destra
						for (int u = r + 1, v = c + 1; u < j && v < k; u++, v++)
							if (this.scacchiera.getCella(u, v) == avversario) {
								this.scacchiera.setCella(u, v, giocatore);
								pedineGirate++;
							} else if (this.scacchiera.getCella(u, v) == Cella.VUOTA) {
								// cancella quelli fatti
								for (int d = r + 1, e = c + 1; d < u && e < v; d++, e++) {
									this.scacchiera.setCella(d, e, avversario);
									pedineGirate--;
								}
								break;
							} else
								break;
					} else if (j > r && k < c) // basso sinistra
						for (int u = r + 1, v = c - 1; u < j && v > k; u++, v--)
							if (this.scacchiera.getCella(u, v) == avversario) {
								this.scacchiera.setCella(u, v, giocatore);
								pedineGirate++;
							} else if (this.scacchiera.getCella(u, v) == Cella.VUOTA) {
								// cancella quelli fatti
								for (int d = r + 1, e = c - 1; d < u && e > v; d++, e--) {
									this.scacchiera.setCella(d, e, avversario);
									pedineGirate--;
								}
								break;
							} else
								break;
			
		}
		
		if (giocatore == Cella.BIANCA) {
			this.pedineBianca += pedineGirate;
			this.pedineNera -= pedineGirate;
		} else {
			this.pedineBianca -= pedineGirate;
			this.pedineNera += pedineGirate;
		}
		
	}

}
