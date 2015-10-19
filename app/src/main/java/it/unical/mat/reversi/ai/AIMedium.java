package it.unical.mat.reversi.ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AIMedium extends AIAbstractClass {
	
	public AIMedium() {
		super();
	}
	
	@Override
	protected String creaFileOptimize() {
		
		String temp_file_optimize = "";
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					"FilesDLV" + File.separator
							+ "file_strategia_quanti_ne_mangia.dl"));
			
			String line;
			while ((line = bufferedReader.readLine()) != null)
				temp_file_optimize += line + "\n";
			
			bufferedReader.close();
			
			bufferedReader = new BufferedReader(new FileReader("FilesDLV"
					+ File.separator
					+ "file_strategia_angoli_e_pedine_stabili.dl"));
			
			while ((line = bufferedReader.readLine()) != null)
				temp_file_optimize += line + "\n";
			
			bufferedReader.close();
			
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temp_file_optimize;
		
	}
	
}
