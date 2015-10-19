package it.unical.mat.reversi.core;

public class NessunaMossaPossibileException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NessunaMossaPossibileException(final String string) {
		super(string);
		System.err.println(string);
	}
	
}
