package pdf.Merge;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class PdfMergeCreate {

	private static final String PAYMENT_DETAILS = "\\支払明細";

	pdfMergeProcess pmp = new pdfMergeProcess();

	public void pdfMerge(String[] pdfPaths, String pass) {

		String path= null;

		//第一引数のファイルパスを取得
		path = FilenameUtils.getFullPath(pdfPaths[0]);

		//Marge後のフォルダ
		String mergeDir = path + PAYMENT_DETAILS;

		File newdir = new File(mergeDir);
		if(!newdir.exists()) {
			newdir.mkdir();
		}

		//paths内のファイルをListに入れる
		//pmp.pdfFileNameSetList(paths);

		//各フォルダ内のファイルを年月、コードごとに結合させる
		//pmp.pdfFileMager(paths, pass);

		//ファイル結合処理を行う
		pmp.pdfFileMerge(pdfPaths, mergeDir, pass);

	}
}
