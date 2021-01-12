package PdfMarge;

import java.io.File;

public class PdfMargeCreate {

	private String PAYMENT_DETAILS = "\\支払明細";

	pdfMargeProcess pmp = new pdfMargeProcess();

	public void pdfMarge(String[] paths, String pass) {

		String p = null;

		String path= null;

		StringBuilder strb = new StringBuilder(paths[0]);


		for(int i = strb.length() -1 ; i > 0 ; i--) {
			p = String.valueOf(strb.charAt(i));
			//ファイル名を削除
			if(p.equals("\\")) {
				strb.setLength(strb.length() - (strb.length() - i));
				path = strb.toString();
				break;
			}
		}

		//Marge後のファイル
		String margedir = path + PAYMENT_DETAILS;

		File newdir = new File(margedir);
		if(!newdir.exists()) {
			newdir.mkdir();
		}

		//paths内のファイルをListに入れる
		pmp.pdfFileNameSetList(paths);

		//各フォルダ内のファイルを年月、コードごとに結合させる
		pmp.pdfFileMager(paths, pass);

		System.out.println("Marge PDF File Complete!!");

	}
}
