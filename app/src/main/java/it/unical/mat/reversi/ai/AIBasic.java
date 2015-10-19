package it.unical.mat.reversi.ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AIBasic extends AIAbstractClass {
	
	public AIBasic() {
		super();
	}
	
	@Override
	protected String creaFileOptimize() {
		
		String temp_file_optimize = "";
		
		try {
			final BufferedReader bufferedReader = new BufferedReader(
					new FileReader("FilesDLV" + File.separator
							+ "file_strategia_quanti_ne_mangia.dl"));
			
			String line;
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
