package PdfMarge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

public class PdfMargeName {

	//給与、賞与、振込明細のフォルダ内のファイルを追加するList
	ArrayList<String[]> FILE_LIST = new ArrayList<String[]>();

	//ArrayList<String[]> list = new ArrayList<String[]>();

	//年月の要素数
	public static int YEAR_MANTH_NUMBER = 0;

	//コードの要素数
	public static int EMPLOYEE_CODE_NUMBER = 1;


	/**
	 * ファイル名から【】を削除
	 * @param file
	 * @return name
	 */
	public String pdfMargeGetName(String fileName) {

		String name = null;

		String p = null;

		for(int i = 0; i < fileName.length(); i++) {
			p = String.valueOf(fileName.charAt(i));
			//文字列から "】"を検索し、それ以降の文字列を取得
			if(p.equals("】")) {
				name = fileName.substring(i + 1);
				break;
			}
		}
		return name ;
	}


	/**
	 *	各フォルダに入ってあるpdfファイル名をListに全て入れる
	 * @param paths
	 */
	public void pdfFileNameSetList(String[] paths) {


		for(int i = 0; i < paths.length ; i++) {
			File f = new File(paths[i]);
			//ファイルパス内のファイル名を配列に入れる
			String[] fList = f.list();
			//配列をListに入れる
			FILE_LIST.add(fList);

		}

	}

	/**
	 *各フォルダの給与、賞与、振込ファイルから同じ年月、コードを検索する
	 * @param paths
	 */

	public void pdfFileMager(String[] paths) {

		ArrayList<String[]> list = null;


		for(int i = 0; i < FILE_LIST.size() - 1; i++) {

			list = pdfMargeGetList(FILE_LIST.get(i));


			for(int j = 0; j < list.size(); j++) {

				// 給与、賞与、振込のファイル名から同じ年月、コードを検索
				if(isYearMonthCodeMatch(list.get(j) , i)){

					String[] margeName = new String[FILE_LIST.size()];

					//結合させるファイル名を取得する
					for(int k = 0; k < FILE_LIST.size() ; k++) {
						margeName[k] = FILE_LIST.get(k)[j];

					}

					// 給与、賞与、振込の年月、コードごとに結合させる
					pdfMargeFileCreate(paths,margeName);

				}
			}
		}
	}


	/**
	 * 給与、賞与、振込の年月、コードごと支払明細pdfを作成する
	 * @param paths
	 * @param margeName
	 */
	private void pdfMargeFileCreate(String[] paths, String[] margeName) {

		String inputName = null;
		List<InputStream> list = new ArrayList<InputStream>();
		String margeFile = null;

		// 支払明細のファイルパスを取得
		margeFile = getSaveFolder(paths[0]) + "\\【支払明細】" + pdfMargeGetName(margeName[0]);


			try {

				//結合させるファイルを取得
				for(int i = 0; i < paths.length; i++) {

					inputName = paths[i] + "\\" + margeName[i];

					try(InputStream input = new FileInputStream(inputName);){
						list.add(input);
					}



				}

				try(FileOutputStream mergedPDFOutputStream =  new FileOutputStream(margeFile);){
					PDFMergerUtility pdfMerger = new PDFMergerUtility();
					pdfMerger.addSources(list);
					pdfMerger.setDestinationStream(mergedPDFOutputStream);

				    //PDFのMerge出力
				    pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
				}





			}catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}


	}


	/**
	 * 支払明細のファイルパスを取得する
	 * @param path
	 * @return filePath
	 */
	public String getSaveFolder(String path) {

		String p = null;
		String filePath = null;

		StringBuilder strb = new StringBuilder(path);
		for(int j = strb.length() -1 ; j > 0 ; j--) {
			p = String.valueOf(strb.charAt(j));
			//ファイル名を削除
			if(p.equals("\\")) {
				strb.setLength(path.length() - (path.length() - j));

				filePath = strb.toString() + "\\支払明細";

				return filePath;

			}
		}

		return filePath;

	}

	/**
	 * 各フォルダから年月、コードが同じファイルを検索する
	 * @param strs
	 * @param num
	 * @return true/false
	 */
	public Boolean isYearMonthCodeMatch(String[] strs, int num) {

		ArrayList<String[]> list = null;

		// FILE_LISTの次の要素から開始
		num = num + 1;

		for(int i = num; i < FILE_LIST.size(); i++) {
			list = pdfMargeGetList(FILE_LIST.get(i));

			for(int j = 0; j < list.size(); j++) {

				//各フォルダに同じ年月、コードのファイルが存在するか検索
				if(strs[YEAR_MANTH_NUMBER].equals(list.get(j)[YEAR_MANTH_NUMBER]) && strs[EMPLOYEE_CODE_NUMBER].equals(list.get(j)[EMPLOYEE_CODE_NUMBER]) ) {

					//　各フォルダに同じ年月、コードのファイルがあるときtrueを返す
					if( i == (FILE_LIST.size() - 1)) {
						return true;
					}

					break;
				}

				// 各フォルダに同じ年月、コードのファイルがないときfalseを返す
				if(j == list.size() - 1) {
					return false;
				}

			}

		}
		return false;
	}


	/**
	 * フォルダ内のファイル名から【】を削除し、"_"で3つに分割する
	 * @param strs
	 * @return list
	 */
	private ArrayList<String[]> pdfMargeGetList(String[] strs) {

		String str = null;

		String p = null;

		String[] strlist = null ;

		ArrayList<String[]> list = new ArrayList<String[]>();



		for(int i = 0; i < strs.length; i++) {

			for(int j = 0; j < strs[i].length(); j++) {
				p = String.valueOf(strs[i].charAt(j));
				//文字列から "】"を検索し、それ以降の文字列を取得
				if(p.equals("】")) {
					str = strs[i].substring(j + 1);

					//年月、コード、名前に分割しListに入れる
					strlist = str.split("_", 3);

					list.add(strlist);

					break;

				}

			}

		}

		return list;
	}



}
