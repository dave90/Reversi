package it.unical.mat.reversi.ai;

import android.content.Context;
import android.graphics.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import it.unical.mat.embasp.base.ASPHandler;
import it.unical.mat.embasp.base.AnswerSetCallback;
import it.unical.mat.embasp.base.AnswerSets;
import it.unical.mat.embasp.dlv.DLVHandler;

import it.unical.mat.reversi.core.Cella;
import it.unical.mat.reversi.core.ComputerPlayCallback;
import it.unical.mat.reversi.core.GameManager;

public abstract class AIAbstractClass implements AnswerSetCallback{
	
	private String inizio_file_fatti;
	private final String file_guess_and_check;
	private ASPHandler handler;
	ComputerPlayCallback callback;
	private Context context;

	public AIAbstractClass() {
		
		String temp_file_guess_and_check = "";
		handler=new DLVHandler();
		try {
			final BufferedReader bufferedReader = new BufferedReader(
					new FileReader("FilesDLV" + File.separator
							+ "file_guess_and_check.dl"));
			
			String line;
			while ((line = bufferedReader.readLine()) != null)
				temp_file_guess_and_check += line + "\n";
			
			bufferedReader.close();
			
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.file_guess_and_check = temp_file_guess_and_check;
		
	}
	
	private void chiamaDLV(final GameManager gameManager) {
		
		final String file_fatti = this.creaFileFatti(gameManager);
		
		System.out.println("file_fatti: " + file_fatti);
		
		// System.out.println(this.file_guess_and_check);
		
		final String file_optimize = this.creaFileOptimize();
		
//		try {


//			final Process process = Runtime.getRuntime().exec(
//					"dlv.mingw.exe -silent -filter=posizioneScelta --");

//			final PrintWriter printWriter = new PrintWriter(
//					process.getOutputStream());
//			printWriter.write(file_fatti);
//			printWriter.write(this.file_guess_and_check);
//			printWriter.write(file_optimize);
//			printWriter.close();
			handler.addRawInput(file_fatti);
			handler.addRawInput(this.file_guess_and_check);
			handler.addRawInput(file_optimize);

//			final String readLine = new BufferedReader(new InputStreamReader(
//					process.getInputStream())).readLine();

			//handler.start(context,this);
//			System.out.println("readLine : " + readLine);
			handler.start(context,this);

//		} catch (final IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

	}
	
	private String creaFileFatti(final GameManager gameManager) {
		
		String pedinePresenti = "";
		
		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++) {
				final Cella cella = gameManager.getCella(i, j);
				if (cella != Cella.VUOTA)
					pedinePresenti += "pedinaPresente(" + i + ", " + j + ", "
							+ (cella == Cella.BIANCA ? "b" : "n") + ").";
			}
		
		return this.inizio_file_fatti + pedinePresenti;
	}
	
	protected abstract String creaFileOptimize();
	
	public void getMossa(final GameManager gameManager, final boolean bianca,ComputerPlayCallback callback) {
		
		this.inizio_file_fatti = "rigaOcolonna(1..8).";
		
		this.inizio_file_fatti += "coloreComputer(" + (bianca ? "b" : "n")
				+ ").";
		this.callback=callback;
		this.chiamaDLV(gameManager);
	}
	
	private Point parseResult(final String risultatoDLV) {
		
		final int indexOf_inizio = risultatoDLV.indexOf("(");
		final int indexOf_fine = risultatoDLV.indexOf(")");
		
		final String[] point = risultatoDLV
				.substring(indexOf_inizio + 1, indexOf_fine).replace("\\s", "")
				.split(",");
		return new Point(Integer.parseInt(point[0]), Integer.parseInt(point[1]));
		
	}


	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public void callback(AnswerSets answerSets) {
		String answerString=answerSets.getAnswerSetsString();
		Point p=parseResult(answerString);
		callback.callback(p);
	}
}
